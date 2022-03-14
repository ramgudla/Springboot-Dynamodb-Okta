package com.springboot.dynamodb.service;

import com.okta.sdk.client.Client;
import com.okta.sdk.resource.user.User;
import com.springboot.dynamodb.entity.Book;
import com.springboot.dynamodb.entity.BookDto;
import com.springboot.dynamodb.entity.UserId;
import com.springboot.dynamodb.entity.GSI_UserId;
import com.springboot.dynamodb.exception.EntityNotFound;
import com.springboot.dynamodb.repo.OktaUserMapRepository;
import com.springboot.dynamodb.repo.GSI_OktaUserMapRepository;
import com.springboot.dynamodb.repo.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookService {

    @Autowired
    BookRepository repository;
    
    @Autowired
    OktaUserMapRepository domainUserRepository;
    
    @Autowired
    GSI_OktaUserMapRepository identityDomainUserRepository;
    
    @Autowired
    public Client client;

    // get total books count
    public long getBooksCount() {
        return repository.count();
    }

    // save new book
    public void save(final BookDto dto) {
        final Book b = createBookBuilder(dto).build();
        repository.save(b);
    }

    //get all books
    public List<Book> getBooks() {
        final Iterable<Book> allBooks = repository.findAll();
        return StreamSupport.stream(allBooks.spliterator(), false).collect(Collectors.toList());
    }

    //get book by id
    public Book getBookById(final String id) throws EntityNotFound {
        return repository.findById(id).orElseThrow(EntityNotFound::new);
    }

    //update book by id
    public void update(final String id, final BookDto dto) throws EntityNotFound {
        getBookById(id);
        final Book bookToBeUpdated = createBookBuilder(dto).id(id).build();
        repository.save(bookToBeUpdated);
    }

    //delete book by id
    public void delete(final String id) throws EntityNotFound {
        getBookById(id);
        repository.deleteById(id);
    }

    //get books count by genre
    public long getCountByGenre(final String genre) {
        return repository.countByGenre(genre);
    }

    //get all books by genre
    public List<Book> getBooksByGenre(final String genre) {
        return repository.findAllByGenre(genre);
    }
    
    public String getOktaUserId(String domain, String userId) {
    	UserId domainUserId = new UserId(domain, userId);
    	String oktaUserId = domainUserRepository.findById(domainUserId).get().getOktaUserId();
    	return oktaUserId;
    }
    
    public User getOktaUser(String domain, String userId) {
    	String oktaUserId = getOktaUserIdGSI(domain, userId);
    	return getOktaUser(oktaUserId);
    }
    
    public User getOktaUser(String userId) {
    	return client.getUser(userId);
    }
    
    private String getOktaUserIdGSI(String domain, String userId) {
    	GSI_UserId identityDomainUserId = new GSI_UserId(domain, userId);
    	String oktaUserId = identityDomainUserRepository.findById(identityDomainUserId).get().getOktaUserId();
    	return oktaUserId;
    }

    //helper method
    private Book.BookBuilder createBookBuilder(final BookDto dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .genre(dto.getGenre())
                .publisher(dto.getPublisher())
                .quantity(dto.getQuantity());
    }
}
