package com.springboot.dynamodb.controller.builder.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

import com.okta.sdk.resource.user.User;
import com.springboot.dynamodb.controller.builder.UserResourceBuilder;
import com.springboot.dynamodb.entity.GSI_UserId;
import com.springboot.dynamodb.repo.GSI_OktaUserMapRepository;
import com.springboot.dynamodb.service.OktaService;
import com.springboot.dynamodb.user.domain.AccountLink;
import com.springboot.dynamodb.user.domain.Profile;
import com.springboot.dynamodb.user.domain.SocialMediaLink;
import com.springboot.dynamodb.user.domain.UserResource;
import com.springboot.dynamodb.user.domain.UserResourceV2;

//@Component
public class UserResourceBuilderImpl implements UserResourceBuilder {
	
	@Autowired
	OktaService oktaService;
	
	@Autowired
    GSI_OktaUserMapRepository oktaUserMapRepository;
	
	private BodyBuilder bodyBuilder = ResponseEntity.ok();
	
	@Override
	public ResponseEntity<UserResource> getUserResource(String userId, String domain, boolean fetchProfile, boolean fetchEmail) {
		GSI_UserId identityDomainUserId = new GSI_UserId(userId, domain);
    	String mappedOktaUserId = oktaUserMapRepository.findById(identityDomainUserId).get().getOktaUserId();
		User user = oktaService.getOktaUser(mappedOktaUserId);
		com.springboot.dynamodb.user.domain.User identityUser = oktaService.extractIdentityUser(user);
		UserResource userResource = new UserResource(identityUser);
		if (fetchProfile) {
        	Profile profile = oktaService.extractProfile(user).orElse(new Profile());
        	userResource.setUserProfile(profile);
        }
        if (fetchEmail) {
        	List<String> emails = oktaService.extractEmails(user);
        	userResource.setEmails(emails);
        }
        List<AccountLink> accountLinks = oktaService.extractAccountLinks(user);
        userResource.setAccountLinks(accountLinks);
        List<SocialMediaLink> socialMediaLinks = oktaService.extractSocialMediaLinks(user);
        userResource.setSocialMediaLinks(socialMediaLinks);
        return bodyBuilder.body(userResource);
    }
	
	@Override
	public ResponseEntity<UserResource> getUserResource(String userId, boolean fetchProfile, boolean fetchEmail) {
		User user = oktaService.getOktaUser(userId);
		com.springboot.dynamodb.user.domain.User identityUser = oktaService.extractIdentityUser(user);
		UserResource userResource = new UserResource(identityUser);
        if (fetchProfile) {
        	Profile profile = oktaService.extractProfile(user).orElse(new Profile());
        	userResource.setUserProfile(profile);
        }
        if (fetchEmail) {
        	List<String> emails = oktaService.extractEmails(user);
        	userResource.setEmails(emails);
        }
        List<AccountLink> accountLinks = oktaService.extractAccountLinks(user);
        userResource.setAccountLinks(accountLinks);
        List<SocialMediaLink> socialMediaLinks = oktaService.extractSocialMediaLinks(user);
        userResource.setSocialMediaLinks(socialMediaLinks);
        return bodyBuilder.body(userResource);
    }

	@Override
    public ResponseEntity<UserResourceV2> getUserResourceV2(String userId) {
		User user = oktaService.getOktaUser(userId);
		com.springboot.dynamodb.user.domain.User identityUser = oktaService.extractIdentityUser(user);
        UserResourceV2 userResource = new UserResourceV2(identityUser);
        Profile profile = oktaService.extractProfile(user).orElse(new Profile());
    	userResource.setUserProfile(profile);
    	List<String> emails = oktaService.extractEmails(user);
    	userResource.setEmails(emails);
    	List<AccountLink> accountLinks = oktaService.extractAccountLinks(user);
    	userResource.setAccountLinks(accountLinks);
        List<SocialMediaLink> socialMediaLinks = oktaService.extractSocialMediaLinks(user);
        userResource.setSocialMediaLinks(socialMediaLinks);
        return bodyBuilder.body(userResource);
    }

}
