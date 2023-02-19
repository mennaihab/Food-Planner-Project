package com.example.foodplanner.features.common.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.features.common.entities.MealItemEntity;

import java.util.List;
import java.util.Optional;

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
    Flowable<Optional<MealItemEntity>> getById(String id);

    @Query("SELECT * FROM mealitementity ORDER BY RANDOM() LIMIT 1")
    Flowable<Optional<MealItemEntity>> getRandom();

    @Query("SELECT * FROM mealitementity ORDER BY RANDOM() LIMIT :count")
    Flowable<List<MealItemEntity>> getRandom(int count);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(MealItemEntity... meals);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<MealItemEntity> meals);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertAllNew(MealItemEntity... meals);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertAllNew(List<MealItemEntity> meals);

    @Delete
    Completable delete(MealItemEntity meal);
}
