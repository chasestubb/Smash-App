package com.example.android.sqliteweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sqliteweather.data.ForecastItem;
import com.example.android.sqliteweather.data.City;
import com.example.android.sqliteweather.data.Status;
import com.example.android.sqliteweather.utils.OpenWeatherMapUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ForecastAdapter.OnForecastItemClickListener, SavedItemsAdapter.OnCityClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    // ForecastItem declare
    private TextView mForecastLocationTV;
    private RecyclerView mForecastItemsRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;

    // City declare
    private TextView mCityTV;
    private RecyclerView mSavedItemsRV;

    private DrawerLayout mDrawerLayout; // drawer layout


    private ForecastAdapter mForecastAdapter;
    private ForecastViewModel mForecastViewModel;

    private SavedItemsAdapter mSavedItemsAdapter;
    private SavedItemsViewModel mSavedItemsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_menu);

        // Remove shadow under action bar.
        getSupportActionBar().setElevation(0);

        // ForecastItems :: LAYOUT FIND
        mForecastLocationTV = findViewById(R.id.tv_forecast_location);
        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mForecastItemsRV = findViewById(R.id.rv_forecast_items);

        // ForecastItems :: Set recycler view
        mForecastAdapter = new ForecastAdapter(this);
        mForecastItemsRV.setAdapter(mForecastAdapter);
        mForecastItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mForecastItemsRV.setHasFixedSize(true);

        // Cities :: LAYOUT FIND
        mCityTV = findViewById(R.id.tv_city_name);
        mSavedItemsRV = findViewById(R.id.rv_nav_drawer);

        // Cities :: Set recycler view
        mSavedItemsAdapter = new SavedItemsAdapter(this);
        mSavedItemsRV.setAdapter(mSavedItemsAdapter);
        mSavedItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mSavedItemsRV.setHasFixedSize(true);


        mDrawerLayout = findViewById(R.id.drawer_layout); // drawer layout


        /*
         * This version of the app code uses the new ViewModel architecture to manage data for
         * the activity.  See the classes in the data package for more about how the ViewModel
         * is set up.  Here, we simply grab the forecast data ViewModel.
         */
        mForecastViewModel = new ViewModelProvider(this).get(ForecastViewModel.class);

        /*
         * Attach an Observer to the forecast data.  Whenever the forecast data changes, this
         * Observer will send the new data into our RecyclerView's adapter.
         */
        mForecastViewModel.getForecast().observe(this, new Observer<List<ForecastItem>>() {
            @Override
            public void onChanged(@Nullable List<ForecastItem> forecastItems) {
                mForecastAdapter.updateForecastItems(forecastItems);
            }
        });

        /*
         * Attach an Observer to the SAVED ITEMS data.  Whenever the forecast data changes, this
         * Observer will send the new data into our RecyclerView's adapter.
         */
// VIEW MODEL
//        mSavedItemsViewModel = new ViewModelProvider(this).get(SavedItemsViewModel.class);
//
//        LiveData<List<City>> all_locations = mSavedItemsViewModel.getAllItems();
//
//        all_locations.observe(this, new Observer<List<City>>() {
//            @Override
//            public void onChanged(@Nullable List<City> cities) {
//                mSavedItemsAdapter.updateCities(cities);
//            }
//        });
//        mSavedItemsViewModel.getAllItems().observe(this, new Observer<City>() {
//            @Override
//            public void onChanged(@Nullable City city) {
//                mSavedItemsViewModel.insertSavedItem(city);
//            }
//        });



        /*
         * Attach an Observer to the network loading status.  Whenever the loading status changes,
         * this Observer will ensure that the correct layout components are visible.  Specifically,
         * it will make the loading indicator visible only when the forecast is being loaded.
         * Otherwise, it will display the RecyclerView if forecast data was successfully fetched,
         * or it will display the error message if there was an error fetching data.
         */
        mForecastViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(@Nullable Status status) {
                if (status == Status.LOADING) {
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                } else if (status == Status.SUCCESS) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                    mForecastItemsRV.setVisibility(View.VISIBLE);
                } else {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mForecastItemsRV.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        loadForecast(preferences);

    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onForecastItemClick(ForecastItem forecastItem) {
        Intent intent = new Intent(this, ForecastItemDetailActivity.class);
        intent.putExtra(OpenWeatherMapUtils.EXTRA_FORECAST_ITEM, forecastItem);
        startActivity(intent);
    }

    // update query and close drawer
    @Override
    public void onCityClick(City city) {
        // get the shared preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // shared preferences editor
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pref_location",city.city);
        editor.apply();

        mDrawerLayout.closeDrawer(GravityCompat.START); // CLOSE NAV DRAWER
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // OPEN DRAWER
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // CLICKS ON MENU BUTTON - open the menu
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_location:
                showForecastLocationInMap();
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadForecast(SharedPreferences preferences) {
        String location = preferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default_value)
        );
        String units = preferences.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_default_value)
        );

        mForecastLocationTV.setText(location);
        mForecastViewModel.loadForecast(location, units);
    }

    public void showForecastLocationInMap() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String forecastLocation = sharedPreferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default_value)
        );
        Uri geoUri = Uri.parse("geo:0,0").buildUpon()
                .appendQueryParameter("q", forecastLocation)
                .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    // all good
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        loadForecast(sharedPreferences);
    }
}
