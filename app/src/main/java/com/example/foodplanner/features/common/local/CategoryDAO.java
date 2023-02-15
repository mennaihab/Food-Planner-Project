package com.example.foodplanner.features.common.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.features.common.entities.CategoryEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface CategoryDAO {
    @Query("SELECT * FROM categoryentity")
    Flowable<List<CategoryEntity>> getAll();

    @Query("SELECT * FROM categoryentity WHERE id == :id LIMIT 1")
    Flowable<CategoryEntity> getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(CategoryEntity... categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<CategoryEntity> categories);

    @Delete
    Completable delete(CategoryEntity category);
}
