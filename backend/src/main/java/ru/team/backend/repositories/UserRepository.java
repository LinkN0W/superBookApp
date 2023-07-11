package ru.team.backend.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.team.backend.entities.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {


    Optional<User> findUserByEmail(String email);

    Iterable<User> findUsersByDeleteIsFalse();

    Optional<User> findByIdAndDeleteIsFalse(UUID id);


    Optional<User> findUserByName(String name);


    @Modifying
    @Transactional
    @Query("UPDATE User u set u.email = :email, u.password = :password,  u.role = :role where u.id = :id")
    void updateUserById(@Param("id") UUID id, @Param("email") String email,
                        @Param("password") String password, @Param("role") String role);



    @Modifying
    @Transactional
    @Query("UPDATE User u set u.delete = :delete where u.id = :id")
    void delete(@Param("id") UUID id,@Param("delete") Boolean is_delete);

}
