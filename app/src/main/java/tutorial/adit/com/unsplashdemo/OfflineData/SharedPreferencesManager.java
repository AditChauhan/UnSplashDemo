/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.OfflineData;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferencesManager {
	private  static final String MY_APP_PREFERENCES = "ca7eed88-2409-4de7-b529-52598af76734";
	private static final String PREF_USER_LEARNED_DRAWER = "963dfbb5-5f25-4fa9-9a9e-6766bfebfda8";

	private SharedPreferences sharedPrefs;
	private static SharedPreferencesManager instance;

	private SharedPreferencesManager(Context context){
		//using application context just to make sure we don't leak any activities
		sharedPrefs = context.getApplicationContext().getSharedPreferences(MY_APP_PREFERENCES, Context.MODE_PRIVATE);
	}

	public static synchronized SharedPreferencesManager getInstance(Context context){
		if(instance == null)
			instance = new SharedPreferencesManager(context);

		return instance;
	}

	public boolean isNavigationDrawerLearned(){
		return sharedPrefs.getBoolean(PREF_USER_LEARNED_DRAWER, false);
	}

	public void setNavigationDrawerLearned(boolean value){
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putBoolean(PREF_USER_LEARNED_DRAWER, value);
		editor.apply();
	}

}