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

package org.casbin.shiro.authorize.rbac.annotation;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.shiro.authorize.rbac.program.Operation;

import java.lang.annotation.Annotation;

/**
 * Operation class of the PreAuth
 *
 * @author shy
 * @since 2021/01/19
 */
public class PreAuthHandler extends AuthorizingAnnotationHandler {

    private final Enforcer e;

    /**
     * Constructs an <code>AuthorizingAnnotationHandler</code> who processes annotations of the
     * specified type.  Immediately calls <code>super(annotationClass)</code>.
     *
     * @param e the enforcer.
     */
    public PreAuthHandler(Enforcer e) {
        super(PreAuth.class);
        this.e = e;
    }

    /**
     * Ensures the calling Subject is authorized to execute based on the directive(s) found in the given
     * annotation.
     * <p/>
     * As this is an AnnotationMethodInterceptor, the implementations of this method typically inspect the annotation
     * and perform a corresponding authorization check based.
     *
     * @param a the <code>Annotation</code> to check for performing an authorization check.
     * @throws AuthorizationException if the class/instance/method is not allowed to proceed/execute.
     */
    @Override
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        if (a instanceof PreAuth) {
            PreAuth preAuth = (PreAuth) a;
            // use jcasbin to judge whether the current login user has the permission.
            boolean hasPermission = Operation.hasAuthorization(e, preAuth.obj(), preAuth.act());
            if (!hasPermission) {
                throw new AuthorizationException("No permission to access this interface");
            }
        }
    }
}
