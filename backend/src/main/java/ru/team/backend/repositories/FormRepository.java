package ru.team.backend.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.team.backend.entities.Form;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormRepository extends CrudRepository<Form, UUID> {

    List<Form> findAllByUserId(UUID id);

    List<Form> findAllByUserIdAndDateOfReturningIsNull(UUID id);
    List<Form> findAllByDateOfReturningIsNull();


    Optional<Form> findByUserIdAndBookId(UUID userId, UUID bookId);


    boolean existsByBookIdAndDateOfReturningIsNotNull(UUID bookId);

    void deleteByUserIdAndBookId(UUID userId, UUID bookId);

    @Query("SELECT f FROM Form f where (f.dateOfReturning is NULL or f.dateOfReturning > :dateOfBegin) and f.dateOfTaking < :dateOfEnd")
    List<Form> findAllByDatePeriod(@Param("dateOfBegin")Date dateOfBegin, @Param("dateOfEnd")Date dateOfEnd);

    @Modifying
    @Transactional
    @Query("UPDATE Form f set f.dateOfReturning = :dateOfReturning " +
            "where f.id = :id")
    void returnBook(@Param("id") UUID id,   @Param("dateOfReturning") Date dateOfReturning);

    Optional<Form> getFormByUserIdAndBookIdAndDateOfReturningIsNull( UUID userId,  UUID bookId);


}
