package com.springboot.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

//lombok annotations
//annotation helps to generate toString(), equals(), hashcode(), getter(), setter()
@Data
//annotation helps to generate a no-argument constructor
@NoArgsConstructor
//annotation helps to generate a constructor with 1 parameter for each field in the class
@AllArgsConstructor
//annotation helps to implement the builder design pattern
//usage can be seen in DefaultBooksLoader.java -> create() method.
@Builder
//spring stereotype annotation
@Component
public class BookDto {

    String title;
    String author;
    String genre;
    String publisher;
    int quantity;
}
