package ru.team.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.team.backend.entities.User;
import ru.team.backend.security.jwt.JwtService;
import ru.team.backend.security.jwt.UserPrinciple;
import ru.team.backend.security.message.requests.LoginRequest;
import ru.team.backend.security.message.requests.RegisterRequest;
import ru.team.backend.security.message.responses.JwtResponse;
import ru.team.backend.security.message.responses.ResponseMessage;
import ru.team.backend.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        if (!this.userService.canRegister(request.getEmail()))
            return new ResponseEntity<>(new ResponseMessage("Email is already in use"),
                    HttpStatus.BAD_REQUEST);

        User user = new User(UUID.randomUUID(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                User.UserRole.ROLE_USER);
        userService.save(user);

        //memberService.save((Listener) listener);

        UserPrinciple userPrinciple = UserPrinciple.build(user);
        String jwtToken = jwtService.generateToken(userPrinciple);

        System.out.println(jwtToken);

        return new ResponseEntity<>(new ResponseMessage("User registration is successful"), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){

        User userEntity;

        userEntity = this.userService.getUserByEmail(request.getEmail());

        System.out.println(userEntity.getEmail()+" "+userEntity.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEntity.getEmail(), request.getPassword()));



        String jwt = jwtService.generateToken((UserPrinciple) authentication.getPrincipal());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }
}
