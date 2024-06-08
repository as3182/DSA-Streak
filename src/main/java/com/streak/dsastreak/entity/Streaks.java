package com.streak.dsastreak.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Streaks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int daysOfStreaks;

    @Min(value = 1, message = "Number of questions must be non-negative,zero")
    private int numberOfQsns;

    private LocalDate lastUpdated;



    @OneToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

}
