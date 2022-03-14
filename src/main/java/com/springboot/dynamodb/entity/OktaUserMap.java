package com.springboot.dynamodb.entity;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "oktaUserMap")
public class OktaUserMap {
	
	@Id
    private UserId id;
	
	private String oktaUserId;

	public OktaUserMap() {}
    
    public OktaUserMap(UserId domainUserId) {this.id = domainUserId;}
    
    @DynamoDBHashKey(attributeName = "userId")
	public String getUserId() {
		return id != null ? id.getUserId() : null;
	}
	public void setUserId(String userId) {
		if (id == null) {
			id = new UserId();
		}
		id.setUserId(userId);
	}
    
	@DynamoDBRangeKey(attributeName = "domain")
	public String getDomain() {
		return id != null ? id.getDomain() : null;
	}
	public void setDomain(String domain) {
		if (id == null) {
			id = new UserId();
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
