package com.streak.dsastreak.entity;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StreakStatusDTO {

    private String name;

    private String userName;

    private int daysOfStreaks;

    private int numberOfQsns;

    private LocalDate lastUpdated;

}
