package ru.team.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PeriodDateDTO {

    public enum Period {

        MONTH,

        YEAR;

    }


    private Date date;

    @JsonProperty(value = "period")
    private Period period;

}
