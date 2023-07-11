package ru.team.backend.archive;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import ru.team.backend.entities.Book;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "deleted_books")
@NoArgsConstructor
public class DeletedBooks implements DeletedEntities {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private UUID id;

    @Column(name = "remover_id")
    private UUID removerId;


    @Column(name = "date_of_delete")
    private Date dateOfDelete;

    @OneToOne
    @JoinColumn(name="book_id")
    private Book book;

    public Book getBook() {
        return book;
    }

    public DeletedBooks(UUID removerId, Date dateOfDelete, Book book) {
        this.removerId = removerId;
        this.dateOfDelete = dateOfDelete;
        this.book = book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getRemoverId() {
        return removerId;
    }

    @Override
    public Date getDateOfDelete() {
        return dateOfDelete;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;

    }

    @Override
    public void setRemoverId(UUID removerId) {
        this.removerId = removerId;

    }

    @Override
    public void setDateOfDelete(Date dateOfDelete) {
        this.dateOfDelete = dateOfDelete;

    }
}
