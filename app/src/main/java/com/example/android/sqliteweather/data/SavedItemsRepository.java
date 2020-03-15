package com.example.android.sqliteweather.data;


import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class SavedItemsRepository {
    private SavedItemsDao mDAO;

    // constructor - get reference to singleton db instance
    public SavedItemsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mDAO = db.savedItemsDao();
    }

    // INSERT
    public void insertSavedItem(City item) {
        new InsertAsyncTask(mDAO).execute(item);
    }

    // DELETE
    public void deleteSavedItem(City item) {
        new DeleteAsyncTask(mDAO).execute(item);
    }

    // GET ALL REPOS (QUERY)
    public LiveData<List<City>> getAllItems() {
        return mDAO.getAllItems();
    }

    public LiveData<City> getItemByName(String fullName) {
        return mDAO.getItemByName(fullName);
    }

    // db operations cannot be run in the main thread
    private static class InsertAsyncTask extends AsyncTask<City, Void, Void> {
        private SavedItemsDao mAsyncTaskDAO;
        InsertAsyncTask(SavedItemsDao dao) {
            mAsyncTaskDAO = dao;
        }

        @Override
        protected Void doInBackground(City... cities) {
            mAsyncTaskDAO.insert(cities[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<City, Void, Void> {
        private SavedItemsDao mAsyncTaskDAO;
        DeleteAsyncTask(SavedItemsDao dao) {
            mAsyncTaskDAO = dao;
        }

        @Override
        protected Void doInBackground(City... cities) {
            mAsyncTaskDAO.delete(cities[0]);
            return null;
        }
    }
}
