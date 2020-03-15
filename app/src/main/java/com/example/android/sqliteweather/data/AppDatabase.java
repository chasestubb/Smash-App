package com.example.android.sqliteweather.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {City.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedItemsDao savedItemsDao();
    private static volatile AppDatabase INSTANCE; // singleton

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            // make sure creation of the instance is thread-safe
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // concrete object from abstract AppDatabase class
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "forecast_items_db" // TODO don't think this name matters
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
