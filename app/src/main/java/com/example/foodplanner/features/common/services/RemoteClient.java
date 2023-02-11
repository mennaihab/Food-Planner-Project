package com.example.foodplanner.features.common.services;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RemoteClient {
    private static Retrofit retrofit = null;

    public synchronized static <T> T createClient(Class<T> productsServiceClass) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.themealdb.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(productsServiceClass);
    }
}
