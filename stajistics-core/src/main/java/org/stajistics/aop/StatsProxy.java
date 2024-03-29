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
package org.stajistics.aop;

import static org.stajistics.Util.assertNotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.stajistics.Stats;
import org.stajistics.StatsConstants;
import org.stajistics.StatsFactory;
import org.stajistics.StatsKey;
import org.stajistics.tracker.span.SpanTracker;

/**
 *
 *
 *
 * @author The Stajistics Project
 */
public class StatsProxy implements InvocationHandler {

    protected static final Method EQUALS_METHOD;
    static {
        try {
            EQUALS_METHOD = Object.class.getMethod("equals",
                                                   new Class[] { Object.class });
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    protected static final String ATTR_METHOD = "method";

    protected final StatsFactory factory;
    protected final StatsKey key;
    protected final Object target;

    protected StatsProxy(final StatsFactory factory,
                         final StatsKey key,
                         final Object target) {
        if (factory == null) {
            this.factory = Stats.getFactory(StatsConstants.DEFAULT_NAMESPACE);
        } else {
            this.factory = factory;
        }

        assertNotNull(key, "key");
        assertNotNull(target, "target");

        this.key = key;
        this.target = target;
    }

    /**
     * The type which receives the result of this method call must be an interface.
     *
     * @param <T>
     * @param key
     * @param target
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T wrap(final StatsFactory factory,
                             final StatsKey key,
                             final T target) {
        Class<? super T>[] ifaces = (Class<? super T>[])target.getClass()
                                                              .getInterfaces();
        return wrap(factory, key, target, ifaces);
    }

    public static <T,U extends T> T wrap(final StatsFactory factory,
                                         final StatsKey key,
                                         final U target,
                                         final Class<T> iface) {
        return wrap(factory, key, target, new Class[] { iface });
    }

    @SuppressWarnings("unchecked")
    public static <T,U extends T> T wrap(final StatsFactory factory,
                                         final StatsKey key,
                                         final U target,
                                         final Class<?>[] ifaces) {
        ClassLoader classLoader = Thread.currentThread()
                                        .getContextClassLoader();

        T proxy = (T) Proxy.newProxyInstance(classLoader,
                                             ifaces,
                                             new StatsProxy(factory, key, target));
        return proxy;
    }

    @SuppressWarnings("unchecked")
    public static <T> T unwrap(final T proxy) {
        if (!Proxy.isProxyClass(proxy.getClass())) {
            return proxy;
        }

        InvocationHandler ih = Proxy.getInvocationHandler(proxy);
        if (!(ih instanceof StatsProxy)) {
            return proxy;
        }

        StatsProxy wrapper = (StatsProxy)ih;
        return (T) wrapper.target;
    }

    public static boolean isProxy(final Object object) {
        if (!Proxy.isProxyClass(object.getClass())) {
            return false;
        }

        return Proxy.getInvocationHandler(object) instanceof StatsProxy;
    }

    protected static String getMethodString(final Method method) {

        String methodName = method.getName();
        Class<?>[] params = method.getParameterTypes();

        if (params.length == 0) {
            return methodName;
        }

        StringBuilder buf = new StringBuilder(methodName.length() + (params.length * 16));

        for (Class<?> param : params) {
            buf.append('_');
            buf.append(param.getClass()
                            .getSimpleName());
        }

        return buf.toString();
    }

    @Override
    public Object invoke(final Object proxy,
                         final Method method,
                         final Object[] args) throws Throwable {
        // Handle equals specially
        if (method.equals(EQUALS_METHOD)) {
            return target.equals(unwrap(args[0]));
        }

        StatsKey methodKey = key.buildCopy()
                                .withAttribute(ATTR_METHOD, getMethodString(method))
                                .newKey();

        try {
            final SpanTracker tracker = factory.track(methodKey);
            try {
                return method.invoke(target, args);

            } finally {
                tracker.commit();
            }

        } catch (Throwable t) {
            Throwable cause;
            if (t instanceof InvocationTargetException) {
                cause = t.getCause();
            } else {
                cause = t;
            }

            factory.failure(cause, methodKey);

            throw cause;
        }
    }
}
