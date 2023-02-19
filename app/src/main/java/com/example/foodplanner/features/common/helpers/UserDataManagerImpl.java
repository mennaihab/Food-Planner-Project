package com.example.foodplanner.features.common.helpers;

import android.content.Context;

import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.core.utils.UserUtils;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.helpers.mappers.FavouriteMealMapper;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.remote.impl.FavouritesBackupServiceImpl;
import com.example.foodplanner.features.common.repositories.FavouriteRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.common.services.AuthenticationManager;
import com.example.foodplanner.features.common.services.UserDataManager;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.disposables.Disposable;

public class UserDataManagerImpl implements UserDataManager {
    private static final String TAG = "UserDataManagerImpl";

    private final AuthenticationManager authenticationManager;
    private final FavouriteRepository favouriteRepository;
    private final Disposable disposable;

    public UserDataManagerImpl(AuthenticationManager authenticationManager,
                               FavouriteRepository favouriteRepository) {
        this.authenticationManager = authenticationManager;
        this.favouriteRepository = favouriteRepository;
        disposable = authenticationManager.getCurrentUserObservable().subscribe(user -> {
            if (UserUtils.isPresent(user)) {
                favouriteRepository.getBackupFromRemote(UserUtils.getUserId(user)).subscribe();
            }
        });
    }

    public static UserDataManager create(
            Context context,
            FirebaseFirestore firestore,
            AuthenticationManager authenticationManager) {
        return new UserDataManagerImpl(
                authenticationManager,
                new FavouriteRepository(
                        AppDatabase.getInstance(context).favouriteMealDAO(),
                        AppDatabase.getInstance(context).mealItemDAO(),
                        new FavouritesBackupServiceImpl(FoodPlannerApplication.from(context).getFirestore()),
                        new FavouriteMealMapper(new BaseMapper<>(MealItem.class, MealItemEntity.class))
                )
        );
    }

    @Override
    public void close() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
