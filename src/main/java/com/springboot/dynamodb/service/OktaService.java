package com.springboot.dynamodb.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.okta.sdk.client.AuthorizationMode;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.User;

@Service
public class OktaService {

    @Autowired
    public Client client;

    public User getOktaUser(String userId) {
    	
    	// https://github.com/okta/okta-sdk-java
    	Client client2 = Clients.builder()
    		    .setOrgUrl("https://dev-83028594.okta.com")  // e.g. https://dev-123456.okta.com
    		    .setAuthorizationMode(AuthorizationMode.PRIVATE_KEY)
    		    .setClientId("0oa3xn5earYA2kV9O5d7")
    		    .setKid("DmFc8GGMKZ0uG5K9NVqMeyA4bk0gvZ00XnnXjPJO8o4") // key id (optional)
    		    .setScopes(new HashSet<>(Arrays.asList("okta.users.read", "okta.apps.read")))
    		    .setPrivateKey("extras/okta/okta_pk.pem")
    		    // (or) .setPrivateKey("full PEM payload")
    		    // (or) .setPrivateKey(Paths.get("/path/to/yourPrivateKey.pem"))
    		    // (or) .setPrivateKey(inputStream)
    		    // (or) .setPrivateKey(privateKey)
    		    .build();
    	
    	return client2.getUser(userId);
    }
    
}
