package com.example.foodplanner.features.common.services;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foodplanner.features.common.entities.AreaEntity;
import com.example.foodplanner.features.common.entities.CategoryEntity;
import com.example.foodplanner.features.common.entities.FavouriteMealEntity;
import com.example.foodplanner.features.common.entities.IngredientEntity;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.entities.PlanDayEntity;
import com.example.foodplanner.features.common.helpers.convertors.DateConvertor;
import com.example.foodplanner.features.common.helpers.convertors.IngredientListConvertor;
import com.example.foodplanner.features.common.helpers.convertors.LocalDateConvertor;
import com.example.foodplanner.features.common.helpers.convertors.StringListConvertor;
import com.example.foodplanner.features.common.local.AreaDAO;
import com.example.foodplanner.features.common.local.CategoryDAO;
import com.example.foodplanner.features.common.local.FavouriteMealDAO;
import com.example.foodplanner.features.common.local.IngredientDAO;
import com.example.foodplanner.features.common.local.MealItemDAO;
import com.example.foodplanner.features.common.local.PlanDayDAO;

@Database(entities = {
        AreaEntity.class,
        CategoryEntity.class,
        IngredientEntity.class,
        MealItemEntity.class,
        PlanDayEntity.class,
        FavouriteMealEntity.class,
},
views = {
        PlanDayEntity.Full.class,
        FavouriteMealEntity.Full.class,
},
exportSchema = false, version = 1)
@TypeConverters({
        IngredientListConvertor.class,
        LocalDateConvertor.class,
        StringListConvertor.class,
        DateConvertor.class,
})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "products").build();
        }
        return instance;
    }

    public abstract AreaDAO areaDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract IngredientDAO ingredientDAO();
    public abstract MealItemDAO mealItemDAO();
    public abstract PlanDayDAO planDayDAO();

    public abstract FavouriteMealDAO favouriteMealDAO();
}
