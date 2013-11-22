package it.feio.android.omninotes;

import java.util.Locale;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.utils.Constants;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.widget.Toast;


public class BaseActivity extends SherlockActivity {

	private final boolean TEST = true;

	protected Tracker tracker;
	protected SharedPreferences prefs;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * Executing the application in test will activate ScrictMode to debug heavy 
		 * i/o operations on main thread and data sending to GA will be disabled
		 */
		if (TEST) {
			StrictMode.enableDefaults();
			GoogleAnalytics.getInstance(this).setDryRun(true);
		}

		// Preloads shared preferences for all derived classes
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		super.onCreate(savedInstanceState);
	}


	@Override
	public void onStart() {
		super.onStart();
		// Google Analytics
		EasyTracker.getInstance(this).activityStart(this);
		tracker = GoogleAnalytics.getInstance(this).getTracker("UA-45502770-1");
	}


	@Override
	public void onStop() {
		super.onStop();
		// Google Analytics
		EasyTracker.getInstance(this).activityStop(this);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings:
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				startActivity(settingsIntent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}


	protected boolean navigationArchived() {
		return "1".equals(prefs.getString(Constants.PREF_NAVIGATION, "0"));
	}


	protected void showToast(CharSequence text, int duration) {
		if (prefs.getBoolean("settings_enable_info", true)) {
			Toast.makeText(getApplicationContext(), text, duration).show();
		}
	}
	
	/**
	 * Set custom locale on application start
	 */
	protected void checkLocale(){
		String languageToLoad = prefs.getString("settings_language", Locale.getDefault().getCountry());		
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
	}

}