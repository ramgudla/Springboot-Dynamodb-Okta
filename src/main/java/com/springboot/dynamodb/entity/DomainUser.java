package com.springboot.dynamodb.entity;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "identityDomain")
public class DomainUser {
	
	@Id
    private DomainUserId domainUserId;
	
	private String oktaUserId;

	public DomainUser() {}
    
    public DomainUser(DomainUserId domainUserId) {this.domainUserId = domainUserId;}
    
	@DynamoDBHashKey(attributeName = "domain")
	public String getDomain() {
		return domainUserId != null ? domainUserId.getDomain() : null;
	}
	public void setDomain(String domain) {
		if (domainUserId == null) {
			domainUserId = new DomainUserId();
		}
		domainUserId.setDomain(domain);
	}
	
	@DynamoDBRangeKey(attributeName = "userId")
	public String getUserId() {
		return domainUserId != null ? domainUserId.getUserId() : null;
	}
	public void setUserId(String userId) {
		if (domainUserId == null) {
			domainUserId = new DomainUserId();
		}
		domainUserId.setUserId(userId);
	}
	
	@DynamoDBAttribute(attributeName = "oktaUserId")
	public String getOktaUserId() {
		return oktaUserId;
	}

	public void setOktaUserId(String oktaUserId) {
		this.oktaUserId = oktaUserId;
	}
	
}
