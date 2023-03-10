package com.example.foodplanner.features.common.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodplanner.features.common.entities.FavouriteMealEntity;
import com.example.foodplanner.features.common.entities.MealItemEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface FavouriteMealDAO {

    @Query("SELECT * FROM favouritemealentityview WHERE userId = :userId")
    Flowable<List<FavouriteMealEntity.Full>> getAll(String userId);

    @Query("SELECT * FROM favouritemealentityview WHERE userId = :userId AND active = 1")
    Flowable<List<FavouriteMealEntity.Full>> getAllActive(String userId);

    @Query("SELECT id FROM favouritemealentityview WHERE userId = :userId AND active = 1")
    Flowable<List<String>> getAllActiveIds(String userId);

    @Query("SELECT * FROM favouritemealentityview WHERE userId = :userId AND id = :mealId")
    Flowable<Optional<FavouriteMealEntity.Full>> getById(String userId, String mealId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(FavouriteMealEntity... days);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<FavouriteMealEntity> days);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable updateAll(FavouriteMealEntity... days);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateAll(List<FavouriteMealEntity> days);

    @Delete
    Completable delete(FavouriteMealEntity day);
}
