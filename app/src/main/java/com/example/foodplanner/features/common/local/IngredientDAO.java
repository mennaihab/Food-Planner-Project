package com.example.foodplanner.features.common.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.features.common.entities.IngredientEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface IngredientDAO {
    @Query("SELECT * FROM ingrediententity")
    Flowable<List<IngredientEntity>> getAll();

    @Query("SELECT * FROM ingrediententity WHERE id == :id LIMIT 1")
    Flowable<IngredientEntity> getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(IngredientEntity... ingredients);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<IngredientEntity> ingredients);

    @Delete
    Completable delete(IngredientEntity ingredient);
}
