package com.springboot.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class GSI_UserId {

	private String userId;
	private String domain;
	
	public GSI_UserId() {
	}
	
	public GSI_UserId(String userId, String domain) {
		this.userId = userId;
		this.domain = domain;
	}
	
	@DynamoDBHashKey
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
    @DynamoDBRangeKey
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
}
