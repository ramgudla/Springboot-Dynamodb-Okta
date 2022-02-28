package com.springboot.dynamodb.user.domain;

import java.util.List;

import org.springframework.hateoas.Link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResourceV2 {
	Profile userProfile;
	List<String> emails;
	List<AccountLink> accountLinks;
    List<SocialMediaLink> socialMediaLinks;
    
    public UserResourceV2(User user, Link... link) {
    	
    }
}