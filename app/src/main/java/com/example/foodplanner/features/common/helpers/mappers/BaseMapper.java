package com.example.foodplanner.features.common.helpers.mappers;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class BaseMapper<M, E> {
    private static final String TAG = "BaseMapper";

    private final Class<M> model;
    private final Class<E> entity;

    public BaseMapper(Class<M> model, Class<E> entity) {
        this.model = model;
        this.entity = entity;
    }

    public E toEntity(M model) {
        return map(model, entity);
    }

    public M toModel(E entity) {
        return map(entity, model);
    }

    private static <From, To> To map(From from, Class<To> toClass) {
        try {
            To model = toClass.newInstance();
            for (Field field: from.getClass().getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    try {
                        Field eField = model.getClass().getDeclaredField(field.getName());
                        field.setAccessible(true);
                        eField.setAccessible(true);
                        eField.set(model, field.get(from));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        Log.e(TAG, null, e);
                    }
                }
            }
            return model;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
