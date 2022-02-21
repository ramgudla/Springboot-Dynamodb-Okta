package com.springboot.dynamodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.okta.sdk.client.Client;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.UserList;
import com.okta.sdk.resource.user.UserProfile;
import com.springboot.dynamodb.controller.builder.UserResourceBuilder;
import com.springboot.dynamodb.service.OktaService;
import com.springboot.dynamodb.user.domain.UserResource;
import com.springboot.dynamodb.user.domain.UserResourceV2;

@RestController
public class UserController {

    @Autowired
    public Client client;
    
    @Autowired
    OktaService service;
    
    @Autowired
    UserResourceBuilder userResourdeBuilder;
    
    @GetMapping("/users")
    public UserList getUsers() {
        return client.listUsers();
    }

    @GetMapping("/user")
    public UserList searchUserByEmail(@RequestParam String query) {
        return client.listUsers(query, null, null, null, null);
    }
    
    @GetMapping("/{domain}/{userId}")
    public ResponseEntity<UserResource> getDomainUser(@PathVariable String domain, @PathVariable String userId) {
    	return userResourdeBuilder.getUserResource(domain, userId);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUser(@PathVariable String userId) {
    	return userResourdeBuilder.getUserResource(userId, false, false);
    }
    
    @GetMapping("/v2/{userId}")
    public ResponseEntity<UserResourceV2> getUserV2(@PathVariable String userId) {
    	return userResourdeBuilder.getUserResourceV2(userId);
    }
    
    @GetMapping("/createUser")
    public User createUser() {
        char[] tempPassword = {'P','a','$','$','w','0','r','d'};
        User user = UserBuilder.instance()
            .setEmail("pqrs@gmail.com")
            .setFirstName("Asdf")
            .setLastName("G")
            .setPassword(tempPassword)
            .setActive(true)
            .putProfileProperty("userId", "ramkygudla@gmail.com")
            .buildAndCreate(client);
        UserProfile userProfile = user.getProfile();
        String customProp = userProfile.getString("test");
        System.out.println(customProp);
        return user;
    }
    
}

// https://www.baeldung.com/spring-security-okta


