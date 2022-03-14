package com.springboot.dynamodb.entity;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "identityDomain")
public class GSI_OktaUserMap {
	
	@Id
    private GSI_UserId id;
	
	private String oktaUserId;

	public GSI_OktaUserMap() {}
    
    public GSI_OktaUserMap(GSI_UserId domainUserId) {this.id = domainUserId;}
    
    @DynamoDBHashKey(attributeName = "userId")
	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "oktaUserMapIndex")
	public String getUserId() {
		return id != null ? id.getUserId() : null;
	}
	public void setUserId(String userId) {
		if (id == null) {
			id = new GSI_UserId();
		}
		id.setUserId(userId);
	}
    
	@DynamoDBRangeKey(attributeName = "domain")
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "oktaUserMapIndex")
	public String getDomain() {
		return id != null ? id.getDomain() : null;
	}
	public void setDomain(String domain) {
		if (id == null) {
			id = new GSI_UserId();
		}
		id.setDomain(domain);
	}
	
	@DynamoDBAttribute(attributeName = "oktaUserId")
	public String getOktaUserId() {
		return oktaUserId;
	}

	public void setOktaUserId(String oktaUserId) {
		this.oktaUserId = oktaUserId;
	}
	
}
