package ru.team.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.team.backend.dto.PeriodDateDTO;
import ru.team.backend.entities.Form;
import ru.team.backend.export.ExcelExportService;
import ru.team.backend.services.FormService;
import ru.team.backend.services.UserService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/form")
public class FormController {

    @Autowired
    private FormService formService;

    @Autowired
    UserService userService;

    @Autowired
    private ExcelExportService excelExportService;


    @Autowired
    private final AuthenticationManager authenticationManager;

    public FormController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/add")
    public Optional<Form> addBookToUser(@RequestBody Form form){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return formService.addBookToUser(userService.getUserByEmail(auth.getName()).getId(),form);
    }
    @GetMapping("/showBook")
    public Iterable<Form> showUserForm(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return formService.showUserForm(userService.getUserByEmail(auth.getName()).getId());
    }

    @GetMapping("/count/{userId}")
    public Iterable<Form> countUserPenalties(@PathVariable UUID userId ){
        return formService.countUserPenalties(userId, new Date());
    }


    @PostMapping("/export")
    public int export(@RequestBody PeriodDateDTO period){
        SimpleDateFormat monthDateFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearDateFormat = new SimpleDateFormat("YYYY");

        Integer month = Integer.valueOf(monthDateFormat.format(period.getDate()));
        Integer year = Integer.valueOf(yearDateFormat.format(period.getDate()));

        LocalDate dateOfBegin = LocalDate.now();
        LocalDate dateOfEnd = LocalDate.now();

        switch (period.getPeriod()){
            case MONTH:
                    dateOfBegin = LocalDate.of(year,month,1);
                    dateOfEnd = LocalDate.of(year,month, 30);
                    break;
            case YEAR:
                    dateOfBegin = LocalDate.of(year,1,1);
                    dateOfEnd = LocalDate.of(year,12, 31);
                    break;
        }


        System.out.println(dateOfBegin);
        System.out.println(dateOfEnd);

        excelExportService.exportData("name", Date.from(dateOfBegin.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(dateOfEnd.atStartOfDay(ZoneId.systemDefault()).toInstant()) );
        return month;
    }


    /*@DeleteMapping("/delete/{userId}/{bbokId}")
    void deleteForm(@PathVariable("userId") UUID userId, @PathVariable("bookId") UUID bookId){
        formService.deleteForm(userId, bookId);

    }
*/


}
