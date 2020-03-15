package com.example.android.sqliteweather.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SavedItemsDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(City item);

    @Delete
    void delete(City item);

    // wrap in LiveData object
    // when updates are made to the db, the Room persistence library will automatically
    //   update the data held in the LiveData object here
    // we can attach an observer to the LiveData object to listen for changes
    // returning a LiveData implicitly turns this method into an asynchronous call
    @Query("SELECT * FROM items")
    LiveData<List<City>> getAllItems();

    @Query("SELECT * FROM items WHERE city = :fullName LIMIT 1")
    LiveData<City> getItemByName(String fullName);
}
