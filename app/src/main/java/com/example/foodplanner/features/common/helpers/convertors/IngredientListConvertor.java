package com.example.foodplanner.features.common.helpers.convertors;

import androidx.room.TypeConverter;

import com.example.foodplanner.features.common.models.Meal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IngredientListConvertor {

    @TypeConverter
    public static List<Meal.Ingredient> toList(String data){
        if (data == null) {
            return null;
        } else {
            return new Gson().fromJson(data,
                    new TypeToken<ArrayList<Meal.Ingredient>>(){}.getType()
            );
        }
    }

    @TypeConverter
    public static String fromList(List<Meal.Ingredient> data) {
        if (data == null) {
            return null;
        } else {
            return new Gson().toJson(data);
        }
    }
}