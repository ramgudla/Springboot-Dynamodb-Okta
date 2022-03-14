package com.springboot.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

public class UserId {
	
	private String userId;
	private String domain;
	
	public UserId() {
	}
	
	public UserId(String userId, String domain) {
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
