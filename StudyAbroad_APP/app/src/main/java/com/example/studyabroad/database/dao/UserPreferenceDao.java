package com.example.studyabroad.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studyabroad.database.entity.UserPreference;

import java.util.List;

@Dao
public interface UserPreferenceDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(UserPreference userPreference);
    
    @Update
    void update(UserPreference userPreference);
    
    @Delete
    void delete(UserPreference userPreference);
    
    @Query("SELECT * FROM user_preferences WHERE id = :id")
    LiveData<UserPreference> getUserPreferenceById(long id);
    
    @Query("SELECT * FROM user_preferences WHERE userId = :userId")
    LiveData<UserPreference> getUserPreferenceByUserId(long userId);
    
    @Query("SELECT * FROM user_preferences")
    LiveData<List<UserPreference>> getAllUserPreferences();
} 