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

package org.casbin.shiro.authorize.rbac.annotation.auth;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.casbin.shiro.util.LogUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.casbin.shiro.factory.EnforcerFactory.getEnforcer;
import static org.casbin.shiro.factory.EnforcerFactory.getUserMethodName;


/**
 * Operation class of the EnforcerAuth.
 *
 * @author shy
 * @since 2021/01/19
 */
public class EnforcerAuthHandler extends AuthorizingAnnotationHandler {

    /**
     * Constructs an <code>AuthorizingAnnotationHandler</code> who processes annotations of the
     * specified type.  Immediately calls <code>super(annotationClass)</code>.
     *
     */
    public EnforcerAuthHandler() {
        super(EnforcerAuth.class);
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
        if (a instanceof EnforcerAuth) {
            WebDelegatingSubject subject = (WebDelegatingSubject) getSubject();
            if (!subject.isAuthenticated()) {
                throw new AuthorizationException("Please login");
            } else {
                // use jcasbin to judge whether the current login user has the permission.
                HttpServletRequest request = (HttpServletRequest) subject.getServletRequest();
                String path = request.getServletPath();
                String method = request.getMethod();
                boolean hasPermission = false;
                if (getUserMethodName() == null) {
                    hasPermission = getEnforcer().enforce(subject.getPrincipal().toString(), path, method);
                } else {
                    // using reflection to call the specified method.
                    Object principal = subject.getPrincipal();
                    Class<?> principalClass = principal.getClass();
                    Object principalCast = principalClass.cast(principal);
                    String userName;
                    try {
                        Method principalMethod = principalClass.getMethod(getUserMethodName());
                        userName = (String) principalMethod.invoke(principalCast);
                        hasPermission = getEnforcer().enforce(userName, path, method);
                    } catch (NoSuchMethodException e) {
                        LogUtil.logPrintfError("Incorrect userNameMethodName");
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                if (!hasPermission) {
                    throw new UnauthorizedException("No permission to access this interface");
                }
            }
        }
    }
}
