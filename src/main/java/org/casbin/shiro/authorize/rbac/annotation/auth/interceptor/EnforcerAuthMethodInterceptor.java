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

package org.casbin.shiro.authorize.rbac.annotation.auth.interceptor;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.casbin.shiro.authorize.rbac.annotation.auth.EnforcerAuthHandler;

import java.lang.annotation.Annotation;

/**
 * Interception processing class of EnforcerAuth.
 *
 * @author shy
 * @since 2021/01/19
 */
public class EnforcerAuthMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {

    /**
     * Constructor that ensures the internal <code>handler</code> is set which will be used to perform the
     * authorization assertion checks when a supported annotation is encountered.
     */
    public EnforcerAuthMethodInterceptor() {
        super(new EnforcerAuthHandler());
    }

    /**
     * Constructor that ensures the internal <code>handler</code> is set which will be used to perform the
     * authorization assertion checks when a supported annotation is encountered with the AnnotationResolver.
     *
     * @param resolver An AOP-framework-independent way of determining if an Annotation exists on a Method.
     */
    public EnforcerAuthMethodInterceptor(AnnotationResolver resolver) {
        super(new EnforcerAuthHandler(), resolver);
    }

    /**
     * Ensures the calling Subject is authorized to execute the specified <code>MethodInvocation</code>.
     * <p/>
     * As this is an AnnotationMethodInterceptor, this implementation merely delegates to the internal
     * {@link AuthorizingAnnotationHandler AuthorizingAnnotationHandler} by first acquiring the annotation by
     * calling {@link #getAnnotation(MethodInvocation) getAnnotation(methodInvocation)} and then calls
     * {@link AuthorizingAnnotationHandler#assertAuthorized(Annotation) handler.assertAuthorized(annotation)}.
     *
     * @param mi the <code>MethodInvocation</code> to check to see if it is allowed to proceed/execute.
     * @throws AuthorizationException if the method invocation is not allowed to continue/execute.
     */
    @Override
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        try {
            ((EnforcerAuthHandler) this.getHandler()).assertAuthorized(getAnnotation(mi));
        } catch (AuthorizationException ae) {
            if (ae.getCause() == null) {
                ae.initCause(new AuthorizationException(mi.getMethod() + " method does not pass authentication."));
            }
            throw ae;
        }
    }
}
