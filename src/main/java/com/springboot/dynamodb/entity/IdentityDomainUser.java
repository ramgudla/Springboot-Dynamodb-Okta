package com.springboot.dynamodb.entity;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "identityDomain")
public class IdentityDomainUser {
	
	@Id
    private IdentityDomainUserId identityDomainUserId;
	
	private String oktaUserId;

	public IdentityDomainUser() {}
    
    public IdentityDomainUser(IdentityDomainUserId domainUserId) {this.identityDomainUserId = domainUserId;}
    
	@DynamoDBHashKey(attributeName = "domain")
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "identityDomainIndex")
	public String getDomain() {
		return identityDomainUserId != null ? identityDomainUserId.getDomain() : null;
	}
	public void setDomain(String domain) {
		if (identityDomainUserId == null) {
			identityDomainUserId = new IdentityDomainUserId();
		}
		identityDomainUserId.setDomain(domain);
	}
	
	@DynamoDBRangeKey(attributeName = "userId")
	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "identityDomainIndex")
	public String getUserId() {
		return identityDomainUserId != null ? identityDomainUserId.getUserId() : null;
	}
	public void setUserId(String userId) {
		if (identityDomainUserId == null) {
			identityDomainUserId = new IdentityDomainUserId();
		}
		identityDomainUserId.setUserId(userId);
	}
	
	@DynamoDBAttribute(attributeName = "oktaUserId")
	public String getOktaUserId() {
		return oktaUserId;
	}

	public void setOktaUserId(String oktaUserId) {
		this.oktaUserId = oktaUserId;
	}
	
}
