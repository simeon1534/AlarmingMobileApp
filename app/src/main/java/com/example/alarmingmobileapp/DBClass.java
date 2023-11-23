package com.example.alarmingmobileapp;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import androidx.room.Database;

import com.example.alarmingmobileapp.DaoClass.DaoClass;
import com.example.alarmingmobileapp.Models.MarkerModel;



@Database(entities ={MarkerModel.class},version = 1)
public abstract class DBClass extends RoomDatabase {

    public abstract DaoClass getDao();
    private static DBClass instance;

    static DBClass getDatabase(final Context context){
        if(instance==null){
            synchronized (DBClass.class){
                instance= Room.databaseBuilder(context,DBClass.class,"MarkersDB").allowMainThreadQueries().build();
            }
        }
        return instance;
    }
}
