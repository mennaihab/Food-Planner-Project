package com.example.foodplanner.features.common.helpers.mappers;

import com.example.foodplanner.features.common.entities.FavouriteMealEntity;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;

public class IdentityMapper<T> extends BaseMapper<T, T> {

    public IdentityMapper(Class<T> clazz) {
        super(clazz, clazz);
    }

    @Override
    protected <From, To> To map(From from, Class<To> toClass) {
        return (To) from;
    }
}
