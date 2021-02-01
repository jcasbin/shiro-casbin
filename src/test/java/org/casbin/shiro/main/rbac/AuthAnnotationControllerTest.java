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

import org.casbin.adapter.JDBCAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static org.casbin.shiro.factory.EnforcerFactory.getEnforcer;
import static org.casbin.shiro.factory.EnforcerFactory.getJdbcAdapter;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AuthAnnotationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpEntity<String> httpEntity;

    @Before
    public void before() {
        JDBCAdapter jdbcAdapter = getJdbcAdapter();

        jdbcAdapter.removePolicy("p", "p" , Arrays.asList("alice", "/data1", "GET"));
        jdbcAdapter.removePolicy("p", "p" , Arrays.asList("bob", "/data2", "POST"));

        jdbcAdapter.addPolicy("p", "p" , Arrays.asList("alice", "/data1", "GET"));
        jdbcAdapter.addPolicy("p", "p" , Arrays.asList("bob", "/data2", "POST"));

        getEnforcer().loadPolicy();
    }

    @Test
    public void testAuthAnnotationController() {
        ResponseEntity<String> responseEntity;
        // alice login
        login("/login?username=alice&password=123");
        // test api data1
        responseEntity = restTemplate.exchange("/data1", HttpMethod.GET, httpEntity, String.class);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        // test api data2
        responseEntity = restTemplate.exchange("/data2", HttpMethod.POST, httpEntity, String.class);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        // alice logout
        logout();

        // bob login
        login("/login?username=bob&password=123");
        // test api data1
        responseEntity = restTemplate.exchange("/data1", HttpMethod.GET, httpEntity, String.class);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        // test api data2
        responseEntity = restTemplate.exchange("/data2", HttpMethod.POST, httpEntity, String.class);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        // bob logout
        logout();
    }

    private void login(String url) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, new HashMap<>(), String.class);
        HttpHeaders headers = new HttpHeaders();
        // add cookie to keep state.
        String headerValue = Objects.requireNonNull(responseEntity.getHeaders().get("Set-Cookie")).toString().replace("[", "").replace("]", "");
        headers.set("Cookie", headerValue);
        httpEntity = new HttpEntity<>(headers);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    private void logout() {
        String result = restTemplate.postForObject("/logout", null, String.class, httpEntity);
        httpEntity = null;
        Assert.assertEquals(result, "success");
    }
}