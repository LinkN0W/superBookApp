package ru.team.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.team.backend.entities.Form;
import ru.team.backend.entities.User;
import ru.team.backend.functional.CountFunctional;
import ru.team.backend.functional.FormFunctional;
import ru.team.backend.repositories.BookRepository;
import ru.team.backend.repositories.FormRepository;
import ru.team.backend.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FormService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final FormRepository formRepository;

    @Autowired
    private final BookRepository bookRepository;


    private CountFunctional countFunctional;

    public FormService(UserRepository userRepository, FormRepository formRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.bookRepository = bookRepository;
    }

    public Optional<Form> addBookToUser(UUID userId,Form form) {
        Optional<User> currentUser = userRepository.findById(userId);
        //Book addedBook = bookRepository.findByIdAndDeleteIsFalse(form.getBookId());

        if(currentUser.isPresent()
                && bookRepository.findByIdAndDeleteIsFalse(form.getBookId()).isPresent()
                && !formRepository.existsByBookId(form.getBookId())) {
            form.setUserId(currentUser.get().getId());
            form.setDateOfTaking(new Date());
            form.setPenalties(0);
            formRepository.save(form);
            return formRepository.findById(form.getId());
        }
        else return Optional.empty();
    }

    /*public void deleteForm(UUID userId, UUID bookId){
        formRepository.deleteByUserIdAndBookId(userId, bookId);
        //bookRepository.updateBookByIdPlusOneFromAmount(bookId);
    }
*/

    public Iterable<Form> countUserPenalties(UUID userId, Date date){
        List<Form> forms = formRepository.findAllByUserIdAndDateOfReturningIsNull(userId)
                .stream().filter(e -> date.getTime() > e.getTermOfReturning().getTime() ).toList();
        countFunctional = new FormFunctional();
        forms =  countFunctional.countUserPenaltiesForOnce(forms, date);
        forms.stream().forEach(e -> formRepository.save(e));
        return forms;
    }

    public Iterable<Form> countAllPenalties(Date date){
        List<Form> forms = formRepository.findAllByDateOfReturningIsNull();
        countFunctional = new FormFunctional();
        forms =  countFunctional.countUserPenaltiesForOnce(forms, date);
        forms.stream().forEach(e -> formRepository.save(e));
        return forms;
    }

    public Iterable<Form> showUserForm(UUID userId){
        return formRepository.findAllByUserIdAndDateOfReturningIsNull(userId);
    }
}
