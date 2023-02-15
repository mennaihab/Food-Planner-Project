package com.example.foodplanner.features.common.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(primaryKeys = {"day", "mealId"})
public class PlanDayEntity {

    @NonNull
    public LocalDate day;
    @NonNull
    public String mealId;

}
