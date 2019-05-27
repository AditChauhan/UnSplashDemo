/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.LocalCache;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import tutorial.adit.com.unsplashdemo.model.Urls;

public class SharedPreferencesLocal {
	 Context ctx;
	int cache_size =0;
	final int limitCache = 10 ;


	public SharedPreferencesLocal(Context context) {
		this.ctx = context ;
	}


	public void saveUrls(String mquery,List<Urls> urls) {

		if(urls.size() > limitCache) {
			cache_size = limitCache;
		} else cache_size = urls.size();
		int i = 0;
		while(i <= cache_size) {
				saveurl( mquery, i, urls.get(i).getRegular());
				i++;
			}
	}

	public  List<Urls> fetchCache(String query)
	{
		List<Urls> murls = new ArrayList<>();
		for(int i = 0 ; i<10;i++) {
			String name = getUrl(query, i);
			Urls mm = new Urls();
			mm.setRegular(name);
			murls.add(mm);
		}

		return murls;

	}


	public void saveurl( String preferenceFileName, int url1, String url2) {
		SharedPreferences.Editor sharedPreferences = ctx.getSharedPreferences(preferenceFileName, 0).edit();
		sharedPreferences.putString(String.valueOf(url1), url2);
		sharedPreferences.apply();
	}


	public  String getUrl( String preferenceFileName, int url1) {
		SharedPreferences sharedPreferences = ctx.getSharedPreferences(preferenceFileName, 0);

		String url = sharedPreferences.getString(String.valueOf(url1),"");
		return url ;
	}
}
