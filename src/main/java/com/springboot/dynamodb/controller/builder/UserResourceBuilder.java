package com.springboot.dynamodb.controller.builder;

import org.springframework.http.ResponseEntity;

import com.springboot.dynamodb.user.domain.UserResource;
import com.springboot.dynamodb.user.domain.UserResourceV2;

public interface UserResourceBuilder {
	ResponseEntity<UserResource> getUserResource(String domain, String userId, boolean fetchProfile, boolean fetchEmail);
	ResponseEntity<UserResource> getUserResource(String userId, boolean fetchProfile, boolean fetchEmail);
	ResponseEntity<UserResourceV2> getUserResourceV2(String userId);
}
