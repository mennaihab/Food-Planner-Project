package com.example.foodplanner.features.common.helpers.mappers;

import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.entities.PlanDayEntity;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;

public class PlanMealMapper extends BaseMapper<PlanMealItem, PlanDayEntity.Full> {
    private final BaseMapper<MealItem, MealItemEntity> mealMapper;

    public PlanMealMapper(BaseMapper<MealItem, MealItemEntity> mealMapper) {
        super(PlanMealItem.class, PlanDayEntity.Full.class);
        this.mealMapper = mealMapper;
    }

    @Override
    protected <From, To> To map(From from, Class<To> toClass) {
        if (toClass.isAssignableFrom(MealItem.class)) {
            return (To) mealMapper.toModel((MealItemEntity)from);
        } else if (toClass.isAssignableFrom(MealItemEntity.class)) {
            return (To) mealMapper.toEntity((MealItem)from);
        }
        return super.mapValue(from, toClass);
    }
}
