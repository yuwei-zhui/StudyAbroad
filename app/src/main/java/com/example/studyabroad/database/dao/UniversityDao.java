package com.example.studyabroad.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studyabroad.database.entity.University;

import java.util.List;

@Dao
public interface UniversityDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(University university);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<University> universities);
    
    @Update
    void update(University university);
    
    @Delete
    void delete(University university);
    
    @Query("SELECT * FROM universities WHERE id = :id")
    LiveData<University> getUniversityById(long id);
    
    @Query("SELECT * FROM universities")
    LiveData<List<University>> getAllUniversities();
    
    @Query("SELECT * FROM universities WHERE qsRanking <= :maxRanking")
    LiveData<List<University>> getUniversitiesByRanking(int maxRanking);
    
    @Query("SELECT * FROM universities WHERE country = :country")
    LiveData<List<University>> getUniversitiesByCountry(String country);
    
    @Query("SELECT * FROM universities WHERE country = :country AND qsRanking <= :maxRanking")
    LiveData<List<University>> getUniversitiesByCountryAndRanking(String country, int maxRanking);
    
    @Query("SELECT * FROM universities WHERE courseName LIKE '%' || :major || '%'")
    LiveData<List<University>> getUniversitiesByMajor(String major);
    
    @Query("SELECT * FROM universities WHERE qsRanking <= :maxRanking AND courseFee <= :maxFee")
    LiveData<List<University>> getUniversitiesByRankingAndFee(int maxRanking, double maxFee);
} 