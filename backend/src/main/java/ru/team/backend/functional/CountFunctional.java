package ru.team.backend.functional;

import ru.team.backend.entities.Form;

import java.util.Date;
import java.util.List;

public interface CountFunctional {
    List<Form> countUserPenaltiesForOnce( List<Form> forms, Date date);

    List<Form> countUserPenaltiesForPeriod( List<Form> forms, Date dateOfBegin, Date dateOfEnd);


}
