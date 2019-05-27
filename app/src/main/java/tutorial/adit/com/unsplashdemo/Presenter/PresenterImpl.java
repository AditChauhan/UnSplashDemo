/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.Presenter;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tutorial.adit.com.unsplashdemo.Api.ApiClient;
import tutorial.adit.com.unsplashdemo.LocalCache.SharedPreferencesLocal;
import tutorial.adit.com.unsplashdemo.model.ResultsArray;
import tutorial.adit.com.unsplashdemo.model.SearchResultmodel;
import tutorial.adit.com.unsplashdemo.model.Urls;

public class PresenterImpl implements SearchContract.Presenter {

    String mQuery ;
    boolean isLoading = false;
    static boolean isonline = false;
    Boolean onscroll = false;
    ApiClient apiClient;
    SearchContract.Views mView;
    int page_Counter ;
    final int page_Limit = 20 ;
    List<Urls> fetched_Urls;
    int total_Pages;
    SharedPreferencesLocal preferences ;
    public PresenterImpl(SearchContract.Views mView, SharedPreferencesLocal  sharedPreferences) {
    	this.preferences = sharedPreferences;
        this.mView = mView;

    }

    @Override
    public void setLoadingStatus(boolean loading_Status) {
        isLoading = loading_Status;
    }

    @Override
    public void scrolling() {
        isLoading = true ;
        if(isonline)
        {
            onscroll= true ;
            loadData();
        }
    }

    @Override
    public void updateQuery(String mquery) {
        mQuery = mquery;
    }


    @Override
    public void isScrolling(boolean status) {


    }

    @Override
    public void setPage(int page) {
        page_Counter = page ;
    }


    @Override
    public void loadData() {
        mView.showProgress();
        apiClient.getInstance().getSearchPhotos(mQuery,page_Counter,page_Limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchResultmodel>() {
                    @Override
                    public void onCompleted() {
                        mView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {

                        if(mView.adapterIsEmpty()) {
                            if(isonline == false) {
                                fetched_Urls = preferences.fetchCache(mQuery);
                                if(fetched_Urls.size() > 0) {
                                    mView.showData(fetched_Urls);
                                }
                                mView.hideProgress();
                            }
                        }
                        else
                        {
                            mView.showError();
                        }
                    }


                    @Override
                    public void onNext(SearchResultmodel data)
                    {
                        total_Pages = data.getTotalPages();
                        isonline = true ;
                        fetched_Urls =  getImageUrls(data);
                        cacheLocal();
                        isLoading =  false;

                    }
                });
    }


    public void cacheLocal()
    {
        mView.showData(fetched_Urls);
        if(!onscroll)
        {
            preferences.saveUrls(mQuery,fetched_Urls);

        }
       // onscroll = false ;
    }


//
//    WallpaperApi.Client.getInstance().getSearchPhotos(mquery, page, itemsPerPage).enqueue(new Callback<SearchResultmodel>() {
//        @Override
//        public void onResponse(Call< SearchResultmodel > call, Response<SearchResultmodel> response) {
//            isonline = true;
//            List<Urls> murls = getImageUrls(response.body());
//
//            progressBar.setVisibility(View.GONE);
//            searchAdapter.addImages(getImageUrls(response.body()));
//            isLoading = false;
//        }
//        @Override
//        public void onFailure(Call<SearchResultmodel> call, Throwable t) {
//            isonline = false;
//
//            urlsList = fetchCache(mquery);
//            if(urlsList.size() > 0 )
//            {
//
//                progressBar.setVisibility(View.GONE);
//                searchAdapter.addImages(urlsList);
//            }
//            else
//            {
//
//            }
//
//        }
//    });








    /*
     * presenter
     */

    public List<Urls> getImageUrls(SearchResultmodel wallpapers)
    {
        List<Urls> murls = new ArrayList<>();
        List<ResultsArray> mResults = wallpapers.getResults();
        for (int i=0; i< mResults.size(); i++)
        {
            murls.add(mResults.get(i).getUrls());
        }
        return  murls;
    }

    /*
     * presenter
     */

//    public  List<Urls> fetchCache(String query)
//    {
//        List<Urls> murls = new ArrayList<>();
//        for(int i = 1 ; i<9;i++) {
//            String name = getUrl(getApplicationContext(), mquery, i);
//            Urls mm = new Urls();
//            mm.setRegular(name);
//            murls.add(mm);
//        }
//
//        return murls;
//
//    }



    public static void saveurl(Context context, String preferenceFileName, int url1, String url2) {
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(preferenceFileName, 0).edit();
        sharedPreferences.putString(String.valueOf(url1), url2);
        sharedPreferences.apply();
    }
    /*fetching urls present in sharedpreferences
					for offline support  */
    public static String getUrl(Context context, String preferenceFileName, int url1) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        String url = sharedPreferences.getString(String.valueOf(url1),"");
        return url ;
    }





}
