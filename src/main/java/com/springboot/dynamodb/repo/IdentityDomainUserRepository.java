package com.springboot.dynamodb.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springboot.dynamodb.entity.IdentityDomainUser;
import com.springboot.dynamodb.entity.IdentityDomainUserId;

@Repository
public interface IdentityDomainUserRepository extends CrudRepository<IdentityDomainUser, IdentityDomainUserId> {
    
}
