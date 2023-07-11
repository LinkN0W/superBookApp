package ru.team.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.team.backend.entities.Book;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    private String email;

    private String password;

    private String role;

    private List<Book> books;
}
