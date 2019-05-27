/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.Api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {



    public static String BASE_URL="https://api.unsplash.com/";
    private static WallpaperApi service;


    public static WallpaperApi getInstance() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
            service = retrofit.create(WallpaperApi.class);
            return service;
        } else {
            return service;
        }
    }
}
