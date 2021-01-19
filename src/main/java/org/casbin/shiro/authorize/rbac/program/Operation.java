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

package org.casbin.shiro.authorize.rbac.program;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.shiro.util.LogUtil;


/**
 * The operations related to authority authentication
 *
 * @author shy
 * @since 2021/01/18
 */
public class Operation {

    /**
     * hasAuthorization determines whether the current login user has a permission.
     *
     * @param e the enforcer;
     * @param obj the object (usually means resources).
     * @param act the action (usually means the operation that the user performs on the resource).
     * @return whether the current login user has the permission.
     */
    public static boolean hasAuthorization(Enforcer e, String obj, String act) {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            LogUtil.logPrintfError("Please login");
            return false;
        }
        Subject subject = SecurityUtils.getSubject();
        return e.enforce(subject.getPrincipal().toString(), obj, act);
    }
}
