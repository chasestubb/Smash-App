package com.example.android.sqliteweather;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SavedItemsViewModel mViewModel; // VIEW MODEL

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EditTextPreference locationPref = (EditTextPreference)findPreference(getString(R.string.pref_location_key));
        locationPref.setSummary(locationPref.getText());

        // VIEW MODEL
        mViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
        ).get(SavedItemsViewModel.class);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_location_key))) {
            EditTextPreference locationPref = (EditTextPreference)findPreference(key);
            locationPref.setSummary(locationPref.getText());
        }
    }

    @Override
    public void onResume() {
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
