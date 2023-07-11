package ru.team.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.team.backend.entities.Book;
import ru.team.backend.repositories.BookRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public Iterable<Book> findAll(){
        return bookRepository.findAll();
    }

    public void  save(Book book){
        bookRepository.save(book);
    }

    public Optional<Book> findBookById(UUID id){
        return bookRepository.findById(id);
    }


    public Optional<Book> findByIdAndDeleteIsFalse(UUID uuid){
        return bookRepository.findByIdAndDeleteIsFalse(uuid);
    }

    public Optional<Book> update(UUID id, Book book){
        bookRepository.updateBookById(id, book.getAuthor(), book.getAmount()  ,book.getDescription(), book.getTitle());
        return findBookById(id);
    }

    public void delete(UUID id){
        bookRepository.delete(id,true);
    }



}
