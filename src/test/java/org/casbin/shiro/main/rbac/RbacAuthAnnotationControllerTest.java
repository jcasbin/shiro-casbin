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
import org.apache.shiro.authz.AuthorizationException;
import org.casbin.shiro.controller.RbacAuthAnnotationController;
import org.casbin.shiro.util.AuthenticatorUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RbacAuthAnnotationControllerTest {

    @Resource
    private RbacAuthAnnotationController controller;

    @Test
    public void testRbacAuthAnnotation() {
        // alice login.
        AuthenticatorUtil.login("classpath:shiro.ini", "alice", "123");
        Assert.assertTrue(SecurityUtils.getSubject().isAuthenticated());
        // The current login user is alice
        Assert.assertEquals("success", controller.testRbacAuthAnnotation1());
        try {
            controller.testRbacAuthAnnotation2();
        } catch (AuthorizationException a) {
            assert true;
        }
        Assert.assertEquals("success", controller.testRbacAuthAnnotation3());
        Assert.assertEquals("success", controller.testRbacAuthAnnotation4());
        // alice logout.
        AuthenticatorUtil.logout();

        // bob login.
        AuthenticatorUtil.login("classpath:shiro.ini", "bob", "123");
        Assert.assertTrue(SecurityUtils.getSubject().isAuthenticated());
        // The current login user is bob.
        try {
            controller.testRbacAuthAnnotation1();
        } catch (AuthorizationException a) {
            assert true;
        }
        try {
            controller.testRbacAuthAnnotation2();
        } catch (AuthorizationException a) {
            assert true;
        }
        try {
            controller.testRbacAuthAnnotation3();
        } catch (AuthorizationException a) {
            assert true;
        }
        Assert.assertEquals("success", controller.testRbacAuthAnnotation4());
        // bob logout.
        AuthenticatorUtil.logout();
    }
}