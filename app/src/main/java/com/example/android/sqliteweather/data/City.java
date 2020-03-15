package com.example.android.sqliteweather.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

// ENTITY
@Entity(tableName = "items")
public class City implements Serializable {
    @PrimaryKey
    @NonNull
    public String city;

    public City(){}

    public City(String city_input){
        city = city_input;
    }

}
