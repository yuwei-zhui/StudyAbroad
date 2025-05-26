package com.example.studyabroad;

import android.app.Application;
import android.util.Log;

import com.example.studyabroad.database.AppDatabase;
import com.example.studyabroad.utils.DatabaseSeeder;

public class StudyAbroadApp extends Application {
    
    private static final String TAG = "StudyAbroadApp";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        AppDatabase database = AppDatabase.getInstance(this);
        
        DatabaseSeeder.seedDatabase(this);
        
        Log.d(TAG, "StudyAbroad application initialized");
    }
} 