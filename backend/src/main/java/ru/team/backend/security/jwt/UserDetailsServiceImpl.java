package ru.team.backend.security.jwt;

import ru.team.backend.entities.User;
import ru.team.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var userByName = this.userRepository.findUserByName(login);
        User user = userByName.isEmpty() ? userRepository.findUserByEmail(login).get():userByName.get();
        //.orElseThrow(
        //() -> new UsernameNotFoundException("User Not Found with -> username or email : " + login));
        return UserPrinciple.build(user);
    }
}