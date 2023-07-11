package ru.team.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User{

    public User(UUID id, String email, String password, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {

    }


    public static enum UserRole {
        ROLE_ADMIN, ROLE_USER;
        public static User.UserRole getById(String id){
            for(User.UserRole e : values()) {
                if(e.name().equalsIgnoreCase(id)) return e;
            }
            return ROLE_USER;
        }
    }
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id ;


    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    @Column(name = "delete")
    private Boolean delete;




    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "form",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books;


    public void addBook(Book book){
        books.add(book);
    }

}
