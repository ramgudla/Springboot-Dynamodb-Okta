package com.springboot.dynamodb.repo;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springboot.dynamodb.entity.DomainUser;
import com.springboot.dynamodb.entity.DomainUserId;


// https://examples.javacodegeeks.com/spring-boot-crud-with-aws-dynamodb/
// https://reflectoring.io/spring-dynamodb/
// http://johnhunsley.blogspot.com/2016/11/spring-data-jpa-with-hash-range-key.html

// https://medium.com/@leohoc/dynamodb-and-spring-data-a81c546a1305
// https://github.com/leohoc/dynamodb-springdata-example
	
// https://medium.com/@jun711.g/aws-dynamodb-global-and-local-secondary-indexes-comparison-80f4c587b1d7

@EnableScan
//spring annotation
@Repository
public interface DomainUserRepository extends CrudRepository<DomainUser, DomainUserId> {
    
}
