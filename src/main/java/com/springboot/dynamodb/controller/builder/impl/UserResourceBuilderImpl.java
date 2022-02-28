package com.springboot.dynamodb.controller.builder.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

import com.okta.sdk.resource.user.User;
import com.springboot.dynamodb.controller.builder.UserResourceBuilder;
import com.springboot.dynamodb.entity.IdentityDomainUserId;
import com.springboot.dynamodb.repo.IdentityDomainUserRepository;
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
    IdentityDomainUserRepository identityDomainUserRepository;
	
	@Autowired
	EntityLinks entityLinks;
	
	private BodyBuilder bodyBuilder = ResponseEntity.ok();
	
	@Override
	public ResponseEntity<UserResource> getUserResource(String domain, String userId, boolean fetchProfile, boolean fetchEmail) {
		IdentityDomainUserId identityDomainUserId = new IdentityDomainUserId(domain, userId);
    	String mappedOktaUserId = identityDomainUserRepository.findById(identityDomainUserId).get().getOktaUserId();
		User user = oktaService.getOktaUser(mappedOktaUserId);
		com.springboot.dynamodb.user.domain.User identityUser = oktaService.extractIdentityUser(user);
		UserResource userResource = createUserResource(identityUser, UserResource.class);
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
		UserResource userResource = createUserResource(identityUser, UserResource.class);
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
        UserResourceV2 userResource = createUserResourceV2(identityUser);
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
	
	private UserResource createUserResourceV1(com.springboot.dynamodb.user.domain.User identityUser) {
        Link urUserlink = entityLinks.linkToSingleResource(com.springboot.dynamodb.user.domain.User.class, identityUser.getUserId());
        Link selfLink = urUserlink.withSelfRel();
        Link userLink = urUserlink.withRel("user");

        LinkBuilder urProfileLink = entityLinks.linkFor(Profile.class).slash(identityUser.getUserId());
        Link profileLink = urProfileLink.withRel("profile");
        Link emailLink = urProfileLink.slash("emails").withRel("emails");
        Link activitiesLink = urProfileLink.slash("activities").withRel("activities");

        return new UserResource(identityUser, selfLink, userLink, profileLink, emailLink, activitiesLink);
    }

    private UserResourceV2 createUserResourceV2(com.springboot.dynamodb.user.domain.User identityUser) {
        Link urUserlink = entityLinks.linkToSingleResource(com.springboot.dynamodb.user.domain.User.class, identityUser.getUserId());
        Link selfLink = urUserlink.withSelfRel();
        Link userLink = urUserlink.withRel("user");

        LinkBuilder urProfileLink = entityLinks.linkFor(Profile.class).slash(identityUser.getUserId());
        Link profileLink = urProfileLink.withRel("profile");
        Link emailLink = urProfileLink.slash("emails").withRel("emails");
        Link activitiesLink = urProfileLink.slash("activities").withRel("activities");

        return new UserResourceV2(identityUser, selfLink, userLink, profileLink, emailLink, activitiesLink);
    }
    
    private UserResource createUserResource(com.springboot.dynamodb.user.domain.User identityUser, Class<?> type) {
        Link urUserlink = entityLinks.linkToSingleResource(com.springboot.dynamodb.user.domain.User.class, identityUser.getUserId());
        Link selfLink = urUserlink.withSelfRel();
        Link userLink = urUserlink.withRel("user");

        LinkBuilder urProfileLink = entityLinks.linkFor(Profile.class).slash(identityUser.getUserId());
        Link profileLink = urProfileLink.withRel("profile");
        Link emailLink = urProfileLink.slash("emails").withRel("emails");
        Link activitiesLink = urProfileLink.slash("activities").withRel("activities");
        
        if (type.equals(UserResource.class)) {
        	return new UserResource(identityUser, selfLink, userLink, profileLink, emailLink, activitiesLink);
        } else if (type.equals(UserResourceV2.class)){
        	// return UserResourceV2
        	return new UserResource(identityUser, selfLink, userLink, profileLink, emailLink, activitiesLink);
        } else {
        	throw new IllegalArgumentException("User Resource type is not supported.");
        }
        
    }

}
