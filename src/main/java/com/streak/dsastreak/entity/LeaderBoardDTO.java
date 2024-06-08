package com.streak.dsastreak.entity;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class LeaderBoardDTO
{
    private String userName;
    private int daysOfStreaks;
    private int numberOfQsns;
}
