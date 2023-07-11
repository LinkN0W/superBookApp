package ru.team.backend.security.message.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    private String name;

    private String email;
    private String role;

    private String password;
}
