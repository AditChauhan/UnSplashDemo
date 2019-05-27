/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tutorial.adit.com.unsplashdemo.R;
import tutorial.adit.com.unsplashdemo.adapter.DetailViewPagerAdapter;
import tutorial.adit.com.unsplashdemo.model.Urls;

public class SingleImage extends AppCompatActivity {

    DetailViewPagerAdapter detailViewPagerAdapter;

    int counter=0;
    int size ;
    public  ArrayList<Urls> urlsList;
	private ViewPager viewPager;
	private int initialItem;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		postponeEnterTransition();
		counter = getIntent().getIntExtra("position", 0);
		initialItem = counter;
		urlsList = (ArrayList<Urls>) getIntent().getSerializableExtra("urlslist");
		size = urlsList.size();
		setUpViewPager(urlsList);
	}

	/*
	introducing support using below 3 methods*/

	@Override
	public void finishAfterTransition() {
		int pos = viewPager.getCurrentItem();
		Intent intent = new Intent();
		intent.putExtra("exit_position", pos);
		setResult(RESULT_OK, intent);
		View view = detailViewPagerAdapter.getCurrentView(viewPager);
		if (initialItem != pos) {
			setSharedElementCallback(view);
		}
		super.finishAfterTransition();
	}

	@TargetApi(21)
	public void setStartPostTransition(final View sharedView) {

		sharedView.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						sharedView.getViewTreeObserver().removeOnPreDrawListener(this);
						startPostponedEnterTransition();
						return false;
					}
				});
	}


	@TargetApi(21)
	private void setSharedElementCallback(final View view) {
		setEnterSharedElementCallback(new SharedElementCallback() {
			@Override
			public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
				int current = viewPager.getCurrentItem();
				String name = getApplicationContext().getString(R.string.transition_name,current);
				View v  = detailViewPagerAdapter.getview(name);
				names.clear();
				sharedElements.clear();
				names.add(name);
				sharedElements.put(name, v);
			}
		});
	}


	private void setUpViewPager(ArrayList<Urls> photos) {
		viewPager = (ViewPager) findViewById(R.id.pager);
		detailViewPagerAdapter  = new DetailViewPagerAdapter(this, photos,initialItem);
		viewPager.setAdapter(detailViewPagerAdapter);
		viewPager.setCurrentItem(initialItem);
		viewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom,
			                           int oldLeft, int oldTop, int oldRight, int oldBottom) {

				if (viewPager.getChildCount() > 0) {
					viewPager.removeOnLayoutChangeListener(this);
					startPostponedEnterTransition();
				}
			}
		});

		viewPager.setPageMarginDrawable(R.drawable.page_margin);
	}


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();


    }@Override
    protected void onResume() {
        super.onResume();


    }


}
