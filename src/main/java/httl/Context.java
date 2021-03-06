/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package httl;

import httl.util.WrappedMap;

import java.util.Map;

/**
 * Context. (API, ThreadLocal, ThreadSafe)
 * 
 * @see httl.Template#evaluate(Map)
 * @see httl.Template#render(Map, java.io.Writer)
 * @see httl.Template#render(Map, java.io.OutputStream)
 * @see httl.spi.parsers.template.WriterTemplate#render(Map, java.io.Writer)
 * @see httl.spi.parsers.template.OutputStreamTemplate#render(Map, java.io.OutputStream)
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public final class Context {

    // The thread local holder.
    private static final ThreadLocal<Context> LOCAL = new ThreadLocal<Context>();

    // The parent context.
    private final Context parent;

    // The current template.
    private final Template template;

    // The current parameters.
    private final Map<String, Object> parameters;

    // The current output.
    private final Object output;

    // The current context parameters.
    private Map<String, Object> contextParameters;

    private Context(Context parent, Template template, Map<String, Object> parameters, Object output) {
        this.parent = parent;
        this.template = template;
        this.parameters = parameters;
        this.output = output;
    }

    /**
     * Get the parent context.
     * 
     * @see #getContext()
     * @return parent context
     */
    public Context getParent() {
        return parent;
    }

    /**
     * Get the current template.
     * 
     * @see #getContext()
     * @return current template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * Get the current parameters.
     * 
     * @see #getContext()
     * @return current parameters
     */
    public Map<String, Object> getParameters() {
        if (contextParameters == null) { // safely in thread local
            contextParameters = new WrappedMap<String, Object>(parameters);
        }
        return contextParameters;
    }

    /**
     * Get the current output.
     * 
     * @see #getContext()
     * @return current output
     */
    public Object getOutput() {
        return output;
    }

    /**
     * Get the current context from thread local.
     * 
     * @return current context
     */
    public static Context getContext() {
        Context context = LOCAL.get();
        if (context == null) {
            context = new Context(null, null, null, null);
            LOCAL.set(context);
        }
        return context;
    }

    /**
     * Push the current context to thread local.
     * 
     * @param template - current template
     * @param parameters - current parameters
     */
    public static Context pushContext(Template template, Map<String, Object> parameters, Object output) {
        Context context = new Context(LOCAL.get(), template, parameters, output);
        LOCAL.set(context);
        return context;
    }

    /**
     * Pop the current context from thread local, and restore parent context to thread local.
     */
    public static void popContext() {
        Context context = LOCAL.get();
        if (context != null) {
            Context parent = context.getParent();
            if (parent != null) {
                LOCAL.set(parent);
            } else {
                LOCAL.remove();
            }
        }
    }

    /**
     * Remove the current context from thread local.
     */
    public static void removeContext() {
        LOCAL.remove();
    }

}
