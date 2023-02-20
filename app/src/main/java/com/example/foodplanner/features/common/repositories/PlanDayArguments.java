package com.example.foodplanner.features.common.repositories;

import java.time.LocalDate;

public class PlanDayArguments {

    private String userId;
    private LocalDate startDay;
    private LocalDate endDay;

    public PlanDayArguments(String userId, LocalDate startDay, LocalDate endDay) {
        this.userId = userId;
        this.startDay = startDay;
        this.endDay = endDay;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }
}
