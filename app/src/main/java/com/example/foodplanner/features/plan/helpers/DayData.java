package com.example.foodplanner.features.plan.helpers;

import com.example.foodplanner.features.common.models.MealItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DayData {
    private final LocalDate day;
    private final List<MealItem> meals;

    public DayData(LocalDate day, List<MealItem> meals) {
        this.day = day;
        this.meals = meals;
    }

    public LocalDate getDay() {
        return day;
    }

    public List<MealItem> getMeals() {
        return meals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayData dayData = (DayData) o;
        return Objects.equals(day, dayData.day) && Objects.equals(meals, dayData.meals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, meals);
    }
}
