package com.example.studyabroad.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.studyabroad.database.dao.ProgramDao;
import com.example.studyabroad.database.dao.UniversityDao;
import com.example.studyabroad.database.dao.UserDao;
import com.example.studyabroad.database.dao.UserPreferenceDao;
import com.example.studyabroad.database.entity.Program;
import com.example.studyabroad.database.entity.University;
import com.example.studyabroad.database.entity.User;
import com.example.studyabroad.database.entity.UserPreference;
import com.example.studyabroad.utils.Converters;

@Database(entities = {User.class, University.class, UserPreference.class, Program.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "studyabroad_db";
    private static AppDatabase instance;
    
    public abstract UserDao userDao();
    public abstract UniversityDao universityDao();
    public abstract UserPreferenceDao userPreferenceDao();
    public abstract ProgramDao programDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
} 