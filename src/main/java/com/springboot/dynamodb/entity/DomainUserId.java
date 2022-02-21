package com.springboot.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

public class DomainUserId {
	
	public DomainUserId() {
	}
	
	public DomainUserId(String domain, String userId) {
		this.domain = domain;
		this.userId = userId;
	}
	private String domain;
    private String userId;
	
    @DynamoDBHashKey
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@DynamoDBRangeKey
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
