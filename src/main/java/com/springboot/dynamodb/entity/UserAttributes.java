package com.springboot.dynamodb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAttributes {
	private String status;
	private boolean locked;
	private boolean closed;
	private String passwordMigratedAt;
	private String userChangedPwAt;
	private String password;
	private String legacyPassword;
	private String salt;
	private String domain;
	private String region;
}
