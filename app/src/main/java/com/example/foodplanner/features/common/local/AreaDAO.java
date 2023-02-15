package com.example.foodplanner.features.common.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.features.common.entities.AreaEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface AreaDAO {
    @Query("SELECT * FROM areaentity")
    Flowable<List<AreaEntity>> getAll();

    @Query("SELECT * FROM areaentity WHERE name == :name LIMIT 1")
    Flowable<AreaEntity> getByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(AreaEntity... areas);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<AreaEntity> areas);

    @Delete
    Completable delete(AreaEntity area);
}
