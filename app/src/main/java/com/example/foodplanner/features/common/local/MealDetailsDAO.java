package com.example.foodplanner.features.common.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.features.common.entities.MealDetailsEntity;
import com.example.foodplanner.features.common.entities.MealItemEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
@Dao
public interface MealDetailsDAO {

    @Query("SELECT * FROM mealdetailsentity")
    Flowable<List<MealDetailsEntity>> getAll();

    @Query("SELECT * FROM mealdetailsentity WHERE id == :id LIMIT 1")
    Flowable<MealDetailsEntity> getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(MealDetailsEntity... meals);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<MealDetailsEntity> meals);

    @Delete
    Completable delete(MealDetailsEntity mealDetails);

}
