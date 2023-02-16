package com.example.foodplanner.features.common.remote;

import com.example.foodplanner.features.common.helpers.RemoteMealWrapper;
import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.models.Ingredient;
import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.services.RemoteClient;
import com.example.foodplanner.features.search.helpers.RemoteCategoriesWrapper;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

public interface MealRemoteService {

    static MealRemoteService create() {
        return RemoteClient.createClient(MealRemoteService.class);
    }

    @GET("json/v1/1/list.php?i=list")
    Single<RemoteMealWrapper<Ingredient>> listIngredients();

    @GET("json/v1/1/list.php?a=list")
    Single<RemoteMealWrapper<Area>> listAreas();

    @GET("json/v1/1/categories.php")
    Single<RemoteCategoriesWrapper> listCategories();

    @GET("json/v1/1/lookup.php")
    Single<RemoteMealWrapper<Meal>>listMealDetails();

    @GET("json/v1/1/search.php")
    Single<RemoteMealWrapper<MealItem>> searchByName(@Query("s") String name);

    @GET("json/v1/1/filter.php")
    Single<RemoteMealWrapper<MealItem>> searchByIngredient(@Query("i") String ingredient);

    @GET("json/v1/1/filter.php")
    Single<RemoteMealWrapper<MealItem>> searchByCategory(@Query("c") String category);

    @GET("json/v1/1/filter.php")
    Single<RemoteMealWrapper<MealItem>> searchByArea(@Query("a") String area);
}
