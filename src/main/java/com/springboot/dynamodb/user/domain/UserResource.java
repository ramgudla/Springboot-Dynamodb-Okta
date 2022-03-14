package com.springboot.dynamodb.user.domain;

import java.util.List;

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
public class UserResource {
	User user;
	Profile userProfile;
	List<String> emails;
	List<AccountLink> accountLinks;
    List<SocialMediaLink> socialMediaLinks;
    public UserResource(User identityUser) {
		this.user = identityUser;
	}
    
}
