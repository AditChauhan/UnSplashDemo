/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.Api;

import android.database.Observable;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import tutorial.adit.com.unsplashdemo.model.SearchResultmodel;
import tutorial.adit.com.unsplashdemo.model.Wallpaper;

public interface UnsplashService {

    @GET("/photos")
    Observable<List<Wallpaper>> getRecentPhotos(@Query("client_id") String apiKey, @Query("page") int page, @Query("per_page") int pageLimit);


    @GET("search/photos")
    Observable<SearchResultmodel> getSearchPhotos(@Query("client_id") String apiKey, @Query("query") String query, @Query("page") int page, @Query("per_page") int pageLimit);

}
