/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.Api;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import tutorial.adit.com.unsplashdemo.model.SearchResultmodel;
import tutorial.adit.com.unsplashdemo.model.Wallpaper;

public interface WallpaperApi {

   // String BASE_URL = "https://api.unsplash.com/";
    String API_KEY = "b441947f2c492e6878108d749fb32a9ef849958f571911e964430bffe1a2dde9";


    @GET("photos/?client_id=" + API_KEY)
    Observable<List<Wallpaper>> getWallpapers(@Query("per_page") int perPage, @Query("page") int page);


    @GET("search/photos/?client_id=" + API_KEY )
    Observable<SearchResultmodel> getSearchPhotos(@Query("query") String query, @Query("page") int page, @Query("per_page") int pageLimit);


}
