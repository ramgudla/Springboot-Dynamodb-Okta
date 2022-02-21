package com.springboot.dynamodb.repo;

import com.springboot.dynamodb.entity.Book;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// https://examples.javacodegeeks.com/spring-boot-crud-with-aws-dynamodb/
// https://reflectoring.io/spring-dynamodb/

// https://github.com/okta/okta-sdk-java#configuration-reference

// https://medium.com/@leohoc/dynamodb-and-spring-data-a81c546a1305
//https://github.com/derjust/spring-data-dynamodb/issues/279

// https://medium.com/@jun711.g/aws-dynamodb-global-and-local-secondary-indexes-comparison-80f4c587b1d7

@EnableScan
//spring annotation
@Repository
public interface BookRepository extends CrudRepository<Book, String> {

    @EnableScanCount
    long countByGenre(String genre);

    List<Book> findAllByGenre(String genre);
}
