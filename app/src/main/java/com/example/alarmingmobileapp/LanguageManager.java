package com.example.alarmingmobileapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private Context context;
    private SharedPreferences sharedPreferences;
    public LanguageManager(Context ct){
        context=ct;
        sharedPreferences=context.getSharedPreferences("Language",Context.MODE_PRIVATE);
    }
    public void updateResource(String lang){
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Resources resources=context.getResources();
        Configuration configuration=resources.getConfiguration();
        configuration.locale=locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        setLanguage(lang);
    }

    public String getLanguage(){
        return sharedPreferences.getString("language","en");

    }

    public void setLanguage(String language){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("language",language);
        editor.commit();
    }
}
