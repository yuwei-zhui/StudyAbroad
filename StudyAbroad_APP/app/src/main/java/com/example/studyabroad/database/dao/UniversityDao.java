package com.example.studyabroad.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.studyabroad.database.entity.University;
import com.example.studyabroad.models.UniversityWithPrograms;

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
    
    @Query("DELETE FROM universities")
    void deleteAll();
    
    @Query("SELECT * FROM universities WHERE id = :id")
    LiveData<University> getUniversityById(long id);
    
    @Query("SELECT * FROM universities")
    LiveData<List<University>> getAllUniversities();
    
    @Query("SELECT COUNT(*) FROM universities")
    int getUniversityCount();
    
    @Query("SELECT * FROM universities WHERE qsRanking <= :maxRanking ORDER BY qsRanking ASC")
    LiveData<List<University>> getUniversitiesByRanking(int maxRanking);
    
    @Query("SELECT * FROM universities WHERE country = :country")
    LiveData<List<University>> getUniversitiesByCountry(String country);
    
    @Query("SELECT * FROM universities WHERE region = :region")
    LiveData<List<University>> getUniversitiesByRegion(String region);
    
    @Query("SELECT * FROM universities WHERE country = :country AND qsRanking <= :maxRanking ORDER BY qsRanking ASC")
    LiveData<List<University>> getUniversitiesByCountryAndRanking(String country, int maxRanking);
    
    @Query("SELECT DISTINCT country FROM universities ORDER BY country ASC")
    LiveData<List<String>> getAllCountries();
    
    @Query("SELECT DISTINCT region FROM universities WHERE region IS NOT NULL ORDER BY region ASC")
    LiveData<List<String>> getAllRegions();
    
    @Query("SELECT * FROM universities WHERE " +
           "name LIKE '%' || :query || '%' OR " +
           "country LIKE '%' || :query || '%' OR " +
           "city LIKE '%' || :query || '%' OR " +
           "region LIKE '%' || :query || '%' " +
           "ORDER BY qsRanking ASC")
    LiveData<List<University>> searchUniversities(String query);
    
    @Query("SELECT * FROM universities WHERE hasScholarships = 1")
    LiveData<List<University>> getUniversitiesWithScholarships();
    
    @Query("SELECT * FROM universities WHERE qsRanking BETWEEN :minRanking AND :maxRanking ORDER BY qsRanking ASC")
    LiveData<List<University>> getUniversitiesByRankingRange(int minRanking, int maxRanking);
    
    @Transaction
    @Query("SELECT * FROM universities WHERE id = :id")
    LiveData<UniversityWithPrograms> getUniversityWithPrograms(long id);
    
    @Transaction
    @Query("SELECT * FROM universities")
    LiveData<List<UniversityWithPrograms>> getAllUniversitiesWithPrograms();
    
    @Transaction
    @Query("SELECT * FROM universities WHERE " +
           "name LIKE '%' || :query || '%' OR " +
           "country LIKE '%' || :query || '%' OR " +
           "city LIKE '%' || :query || '%' OR " +
           "region LIKE '%' || :query || '%' " +
           "ORDER BY qsRanking ASC")
    LiveData<List<UniversityWithPrograms>> searchUniversitiesWithPrograms(String query);
} 