package com.springboot.dynamodb.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springboot.dynamodb.entity.GSI_OktaUserMap;
import com.springboot.dynamodb.entity.GSI_UserId;

@Repository
public interface GSI_OktaUserMapRepository extends CrudRepository<GSI_OktaUserMap, GSI_UserId> {
    
}
