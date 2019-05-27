/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tutorial.adit.com.unsplashdemo.LocalCache.SharedPreferencesLocal;
import tutorial.adit.com.unsplashdemo.Presenter.PresenterImpl;
import tutorial.adit.com.unsplashdemo.Presenter.SearchContract;
import tutorial.adit.com.unsplashdemo.R;
import tutorial.adit.com.unsplashdemo.adapter.SearchAdapter;
import tutorial.adit.com.unsplashdemo.model.Urls;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.RecyclerViewClickListener, SearchContract.Views {

	private static final String TAG = SearchActivity.class.getSimpleName()+"debug";
	public String mquery;
	static int page = 1;
	PresenterImpl presenter ;
	int cache_size =0;
	public static List<Urls> urlsList;
	RecyclerView imageRecyclerView;
	GridLayoutManager mLayoutManager;
	SearchAdapter searchAdapter;
	Context ctx;
	boolean isLoading = false;
	static boolean isonline = false;
	MaterialSearchView searchView;
	public ProgressBar progressBar;
	Boolean onscroll = false;
	private int exitPosition;
	private int enterPosition;
	final int itemsPerPage = 20;
	final int limitCache = 8 ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		urlsList = new ArrayList<>();
		ctx = this;


		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitleTextColor(Color.WHITE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		initialSetup();
		setExitSharedElementCallback(ExitTransitionCallback);

		searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextSubmit(String query) {
				presenter.updateQuery(query);
				page= 1 ;
				presenter.setPage(page);
				clearAdapter();
				presenter.loadData();
				searchView.closeSearch();
				return true ;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
			@Override
			public void onSearchViewShown() {
			}

			@Override
			public void onSearchViewClosed() {

			}
		});


//		imageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//			@Override
//			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//				super.onScrolled(recyclerView, dx, dy);
//				onscroll = true ;
//				// Check if end of page has been reached
//				if(!isLoading && ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition() == searchAdapter.getItemCount() - 1) {
//					Log.d("**debug","page :" + page);
//					isLoading = true;
//					page++;
//					if(isonline) {
//						downloadImages();
//					}
//				}
//			}
//		});



		imageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				onscroll = true ;
				if(dy > 0) {
					if(!isLoading && ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition() == searchAdapter.getItemCount() - 1) {
						page++;
						presenter.setPage(page);
						presenter.scrolling();
					}
				}
			}
		});
	}




	@Override
	public void showOffline(List<Urls> data) {
	}



	@Override
	public void showProgress()
		{
			progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progressBar.setVisibility(View.GONE);
	}


	@Override
	public void showError() {
		Toast.makeText(this,"Starting App first time, No cached Images",Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean adapterIsEmpty() {
		if(searchAdapter.getItemCount() == 0)
		{
			return  true;
		}
		return false;
	}

	@Override
	public void clearAdapter() {
		searchAdapter.clearImages();
	}


	public void initialSetup()
	{

		SharedPreferencesLocal sharedPreferences =  new SharedPreferencesLocal(SearchActivity.this);
		progressBar = (ProgressBar)findViewById(R.id.progress_bar);
		searchView = (MaterialSearchView) findViewById(R.id.search_view);
		imageRecyclerView =  (RecyclerView)findViewById(R.id.images_recycler_view);
		mLayoutManager = new GridLayoutManager(ctx, 2);
		imageRecyclerView.setLayoutManager(mLayoutManager);
		imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
		searchAdapter = new SearchAdapter(ctx,this);
		imageRecyclerView.setAdapter(searchAdapter);
		presenter  = new PresenterImpl(this,sharedPreferences);

	}




	/*<--------fetches url list from
	                    unsplash Rest api
	                            --------->*/

	@Override
	public void showData(List<Urls> data) {

		searchAdapter.addImages(data);
	}



//	/*presenter*/
//	private void getFirstSearchPhotos() {
//		WallpaperApi.Client.getInstance().getSearchPhotos(mquery,page,itemsPerPage).enqueue(new Callback<SearchResultmodel>() {
//			@Override
//			public void onResponse(Call<SearchResultmodel> call, Response<SearchResultmodel> response) {
//				isonline = true;
//				List<Urls> murls = getImageUrls(response.body());
//				if(murls.size() > limitCache) {
//					cache_size = limitCache;
//				} else cache_size = murls.size();
//				int i = 1;
//				if(!onscroll)
//				{
//					while(i <= cache_size) {
//						saveurl(getBaseContext(), mquery, i, murls.get(i).getRegular());
//						i++;
//					}
//				}
//				progressBar.setVisibility(View.GONE);
//				isLoading = false;
//			}
//			@Override
//			public void onFailure(Call<SearchResultmodel> call, Throwable t) {
//				isonline = false;
//
//				urlsList = fetchCache(mquery);
//				if(urlsList.size() > 0 )
//				{
//
//					progressBar.setVisibility(View.GONE);
//					searchAdapter.addImages(urlsList);
//				}
//				else
//				{
//
//				}
//
//			}
//		});
//	}





/*<------------- Transition support --------------->*/


		/* transiton support
				assigning exit& entry position*/

	@Override
	public void recyclerViewListClicked(int position) {

		enterPosition = position ;
		exitPosition = position;




	}

		/*	transiton support
					shared element callback */

	private final SharedElementCallback ExitTransitionCallback = new SharedElementCallback() {
		@SuppressLint("NewApi")
		@Override
		public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {

			if(exitPosition == enterPosition) {
			} else
			{
				names.clear();
				sharedElements.clear();
				View view = searchAdapter.getViewAtIndex(imageRecyclerView,exitPosition);
				Log.d(TAG, "Source : SharedElementCallback   view.transitionnmae - "+ view.getTransitionName());


				if(view != null) {
					names.add(view.getTransitionName());
					sharedElements.put(view.getTransitionName(), view);
				}
			}
		}
	};


		/*Transition support
					onActivityReenter*/
	@Override
	public void onActivityReenter(int resultCode, Intent data)
	{
		postponeEnterTransition();
		imageRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				startPostponedEnterTransition();

				return true;
			}
		});
		if (resultCode == RESULT_OK && data != null) {

			exitPosition = data.getIntExtra("exit_position", enterPosition);
			Log.d(TAG, "Source : onActivityReenter   exitPosition - "+ exitPosition);
			imageRecyclerView.scrollToPosition(exitPosition);
			Log.d(TAG, "--> "+imageRecyclerView.getChildCount());
		}
	}

		/*<------------- Transition support --------------->*/


				/*saving urls in sharedpreferences
						for offline support  */


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		final MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView.setMenuItem(searchItem);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {

			case android.R.id.home:
				finish();
				return true;

			case R.id.span2:
				mLayoutManager.setSpanCount(2);
				return true;

			case R.id.span3:
				mLayoutManager.setSpanCount(3);
				return true;

			case R.id.span4:
				mLayoutManager.setSpanCount(4);

				return true;
		}
		return super.onOptionsItemSelected(item);

	}



	@Override
	protected void onStart() {
		super.onStart();
	}




//
//	public static void saveurl(Context context, String preferenceFileName, int url1, String url2) {
//		SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(preferenceFileName, 0).edit();
//		sharedPreferences.putString(String.valueOf(url1), url2);
//		sharedPreferences.apply();
//	}
//	/*fetching urls present in sharedpreferences
//					for offline support  */
//	public static String getUrl(Context context, String preferenceFileName, int url1) {
//		SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
//		String url = sharedPreferences.getString(String.valueOf(url1),"");
//		return url ;
//	}




}
