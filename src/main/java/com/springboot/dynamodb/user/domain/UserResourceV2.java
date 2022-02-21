package com.springboot.dynamodb.user.domain;

import com.okta.sdk.resource.user.UserProfile;

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
	UserProfile userProfile;
}