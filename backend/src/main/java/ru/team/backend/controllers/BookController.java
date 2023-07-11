package ru.team.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.team.backend.archive.ArchiveService;
import ru.team.backend.archive.DeletedBooks;
import ru.team.backend.entities.Book;
import ru.team.backend.services.BookService;
import ru.team.backend.services.UserService;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/book")
public class BookController {

    @Autowired
    private final BookService bookService;

    @Autowired
    private final ArchiveService archiveService;

    @Autowired
    private UserService userService;

    public BookController(BookService bookService, ArchiveService archiveService) {
        this.bookService = bookService;
        this.archiveService = archiveService;
    }

    @GetMapping
    public Iterable<Book> showAll(){
        return bookService.findAll();
    }

    @PostMapping("/new")
    public Optional<Book> save(@RequestBody Book book){
        bookService.save(book);
        return Optional.ofNullable(book);
    }

    @GetMapping("/show/{bookId}")
    public Optional<Book> show(@PathVariable UUID bookId){
        return bookService.findBookById(bookId);
    }

    @PostMapping("/edit/{bookId}")
    public Optional<Book> update(@PathVariable UUID bookId, @RequestBody Book book){
        return bookService.update(bookId,book);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<?> delete(@PathVariable UUID bookId){
        try {

            Book book = bookService.findByIdAndDeleteIsFalse(bookId).get();
            bookService.delete(book.getId());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            DeletedBooks deletedBook = new DeletedBooks(userService.getUserByEmail(auth.getName()).getId(), new Date(), book);
            archiveService.addToArchive(deletedBook);
            return new ResponseEntity<>(deletedBook, HttpStatus.OK);
        } catch (Exception e){
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }

    }



}
