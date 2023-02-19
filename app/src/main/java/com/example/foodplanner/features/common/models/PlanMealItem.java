package com.example.foodplanner.features.common.models;

import java.time.LocalDate;
import java.util.Objects;

public class PlanMealItem {
    private LocalDate day;
    private MealItem meal;

    public PlanMealItem(LocalDate day, MealItem meal) {
        this.day = day;
        this.meal = meal;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public MealItem getMeal() {
        return meal;
    }

    public void setMeal(MealItem meal) {
        this.meal = meal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanMealItem that = (PlanMealItem) o;
        return Objects.equals(day, that.day) && Objects.equals(meal, that.meal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, meal);
    }
}
