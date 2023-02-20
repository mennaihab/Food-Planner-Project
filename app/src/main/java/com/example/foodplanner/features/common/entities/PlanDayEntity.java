package com.example.foodplanner.features.common.entities;

import androidx.annotation.NonNull;
import androidx.room.DatabaseView;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.time.LocalDate;

@Entity(primaryKeys = {"userId", "day", "mealId"})
public class PlanDayEntity {

    @NonNull
    public String userId;
    @NonNull
    public LocalDate day;
    @NonNull
    public String mealId;
    public String calendarUri;
    public boolean active = true;

    @DatabaseView(
            viewName = "PlanDayEntityView",
            value = "SELECT * FROM plandayentity " +
            "JOIN mealitementity on plandayentity.mealId = mealitementity.id"
    )
    public static class Full {
        public String userId;
        public LocalDate day;
        @Embedded
        public MealItemEntity meal;
        public String calendarUri;
        public boolean active = true;

        public PlanDayEntity toEntity() {
            PlanDayEntity entity = new PlanDayEntity();
            entity.userId = userId;
            entity.day = day;
            entity.mealId = meal.id;
            entity.calendarUri = calendarUri;
            entity.active = active;
            return entity;
        }
    }
}
