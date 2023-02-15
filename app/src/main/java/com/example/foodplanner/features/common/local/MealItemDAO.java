package com.example.foodplanner.features.common.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.features.common.entities.MealItemEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealItemDAO {

    @Query("SELECT Count(*) FROM mealitementity")
    Single<Long> getCount();

    @Query("SELECT * FROM mealitementity")
    Flowable<List<MealItemEntity>> getAll();

    @Query("SELECT * FROM mealitementity WHERE name LIKE '%' || :name || '%'")
    Flowable<List<MealItemEntity>> getAllWhereNameContains(String name);

    @Query("SELECT * FROM mealitementity WHERE id == :id LIMIT 1")
    Flowable<MealItemEntity> getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(MealItemEntity... meals);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<MealItemEntity> meals);

    @Delete
    Completable delete(MealItemEntity meal);
}
