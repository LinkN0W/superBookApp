package ru.team.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.team.backend.entities.User;
import ru.team.backend.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public boolean canRegister( String email) {

        if (!userRepository.findUserByEmail(email).isEmpty()) {
            return false;
        }
        return true;

    }
    public User getUserByEmail(String email){
        return userRepository.findUserByEmail(email).get();
    }

    public Optional<User> findByIdAndDeleteIsFalse(UUID uuid){
        return userRepository.findByIdAndDeleteIsFalse(uuid);
    }


    public void save(User user){
        userRepository.save(user);
    }

    public Optional<User> findUserById(UUID id){
        return userRepository.findById(id);
    }

    public Optional<User> update(UUID id, User user){
        userRepository.updateUserById(id, user.getEmail(), user.getPassword(), user.getRole().name());
        return findUserById(id);
    }
    public void delete(UUID id){
        userRepository.delete(id,true);
    }

}
