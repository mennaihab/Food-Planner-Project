package com.example.foodplanner.features.common.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foodplanner.features.common.models.Meal;

import java.util.List;
@Entity
public class MealEntity {


    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String thumbnail;
    public String category;
    public String area;
    public String instructions;
    public List<String> tags;
    public String youtube;
    public String source;
    public List<Meal.Ingredient> ingredients;

}

