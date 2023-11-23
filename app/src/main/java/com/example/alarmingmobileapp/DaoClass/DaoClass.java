package com.example.alarmingmobileapp.DaoClass;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.alarmingmobileapp.Models.MarkerModel;

import java.util.List;


@Dao
public interface DaoClass {

    @Insert
    void insertAllData(MarkerModel marker);

    @Delete
    void deleteMarker(MarkerModel marker);

    @Query("select * from markers")
    List<MarkerModel> getAllData();

}
