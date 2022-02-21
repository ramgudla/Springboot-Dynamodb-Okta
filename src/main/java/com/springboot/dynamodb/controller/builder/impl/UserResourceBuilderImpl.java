package com.springboot.dynamodb.controller.builder.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Component;

import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserProfile;
import com.springboot.dynamodb.controller.builder.UserResourceBuilder;
import com.springboot.dynamodb.entity.IdentityDomainUserId;
import com.springboot.dynamodb.repo.IdentityDomainUserRepository;
import com.springboot.dynamodb.service.OktaService;
import com.springboot.dynamodb.user.domain.UserResource;
import com.springboot.dynamodb.user.domain.UserResourceV2;

@Component
public class UserResourceBuilderImpl implements UserResourceBuilder {
	
	@Autowired
	OktaService oktaService;
	
	@Autowired
    IdentityDomainUserRepository identityDomainUserRepository;
	
	private BodyBuilder bodyBuilder = ResponseEntity.ok();
	
	@Override
	public ResponseEntity<UserResource> getUserResource(String domain, String userId) {
		IdentityDomainUserId identityDomainUserId = new IdentityDomainUserId(domain, userId);
    	String mappedOktaUserId = identityDomainUserRepository.findById(identityDomainUserId).get().getOktaUserId();
		User user = oktaService.getOktaUser(mappedOktaUserId);
		UserProfile userProfile = user.getProfile();
        UserResource userResource = new UserResource();
        userResource.setUserProfile(userProfile);
        return bodyBuilder.body(userResource);
    }
	
	@Override
	public ResponseEntity<UserResource> getUserResource(String userId, boolean fetchProfile, boolean fetchEmail) {
		User user = oktaService.getOktaUser(userId);
		UserProfile userProfile = user.getProfile();
        UserResource userResource = new UserResource();
        userResource.setUserProfile(userProfile);
        return bodyBuilder.body(userResource);
    }

	@Override
    public ResponseEntity<UserResourceV2> getUserResourceV2(String userId) {
		User user = oktaService.getOktaUser(userId);
		UserProfile userProfile = user.getProfile();
        UserResourceV2 userResource = new UserResourceV2();
        userResource.setUserProfile(userProfile);
        return bodyBuilder.body(userResource);
    }

}
