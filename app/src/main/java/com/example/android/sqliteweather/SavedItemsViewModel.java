package com.example.android.sqliteweather;


import android.app.Application;

import com.example.android.sqliteweather.data.City;
import com.example.android.sqliteweather.data.SavedItemsRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SavedItemsViewModel extends AndroidViewModel {
    private SavedItemsRepository mRepository;

    public SavedItemsViewModel(Application application) {
        super(application);
        mRepository = new SavedItemsRepository(application);
    }

    // INSERT
    public void insertSavedItem(City item) {
        mRepository.insertSavedItem(item);
    }

    // DELETE
    public void deleteSavedItem(City item) {
        mRepository.deleteSavedItem(item);
    }

    // GET ALL ITEMS (QUERY)
    public LiveData<List<City>> getAllItems() {
        return mRepository.getAllItems();
    }

    public LiveData<City> getItemByName(String fullName) {
        return mRepository.getItemByName(fullName);
    }
}
