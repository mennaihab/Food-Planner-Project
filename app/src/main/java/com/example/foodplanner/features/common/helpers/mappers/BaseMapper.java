package com.example.foodplanner.features.common.helpers.mappers;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

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

    protected <From, To> To mapValue(From from, Class<To> toClass) {
        return (To) from;
    }

    protected <From, To> To map(From from, Class<To> toClass) {
        try {
            To model = toClass.newInstance();
            List<Field> notFoundFields = new LinkedList<>();
            for (Field field: from.getClass().getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    MapperInfo mapperInfo = field.getAnnotation(MapperInfo.class);
                    if (mapperInfo != null && mapperInfo.ignored())
                        continue;
                    try {
                        Field eField = model.getClass().getDeclaredField(field.getName());
                        field.setAccessible(true);
                        eField.setAccessible(true);
                        Object value = field.get(from);
                        if (value != null) {
                            eField.set(model, mapValue(value, eField.getType()));
                        }
                    } catch (IllegalAccessException e) {
                        Log.e(TAG, null, e);
                    } catch (NoSuchFieldException ignored) {
                        notFoundFields.add(field);
                    }
                }
            }
            if (!notFoundFields.isEmpty()) {
                Log.d(TAG, "map: notFoundFields: " + notFoundFields);
            }
            return model;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
