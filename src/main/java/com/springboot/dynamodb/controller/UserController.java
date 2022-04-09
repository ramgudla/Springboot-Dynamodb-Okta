package com.springboot.dynamodb.controller;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.sdk.client.Client;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.UserList;
import com.okta.sdk.resource.user.UserProfile;
import com.springboot.dynamodb.controller.builder.UserResourceBuilder;
import com.springboot.dynamodb.entity.OktaUserMap;
import com.springboot.dynamodb.entity.UserAttributes;
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
    
    @Autowired
	private ObjectMapper objectMapper;
    
    @GetMapping("/users")
    public UserList getUsers() {
        return client.listUsers();
    }

    @GetMapping("/user")
    public UserList searchUserByEmail(@RequestParam String query) {
        return client.listUsers(query, null, null, null, null);
    }
    
    @GetMapping("/user/{userId}")
    public User getOktaUser(@PathVariable String userId) {
        return client.getUser(userId);
    }
    
    @GetMapping("/createUser")
    public User createUser() {
    	UserAttributes updatedUserAttributes = UserAttributes.builder()
        		.closed(false)
        		.locked(true)
        		.domain("d1")
        		.region("region1")
        		.password("pw1")
        		.passwordMigratedAt("date1")
        		.build();
    	
    	String attrsString = objToJson(updatedUserAttributes);
    	
        char[] tempPassword = {'P','a','$','$','w','0','r','d'};
        User user = UserBuilder.instance()
            .setEmail("ram@ramgudla.in")
            .setFirstName("Test")
            .setLastName("Runner")
            .setPassword(tempPassword)
            .setActive(true)
            // A CustomProperty named "my_custom_prop" should have been created in Users->Profile
            .putProfileProperty("my_custom_prop", attrsString)
            .buildAndCreate(client);
        
        UserProfile userProfile = user.getProfile();
        String customProp = userProfile.getString("my_custom_prop");
        System.out.println(customProp);
        return user;
    }
    
    private String objToJson(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "JSON_CONVERSION_FAILED";
        }
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
    	String oktaUserId = "00u4i0rq0k9PXawaK5d7";
    	OktaUserMap user = new OktaUserMap(id);
    	user.setOktaUserId(oktaUserId);
    	oktaUserMapRepository.save(user);
    }
    
    // https://developer.okta.com/blog/2018/01/23/replace-local-storage-with-okta-profile-attributes
    @PostMapping(path = "/{userId}")
    public User updateUser(@RequestBody com.springboot.dynamodb.user.domain.User user, @PathVariable String userId,  boolean isAdmin) {
    	User oktaUser = service.getOktaUser(userId); //client.getUser(userId);
        UserProfile userProfile = oktaUser.getProfile();
        UserAttributes userAttrsFromOkta = null;
        String attributesFromOkta = (String)userProfile.get("my_custom_prop");
        if (attributesFromOkta != null) {
            try {
            	userAttrsFromOkta = objectMapper.readValue(attributesFromOkta, new TypeReference<UserAttributes>() {});
            } catch (Exception io) {
                io.printStackTrace();
            }
        }
        
        UserAttributes.UserAttributesBuilder userAttributesBuilder = UserAttributes.builder();
        
        if (!isAdmin) {
        	if (userAttrsFromOkta.isLocked() || userAttrsFromOkta.isClosed()) {
        		throw new RuntimeException("Only admins can update locked/closed accounts");
        	}
        }
        
        if (user.getStatus() != null) {
            //set status = user.getStatus().name()
        	userAttributesBuilder.status(user.getStatus());
        }
        
        if (user.getPassword() != null || user.getLegacyPassword() != null) {
        	  if (user.getPasswordMigratedAt() != null) {
        		  userAttributesBuilder.passwordMigratedAt(user.getPasswordMigratedAt());
        	    //set passwordMigratedAt = user.getPasswordMigratedAt();
        	  }
        	  else {
        		  userAttributesBuilder.passwordMigratedAt(Instant.now().toString());
        	    //set userChangedPwAt = user.getUserChangedPasswordAt();
        	  }
        	  if (user.getStatus() == null) {
        	      //set status = AccountStatus.ENABLED.name();
        		  userAttributesBuilder.status("enabled");
        	  }
        	}
        
        if (user.getPassword() != null) {
        	userAttributesBuilder.password(user.getPassword());
        }
        
        if (user.getLegacyPassword() != null) {
        	userAttributesBuilder.legacyPassword(user.getLegacyPassword());
        }
        
        UserAttributes userAttributes = userAttributesBuilder.build();

        String attrsString = objToJson(userAttributes);
        		
        userProfile.put("my_custom_prop", attrsString);
        oktaUser.update();

        return oktaUser;
    }
    
}

// String oktaUserId = "00u4i0rq0k9PXawaK5d7";
// https://www.baeldung.com/spring-security-okta


