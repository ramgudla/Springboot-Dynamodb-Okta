package com.springboot.dynamodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;

import com.okta.sdk.resource.user.User;
import com.springboot.dynamodb.controller.builder.impl.UserResourceBuilderImpl;
import com.springboot.dynamodb.entity.IdentityDomainUser;
import com.springboot.dynamodb.entity.IdentityDomainUserId;
import com.springboot.dynamodb.repo.IdentityDomainUserRepository;
import com.springboot.dynamodb.service.OktaService;
import com.springboot.dynamodb.user.domain.Profile;

@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest
public class TestUserResourceBuilder {
	
	@InjectMocks
	UserResourceBuilderImpl userResourceBuilder;
	
	@Mock
	OktaService oktaService;
	
	@Mock
    IdentityDomainUserRepository identityDomainUserRepository;
	
	@Mock
	User user;
	
	@Mock
	com.springboot.dynamodb.user.domain.User identityUser;
	
	@Mock
	EntityLinks entityLinks;
	
	@Mock
	Link link;
	
	@Mock
	LinkBuilder linkBuilder;
	
	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void getUserResourceByUserIdWithAndWithoutProfileAndEmails() {
		when(oktaService.getOktaUser("id1")).thenReturn(user);
		when(oktaService.extractIdentityUser(user)).thenReturn(identityUser);
		when(identityUser.getUserId()).thenReturn("id1");
		
		// for links
		when(entityLinks.linkToSingleResource(com.springboot.dynamodb.user.domain.User.class, identityUser.getUserId())).thenReturn(link);
		when(link.withSelfRel()).thenReturn(link);
		when(link.withRel("user")).thenReturn(link);
		when(entityLinks.linkFor(Profile.class)).thenReturn(linkBuilder);
		when(linkBuilder.slash(identityUser.getUserId())).thenReturn(linkBuilder);
		when(linkBuilder.withRel("profile")).thenReturn(link);
		when(linkBuilder.slash("emails")).thenReturn(linkBuilder);
		when(linkBuilder.slash("activities")).thenReturn(linkBuilder);
		when(linkBuilder.withRel("emails")).thenReturn(link);
		when(linkBuilder.withRel("activities")).thenReturn(link);
		
		assertNull(userResourceBuilder.getUserResource("id1", false, false).getBody().getEmails());
		assertNull(userResourceBuilder.getUserResource("id1", false, false).getBody().getUserProfile());
		
		assertNotNull(userResourceBuilder.getUserResource("id1", true, false).getBody().getUserProfile());
		assertNotNull(userResourceBuilder.getUserResource("id1", false, true).getBody().getEmails());
		
		assertEquals(200, userResourceBuilder.getUserResource("id1", false, false).getStatusCodeValue());
	}
	
	@Test
	public void getUserResourceByDomainUser() {
		IdentityDomainUserId identityDomainUserId = new IdentityDomainUserId("d1", "u1");
		IdentityDomainUser identityDomainUser = new IdentityDomainUser(identityDomainUserId);
		identityDomainUser.setOktaUserId("id1");
		
    	when(identityDomainUserRepository.findById(identityDomainUserId)).thenReturn(Optional.of(identityDomainUser));
		when(oktaService.getOktaUser("id1")).thenReturn(user);
		when(oktaService.extractIdentityUser(user)).thenReturn(identityUser);
		when(identityUser.getUserId()).thenReturn("id1");
		
		// for links
		when(entityLinks.linkToSingleResource(com.springboot.dynamodb.user.domain.User.class, identityUser.getUserId())).thenReturn(link);
		when(link.withSelfRel()).thenReturn(link);
		when(link.withRel("user")).thenReturn(link);
		when(entityLinks.linkFor(Profile.class)).thenReturn(linkBuilder);
		when(linkBuilder.slash(identityUser.getUserId())).thenReturn(linkBuilder);
		when(linkBuilder.withRel("profile")).thenReturn(link);
		when(linkBuilder.slash("emails")).thenReturn(linkBuilder);
		when(linkBuilder.slash("activities")).thenReturn(linkBuilder);
		when(linkBuilder.withRel("emails")).thenReturn(link);
		when(linkBuilder.withRel("activities")).thenReturn(link);
		
		assertNull(userResourceBuilder.getUserResource("d1", "u1", false, false).getBody().getEmails());
		assertNull(userResourceBuilder.getUserResource("d1", "u1", false, false).getBody().getUserProfile());
		
		assertNotNull(userResourceBuilder.getUserResource("d1", "u1", true, false).getBody().getUserProfile());
		assertNotNull(userResourceBuilder.getUserResource("d1", "u1", false, true).getBody().getEmails());
		
		assertEquals(200, userResourceBuilder.getUserResource("d1", "u1", false, false).getStatusCodeValue());
	}
	
	@Test
	public void getUserResourceV2ByUserId() {
		when(oktaService.getOktaUser("id1")).thenReturn(user);
		when(oktaService.extractIdentityUser(user)).thenReturn(identityUser);
		when(identityUser.getUserId()).thenReturn("id1");
		
		// for links
		when(entityLinks.linkToSingleResource(com.springboot.dynamodb.user.domain.User.class, identityUser.getUserId())).thenReturn(link);
		when(link.withSelfRel()).thenReturn(link);
		when(link.withRel("user")).thenReturn(link);
		when(entityLinks.linkFor(Profile.class)).thenReturn(linkBuilder);
		when(linkBuilder.slash(identityUser.getUserId())).thenReturn(linkBuilder);
		when(linkBuilder.withRel("profile")).thenReturn(link);
		when(linkBuilder.slash("emails")).thenReturn(linkBuilder);
		when(linkBuilder.slash("activities")).thenReturn(linkBuilder);
		when(linkBuilder.withRel("emails")).thenReturn(link);
		when(linkBuilder.withRel("activities")).thenReturn(link);
		
		assertNotNull(userResourceBuilder.getUserResourceV2("id1").getBody().getUserProfile());
		assertNotNull(userResourceBuilder.getUserResourceV2("id1").getBody().getEmails());
		
		assertEquals(200, userResourceBuilder.getUserResourceV2("id1").getStatusCodeValue());
	}
	
}
