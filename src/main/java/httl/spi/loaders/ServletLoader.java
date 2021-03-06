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
package httl.spi.loaders;

import httl.Engine;
import httl.Resource;
import httl.spi.Loader;
import httl.spi.Logger;
import httl.util.UrlUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ServletLoader. (SPI, Singleton, ThreadSafe)
 * 
 * @see httl.spi.engines.DefaultEngine#setLoader(Loader)
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public class ServletLoader extends AbstractLoader implements ServletContextListener {

    private static ServletContext SERVLET_CONTEXT;

    public static ServletContext getAndCheckServletContext() {
    	if (SERVLET_CONTEXT == null) {
			throw new IllegalStateException("servletContext == null. Please add config in your /WEB-INF/web.xml: " +
					"\n<listener>\n\t<listener-class>" + ServletLoader.class.getName() + "</listener-class>\n</listener>\n");
		}
    	return SERVLET_CONTEXT;
    }

    public static ServletContext getServletContext() {
        return SERVLET_CONTEXT;
    }

    public static void setServletContext(ServletContext servletContext) {
        SERVLET_CONTEXT = servletContext;
    }

    public void contextInitialized(ServletContextEvent sce) {
    	SERVLET_CONTEXT = sce.getServletContext();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        SERVLET_CONTEXT = null;
    }
    
    public void init() {
    	Logger logger = getLogger();
    	Engine engine = getEngine();
    	if (logger != null && logger.isInfoEnabled()
    			&& engine != null && SERVLET_CONTEXT != null) {
    		String name = engine.getName();
    		try {
				if (name != null && name.startsWith("/")
						&& SERVLET_CONTEXT.getResource(name) != null) {
					logger.info("Load httl config form " + SERVLET_CONTEXT.getRealPath(name) + " in webapp.");
				}
			} catch (MalformedURLException e) {
			}
    	}
    }

    public List<String> doList(String directory, String[] suffixes) throws IOException {
        return UrlUtils.listUrl(getAndCheckServletContext().getResource(directory), suffixes);
    }

    protected Resource doLoad(String name, String encoding, String path) throws IOException {
		return new ServletResource(getEngine(), name, encoding, path, getAndCheckServletContext());
	}

	public boolean doExists(String name, String path) throws Exception {
		return SERVLET_CONTEXT != null && SERVLET_CONTEXT.getResource(path) != null;
	}

}
