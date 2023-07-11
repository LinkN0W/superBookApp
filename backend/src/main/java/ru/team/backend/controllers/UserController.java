package ru.team.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.team.backend.archive.ArchiveService;
import ru.team.backend.archive.DeletedUsers;
import ru.team.backend.dto.UserDTO;
import ru.team.backend.entities.User;

import ru.team.backend.services.UserService;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final ArchiveService archiveService;

    public UserController(UserService userService, ArchiveService archiveService) {
        this.userService = userService;
        this.archiveService = archiveService;
    }

    @GetMapping("/myInfo")
    public User showInfoCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByEmail(auth.getName());
    }


    @GetMapping("/show/{userId}")
    public Optional<UserDTO> show(@PathVariable UUID userId){
        User user = userService.findUserById(userId).get();
        return Optional.of(new UserDTO(user.getEmail(), user.getPassword(), user.getRole().name(), user.getBooks()));
    }

    @PostMapping("/edit/{userId}")
    public Optional<User> update(@PathVariable UUID userId, @RequestBody User user){
        return userService.update(userId,user);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> delete(@PathVariable UUID userId){
        try {

            User user = userService.findByIdAndDeleteIsFalse(userId).get();
            userService.delete(user.getId());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            DeletedUsers deletedUser = new DeletedUsers(userService.getUserByEmail(auth.getName()).getId(), new Date(), user);
            archiveService.addToArchive(deletedUser);
            return new ResponseEntity<>(deletedUser, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
