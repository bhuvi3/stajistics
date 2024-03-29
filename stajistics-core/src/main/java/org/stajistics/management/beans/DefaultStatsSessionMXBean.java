/* Copyright 2009 - 2010 The Stajistics Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.stajistics.management.beans;

import static org.stajistics.Util.assertNotEmpty;
import static org.stajistics.Util.assertNotNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ReflectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stajistics.StatsManager;
import org.stajistics.StatsManagerRegistry;
import org.stajistics.StatsProperties;
import org.stajistics.data.DataSet;
import org.stajistics.session.StatsSession;
import org.stajistics.session.StatsSessionManager;

/**
 *
 *
 *
 * @author The Stajistics Project
 */
public class DefaultStatsSessionMXBean implements StatsSessionMXBean,DynamicMBean {

    private static final Logger sessionLogger = LoggerFactory
            .getLogger(DefaultStatsSessionManagerMXBean.SESSION_DUMP_LOGGER_NAME);

    private static final String PROP_CACHE_DATA_SET =
        DefaultStatsSessionMXBean.class.getName() + ".cacheDataSet";
    private static final boolean DEFAULT_CACHE_DATA_SET = true;

    private static final String PROP_CACHED_DATA_SET_LIFE_SPAN =
        DefaultStatsSessionMXBean.class.getName() + ".cachedDataSetLifeSpanMillis";
    private static final long DEFAULT_CACHED_DATA_SET_LIFE_SPAN = 2000; // 2 seconds

    protected static final String ATTR_IMPLEMENTATION = "Implementation";
    protected static final String ATTR_DATA_RECORDERS = "DataRecorders";

    protected static final String OP_CLEAR = "clear";
    protected static final String OP_DESTROY = "destroy";
    protected static final String OP_DUMP = "dump";
    protected static final String OP_COLLECT_DATA = "collectData";

    protected static final String FIELD_PREFIX = "_";

    protected final String namespace;
    protected final org.stajistics.session.StatsSession session;

    // DataSet cache members
    private transient long cachedDataSetLastAccess = 0L;
    private transient WeakReference<DataSet> cachedDataSetRef = null;

    public DefaultStatsSessionMXBean(final String namespace,
                                     final StatsSession session) {
        assertNotEmpty(namespace, "namespace");
        assertNotNull(session, "session");

        this.namespace = namespace;
        this.session = session;
    }

    protected StatsSessionManager getSessionManager() {
        StatsManager manager = StatsManagerRegistry.getInstance().getStatsManager(namespace);
        return manager.getSessionManager();
    }

    public String getImplementation() {
        return session.getClass().getName();
    }

    public String getDataRecorders() {
        return session.getDataRecorders().toString();
    }

    /* Manages a very short-lived cached DataSet for the purposes of throttling
     * calls to StatsSession#collectData().
     */
    private DataSet getDataSet() {

        // If not caching
        if (!StatsProperties.getBooleanProperty(PROP_CACHE_DATA_SET,
                                                DEFAULT_CACHE_DATA_SET)) {
            return session.collectData();
        }

        synchronized (this) {
            final long now = System.currentTimeMillis();

            DataSet dataSet = null;

            if (cachedDataSetRef != null) {
                dataSet = cachedDataSetRef.get();
            }

            final long cacheLifeSpan = StatsProperties.getLongProperty(PROP_CACHED_DATA_SET_LIFE_SPAN,
                                                                       DEFAULT_CACHED_DATA_SET_LIFE_SPAN);

            if (dataSet != null) {
                if (now - cachedDataSetLastAccess > cacheLifeSpan) {
                    dataSet = null;
                }
            }

            if (dataSet == null) {
                dataSet = session.collectData();

                cachedDataSetLastAccess = now;
                cachedDataSetRef = new WeakReference<DataSet>(dataSet);
            }

            return dataSet;
        }
    }

    @Override
    public Object getAttribute(String attribute)
            throws AttributeNotFoundException,MBeanException,ReflectionException {

        if (attribute.equals(ATTR_IMPLEMENTATION)) {
            return getImplementation();
        }

        if (attribute.equals(ATTR_DATA_RECORDERS)) {
            return getDataRecorders();
        }

        if (!attribute.startsWith(FIELD_PREFIX)) {
            throw new AttributeNotFoundException(attribute);
        }

        if (attribute.startsWith(FIELD_PREFIX)) {
            attribute = attribute.substring(FIELD_PREFIX.length());

            Object value = session.getField(attribute);
            if (value == null) {
                throw new AttributeNotFoundException(attribute);
            }

            return value;
        }

        // Not found
        return null;
    }

    @Override
    public AttributeList getAttributes(final String[] attributes) {

        AttributeList attrList = new AttributeList();
        DataSet dataSet = getDataSet();

        for (String name : attributes) {
            if (name.equals(ATTR_IMPLEMENTATION)) {
                attrList.add(new Attribute(name, getImplementation()));

            } else if (name.equals(ATTR_DATA_RECORDERS)) {
                attrList.add(new Attribute(name, getDataRecorders()));

            } else if (!name.startsWith(FIELD_PREFIX)) {
                attrList.add(new Attribute(name, null));

            } else {
                name = name.substring(FIELD_PREFIX.length());
                attrList.add(new Attribute(name, dataSet.getField(name)));
            }
        }

        return attrList;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        DataSet dataSet = getDataSet();

        MBeanAttributeInfo[] attrs = new MBeanAttributeInfo[dataSet.size() + 2];

        int i = 0;

        attrs[i++] = new MBeanAttributeInfo(ATTR_IMPLEMENTATION,
                                            String.class.getName(),
                                            null,
                                            true,
                                            false,
                                            false);
        attrs[i++] = new MBeanAttributeInfo(ATTR_DATA_RECORDERS,
                                            String.class.getName(),
                                            null,
                                            true,
                                            false,
                                            false);

        for (String name : dataSet.getFieldNames()) {
            Object value = dataSet.getField(name);
            attrs[i++] = new MBeanAttributeInfo(FIELD_PREFIX + name,
                                                value.getClass().getName(),
                                                "DataSet field " + name,
                                                true,
                                                false,
                                                false);
        }

        MBeanOperationInfo[] ops = {
            new MBeanOperationInfo(OP_CLEAR,
                                   "Clear collected data",
                                   null,
                                   "void",
                                   MBeanOperationInfo.ACTION),
            new MBeanOperationInfo(OP_DESTROY,
                                   "Destroy this session",
                                   null,
                                   "void",
                                   MBeanOperationInfo.ACTION),
            new MBeanOperationInfo(OP_DUMP,
                                   "Dump session data to log",
                                   null,
                                   "void",
                                   MBeanOperationInfo.ACTION),
            new MBeanOperationInfo(OP_COLLECT_DATA,
                                   "Query all data fields",
                                   null,
                                   "java.util.Map<String,Object>",
                                   MBeanOperationInfo.ACTION)
        };

        return new MBeanInfo(getClass().getName(),
                             StatsSessionMXBean.class.getSimpleName(),
                             attrs,
                             null,
                             ops,
                             null);
    }

    @Override
    public Object invoke(final String actionName,
                         final Object[] params,
                         final String[] signature)
            throws MBeanException, ReflectionException {

        if (actionName.equals(OP_CLEAR)) {
            session.clear();

        } else if (actionName.equals(OP_DESTROY)) {
            getSessionManager().remove(session);

        } else if (actionName.equals(OP_DUMP)) {
            if (sessionLogger.isInfoEnabled()) {
                sessionLogger.info(session.toString());
            }

        } else if (actionName.equals(OP_COLLECT_DATA)) {
            DataSet dataSet = session.collectData();
            Map<String,Object> result = new HashMap<String,Object>(dataSet.size());
            for (String fieldName : dataSet.getFieldNames())
            {
                result.put(fieldName, dataSet.getField(fieldName));
            }

            return result;
        }

        return null;
    }

    @Override
    public void setAttribute(final Attribute attribute)
            throws AttributeNotFoundException,InvalidAttributeValueException,MBeanException,ReflectionException {

    }

    @Override
    public AttributeList setAttributes(final AttributeList attributes) {
        return null;
    }


}
