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
package org.stajistics.data;

import java.util.Set;

/**
 * 
 * 
 *
 * @author The Stajistics Project
 */
public class DataSets {

    private DataSets() {}

    public static DataSet unmodifiable(final DataSet dataSet) {
        if (dataSet instanceof ImmutableDataSetDecorator) {
            return dataSet;
        }

        return new ImmutableDataSetDecorator(dataSet);
    }

    /* NESTED CLASSES */

    private static final class ImmutableDataSetDecorator implements DataSet {

        private final DataSet delegate;
        private final MetaData delegateMetaData;
        private final FieldMetaDataSet delegateFieldMetaDataSet;

        ImmutableDataSetDecorator(final DataSet delegate) {
            if (delegate == null) {
                throw new NullPointerException("delegate");
            }

            this.delegate = delegate;
            this.delegateMetaData = 
                new ImmutableMetaDataDecorator(delegate.getMetaData());
            this.delegateFieldMetaDataSet = 
                new ImmutableFieldMetaDataSetDecorator(delegate.getFieldMetaDataSet());
        }

        @Override
        public FieldMetaDataSet getFieldMetaDataSet() {
            return delegateFieldMetaDataSet;
        }

        @Override
        public MetaData getMetaData() {
            return delegateMetaData;
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getField(final String name) {
            return delegate.getField(name);
        }

        @Override
        public <T> T getField(final String name, final Class<T> type) throws ClassCastException {
            return delegate.getField(name, type);
        }

        @Override
        public <T> T getField(final String name, final T defaultValue) {
            return delegate.getField(name, defaultValue);
        }

        @Override
        public Set<String> getFieldNames() {
            // Already unmodifiable
            return delegate.getFieldNames();
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public Object removeField(final String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setField(final String name, final Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return delegate.size();
        }
        
    }

    private static final class ImmutableMetaDataDecorator implements MetaData {

        private final MetaData delegate;

        ImmutableMetaDataDecorator(final MetaData delegate) {
            if (delegate == null) {
                throw new NullPointerException("delegate");
            }
            this.delegate = delegate;
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getField(final String name) {
            return delegate.getField(name);
        }

        @Override
        public <T> T getField(final String name, final Class<T> type) throws ClassCastException {
            return delegate.getField(name, type);
        }

        @Override
        public <T> T getField(final String name, final T defaultValue) {
            return delegate.getField(name, defaultValue);
        }

        @Override
        public Set<String> getFieldNames() {
            // Already unmodifiable
            return delegate.getFieldNames();
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public Object removeField(final String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setField(final String name, final Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return delegate.size();
        }
    }

    private static final class ImmutableFieldMetaDataSetDecorator implements FieldMetaDataSet {

        private final FieldMetaDataSet delegate;

        ImmutableFieldMetaDataSetDecorator(final FieldMetaDataSet delegate) {
            if (delegate == null) {
                throw new NullPointerException("delegate");
            }
            this.delegate = delegate;
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public MetaData getMetaData(final String fieldName) {
            return new ImmutableMetaDataDecorator(delegate.getMetaData(fieldName));
        }

    }
}
