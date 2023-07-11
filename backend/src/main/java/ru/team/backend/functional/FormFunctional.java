package ru.team.backend.functional;

import ru.team.backend.entities.Form;

import java.util.Date;
import java.util.List;

public class FormFunctional implements CountFunctional{


    @Override
    public List<Form> countUserPenaltiesForOnce( List<Form> forms, Date date) {
        forms.stream().forEach(e -> {
                    int difference = 0;
                    if( e.getDateOfReturning() == null)
                        difference = (int) ((e.getTermOfReturning().getTime() - date.getTime()) / 86400000);
                    else
                        difference = (int) ((e.getTermOfReturning().getTime() - e.getDateOfReturning().getTime()) / 86400000);

                    if(difference < 0){
                        e.setDelay(Math.abs(difference));
                        e.setPenalties( 5*(Math.abs(difference)));
                    }
                    else{
                        e.setPenalties(0);
                        e.setDelay(0);
                    }
                }
        );

        return forms;
    }

    @Override
    public List<Form> countUserPenaltiesForPeriod(List<Form> forms, Date dateOfBegin, Date dateOfEnd) {
        forms.stream().forEach(e -> {
                    long difference = 0;
                    if (e.getDateOfReturning() != null)
                        difference = Math.min(dateOfEnd.getTime(), e.getDateOfReturning().getTime())
                                - Math.max(dateOfBegin.getTime(), e.getTermOfReturning().getTime());
                    else
                        difference = dateOfEnd.getTime() - Math.max(dateOfBegin.getTime(), e.getTermOfReturning().getTime());
                    if(difference >= 0){
                        System.out.println(difference);
                        e.setDelay((int) (difference/ 86400000)+1);
                        e.setPenalties(e.getDelay()*5);
                    }
                    else{
                        e.setDelay(0);
                        e.setPenalties(0);
                    }

        }



        );

        forms.stream().forEach(e -> System.out.println(e.getPenalties()));

        return forms;
    }
}
