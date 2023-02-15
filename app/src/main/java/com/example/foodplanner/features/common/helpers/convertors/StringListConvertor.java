package com.example.foodplanner.features.common.helpers.convertors;

import androidx.room.TypeConverter;

import com.example.foodplanner.features.common.models.Meal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class StringListConvertor {

    @TypeConverter
    public static List<String> toList(String data){
        if (data == null) {
            return null;
        } else {
            return new Gson().fromJson(data,
                    new TypeToken<ArrayList<String>>(){}.getType()
            );
        }
    }

    @TypeConverter
    public static String fromList(List<String> data) {
        if (data == null) {
            return null;
        } else {
            return new Gson().toJson(data);
        }
    }
}