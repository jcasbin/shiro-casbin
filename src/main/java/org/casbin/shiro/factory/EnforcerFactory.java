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
import org.casbin.shiro.config.EnforcerConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    private EnforcerConfigProperties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        jdbcAdapter = new JDBCAdapter(driver, url, username, password);
        enforcer = new Enforcer(properties.getModelPath(), properties.getPolicyPath());
        jdbcAdapter.savePolicy(enforcer.getModel());
    }

    public static Enforcer getEnforcer() {
        return enforcer;
    }

    public static JDBCAdapter getJdbcAdapter() {
        return jdbcAdapter;
    }
}
