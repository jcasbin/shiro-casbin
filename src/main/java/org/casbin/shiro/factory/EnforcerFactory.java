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

package org.casbin.shiro.factory;

import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.persist.file_adapter.FileAdapter;
import org.casbin.shiro.config.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

/**
 * When the container is loaded, it is initialized to load the information in the configuration file.
 *
 * @author shy
 * @since 2021/01/27
 */
@Configuration
@EnableConfigurationProperties(EnforcerConfigProperties.class)
public class EnforcerFactory implements InitializingBean {

    private static Enforcer enforcer;
    private static JDBCAdapter jdbcAdapter;
    private static String userMethodName;

    @Autowired
    private EnforcerConfigProperties properties;

    @Autowired
    private DataSource dataSource;

    /**
     * Initialize the JDBCAdapter and Enforcer.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        jdbcAdapter = new JDBCAdapter(dataSource);
        String modelPath = getResourcePath(properties.getModelPath());
        String policyPath = getResourcePath(properties.getPolicyPath());
        FileAdapter fileAdapter = new FileAdapter(policyPath);
        enforcer = new Enforcer(modelPath, fileAdapter);
        jdbcAdapter.savePolicy(enforcer.getModel());
        jdbcAdapter.close();
        userMethodName = properties.getUserNameMethodName();
    }

    public static Enforcer getEnforcer() {
        return enforcer;
    }

    public static JDBCAdapter getJdbcAdapter() {
        return jdbcAdapter;
    }

    public static String getUserMethodName() {
        return userMethodName;
    }

    /**
     * Get the resource path which can used by enforcer.
     *
     * @param resourcePath the path in the configuration.
     * @return path;
     * @throws FileNotFoundException the resource path exists error.
     */
    private String getResourcePath(String resourcePath) throws FileNotFoundException {
        String path;
        if (ResourceUtils.isUrl(resourcePath)) {
            path = ResourceUtils.getURL(resourcePath).getPath();
        } else {
            path = resourcePath;
        }
        return path;
    }
}
