package com.springboot.dynamodb.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.springboot.dynamodb.entity.BookDto;
import com.springboot.dynamodb.service.BookService;

import lombok.extern.slf4j.Slf4j;

//lombok annotation for logger
@Slf4j
//spring stereotype annotation
@Component
public class DefaultBooksLoader implements CommandLineRunner {

    @Autowired
    BookService service;
    
    @Autowired
    Faker faker;

    @Override
    public void run(String... args) throws Exception {
        final long count = service.getBooksCount();
        if (count == 0) {
            log.info("Saving the default books into the db");
            for (int x = 0; x < 10; x++) {
                persist();
            }
        } else {
            log.info("{} books are already present in the db", count);
        }
    }

    private void persist() {
        final BookDto dto = create();
        service.save(dto);
    }

    private BookDto create() {
        return BookDto.builder()
                .title(faker.book().title())
                .author(faker.book().author())
                .genre(faker.book().genre())
                .publisher(faker.book().publisher())
                .quantity(faker.number().randomDigitNotZero())
                .build();
    }
}
