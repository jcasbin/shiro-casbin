// Copyright 2021 The casbin Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.casbin.shiro.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log tool class
 *
 * @author shy
 * @since 2021/01/08
 */
public class LogUtil {
    public static boolean enableLog = true;

    private static final Logger LOGGER = LoggerFactory.getLogger("org.casbin.shiro");


    /**
     * logPrint prints the log.
     *
     * @param v the log.
     */
    public static void logPrint(String v) {
        if (enableLog) {
            LOGGER.info(v);
        }
    }

    /**
     * logPrintf prints the log with the format as an error.
     *
     * @param format the format of the log.
     * @param v the log.
     */
    public static void logPrintfError(String format, Object... v) {
        if (enableLog) {
            LOGGER.error(format, v);
        }
    }
}
