package com.springboot.dynamodb.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.okta.sdk.client.Client;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.UserList;
import com.okta.sdk.resource.user.UserProfile;
import com.springboot.dynamodb.controller.builder.UserResourceBuilder;
import com.springboot.dynamodb.entity.OktaUserMap;
import com.springboot.dynamodb.entity.UserId;
import com.springboot.dynamodb.repo.OktaUserMapRepository;
import com.springboot.dynamodb.service.OktaService;
import com.springboot.dynamodb.user.domain.UserResource;
import com.springboot.dynamodb.user.domain.UserResourceV2;

@RestController
public class UserController {

    public UserController(Optional<UserResourceBuilder> userResourceBuilder) {
		super();
		this.userResourceBuilder = userResourceBuilder.orElse(null);
	}

	@Autowired
    public Client client;
    
    @Autowired
    OktaService service;
    
    //@Autowired
    UserResourceBuilder userResourceBuilder;
    
    @Autowired
    OktaUserMapRepository oktaUserMapRepository;
    
    @GetMapping("/users")
    public UserList getUsers() {
        return client.listUsers();
    }

    @GetMapping("/user")
    public UserList searchUserByEmail(@RequestParam String query) {
        return client.listUsers(query, null, null, null, null);
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
    
    @GetMapping("/{userId}/{domain}")
    public ResponseEntity<UserResource> getDomainUser(@PathVariable String userId, @PathVariable String domain) {
    	if (null != userResourceBuilder) {
    		return userResourceBuilder.getUserResource(userId, domain, false, false);
    	}
    	return ResponseEntity.ok().body(new UserResource());
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUser(@PathVariable String userId) {
    	if (null != userResourceBuilder) {
    		return userResourceBuilder.getUserResource(userId, false, false);
    	}
    	return ResponseEntity.ok().body(new UserResource());
    }
    
    @GetMapping("/v2/{userId}")
    public ResponseEntity<UserResourceV2> getUserV2(@PathVariable String userId) {
    	if (null != userResourceBuilder) {
    		return userResourceBuilder.getUserResourceV2(userId);
    	}
    	return ResponseEntity.ok().body(new UserResourceV2());
    }
    
    @PostMapping("/{userId}/{domain}")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable String userId, @PathVariable String domain) {
    	UserId id = new UserId(userId, domain);
    	String oktaUserId = "00u3tjxsmwNBKiGE75d7";
    	OktaUserMap user = new OktaUserMap(id);
    	user.setOktaUserId(oktaUserId);
    	oktaUserMapRepository.save(user);
    }
    
}

// https://www.baeldung.com/spring-security-okta


