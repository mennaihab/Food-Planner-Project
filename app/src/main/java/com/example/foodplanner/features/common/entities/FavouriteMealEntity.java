package com.example.foodplanner.features.common.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.DatabaseView;

@Entity(primaryKeys = {"userId", "mealId"})
public class FavouriteMealEntity {

    @NonNull
    public String userId;
    @NonNull
    public String mealId;
    public boolean active = true;

    @DatabaseView(
            viewName = "FavouriteMealEntityView",
            value = "SELECT * FROM favouritemealentity " +
            "JOIN mealitementity on favouritemealentity.mealId = mealitementity.id"
    )
    public static class Full {
        public String userId;
        @Embedded
        public MealItemEntity meal;
        public boolean active = true;

        public FavouriteMealEntity toEntity() {
            FavouriteMealEntity entity = new FavouriteMealEntity();
            entity.userId = userId;
            entity.mealId = meal.id;
            entity.active = active;
            return entity;
        }
    }
}
