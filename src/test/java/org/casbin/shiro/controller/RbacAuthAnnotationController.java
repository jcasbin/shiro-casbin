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

package org.casbin.shiro.controller;

import org.casbin.shiro.authorize.rbac.annotation.PreAuth;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RbacAuthAnnotationController {

    @PreAuth(obj = "data1", act = "read")
    public String testRbacAuthAnnotation1() {
        return "success";
    }

    @PreAuth(obj = "data1", act = "write")
    public String testRbacAuthAnnotation2() {
        return "success";
    }

    @PreAuth(obj = "data2", act = "read")
    public String testRbacAuthAnnotation3() {
        return "success";
    }


    @PreAuth(obj = "data2", act = "write")
    public String testRbacAuthAnnotation4() {
        return "success";
    }
}
