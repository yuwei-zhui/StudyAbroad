package com.example.studyabroad.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studyabroad.database.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);
    
    @Update
    void update(User user);
    
    @Delete
    void delete(User user);
    
    @Query("SELECT * FROM users WHERE id = :id")
    LiveData<User> getUserById(long id);
    
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    User login(String email, String password);
    
    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);
    
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    boolean checkEmailExists(String email);
    
    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();
} 