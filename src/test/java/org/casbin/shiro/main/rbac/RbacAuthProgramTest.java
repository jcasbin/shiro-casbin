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

package org.casbin.shiro.main.rbac;

import org.apache.shiro.SecurityUtils;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.shiro.authorize.rbac.program.Operation;
import org.casbin.shiro.util.AuthenticatorUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RbacAuthProgramTest {

    private Enforcer e;

    @Before
    public void initEnforcer() {
        e = new Enforcer("example/rbac_model.conf", "example/rbac_policy.csv");
    }

    @Test
    public void testRbacAuthorization() {
        // alice login.
        AuthenticatorUtil.login("classpath:shiro.ini", "alice", "123");
        Assert.assertTrue(SecurityUtils.getSubject().isAuthenticated());
        // The current login user is alice
        Assert.assertTrue(Operation.hasAuthorization(e, "data1", "read"));
        Assert.assertFalse(Operation.hasAuthorization(e, "data1", "write"));
        Assert.assertTrue(Operation.hasAuthorization(e, "data2", "read"));
        Assert.assertTrue(Operation.hasAuthorization(e, "data2", "write"));
        // alice logout.
        AuthenticatorUtil.logout();

        // bob login.
        AuthenticatorUtil.login("classpath:shiro.ini", "bob", "123");
        Assert.assertTrue(SecurityUtils.getSubject().isAuthenticated());
        // The current login user is bob
        Assert.assertFalse(Operation.hasAuthorization(e, "data1", "read"));
        Assert.assertFalse(Operation.hasAuthorization(e, "data1", "write"));
        Assert.assertFalse(Operation.hasAuthorization(e, "data2", "read"));
        Assert.assertTrue(Operation.hasAuthorization(e, "data2", "write"));
        // bob logout.
        AuthenticatorUtil.logout();
    }
}
