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
package httl.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * IOUtils. (Tool, Static, ThreadSafe)
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public class IOUtils {

    public static byte[] readToBytes(InputStream in) throws IOException {
    	try {
	        UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream();
	        try {
		        byte[] buf = new byte[8192];
		        int len = 0;
		        while ((len = in.read(buf)) != -1) {
		        	out.write(buf, 0, len);
		        }
		        return out.toByteArray();
	        } finally {
	        	out.close();
	        }
    	} finally {
    		in.close();
    	}
    }

    public static String readToString(Reader reader) throws IOException {
    	try {
	        StringBuilder buffer = new StringBuilder();
	        char[] buf = new char[8192];
	        int len = 0;
	        while ((len = reader.read(buf)) != -1) {
	            buffer.append(buf, 0, len);
	        }
	        return buffer.toString();
    	} finally {
    		reader.close();
    	}
    }
    
    private IOUtils() {}

}
