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

package org.casbin.shiro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * shiro-casbin configuration properties.
 *
 * @author shy
 * @since 2021/02/17
 */
@ConfigurationProperties(prefix = "shiro-casbin")
public class EnforcerConfigProperties {
    private String modelPath;
    private String policyPath;
    private String userNameMethodName;

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getPolicyPath() {
        return policyPath;
    }

    public void setPolicyPath(String policyPath) {
        this.policyPath = policyPath;
    }

    public String getUserNameMethodName() {
        return userNameMethodName;
    }

    public void setUserNameMethodName(String userNameMethodName) {
        this.userNameMethodName = userNameMethodName;
    }
}
