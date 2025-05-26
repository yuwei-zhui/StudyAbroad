package com.example.studyabroad.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;
import androidx.room.RoomWarnings;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.studyabroad.database.entity.Program;

import java.util.List;

@Dao
public interface ProgramDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Program program);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<Program> programs);
    
    @Update
    void update(Program program);
    
    @Delete
    void delete(Program program);
    
    @Query("DELETE FROM programs")
    void deleteAll();
    
    @Query("SELECT * FROM programs WHERE id = :id")
    LiveData<Program> getProgramById(long id);
    
    @Query("SELECT * FROM programs WHERE id = :id")
    Program getProgramByIdSync(long id);
    
    @Query("SELECT * FROM programs")
    LiveData<List<Program>> getAllPrograms();
    
    @Query("SELECT * FROM programs WHERE universityId = :universityId")
    LiveData<List<Program>> getProgramsByUniversity(long universityId);
    
    @Query("SELECT * FROM programs WHERE category = :category")
    LiveData<List<Program>> getProgramsByCategory(String category);
    
    @Query("SELECT * FROM programs WHERE degreeLevel = :degreeLevel")
    LiveData<List<Program>> getProgramsByDegreeLevel(String degreeLevel);
    
    @Query("SELECT * FROM programs WHERE tuitionFee <= :maxFee")
    LiveData<List<Program>> getProgramsByMaxTuition(double maxFee);
    
    @Query("SELECT * FROM programs WHERE category = :category AND degreeLevel = :degreeLevel")
    LiveData<List<Program>> getProgramsByCategoryAndDegree(String category, String degreeLevel);
    
    @Query("SELECT * FROM programs WHERE minGPA <= :userGPA")
    LiveData<List<Program>> getProgramsByEligibility(double userGPA);
    
    @Query("SELECT * FROM programs WHERE " +
           "name LIKE '%' || :query || '%' OR " +
           "category LIKE '%' || :query || '%' OR " +
           "description LIKE '%' || :query || '%' OR " +
           "facultyName LIKE '%' || :query || '%'")
    LiveData<List<Program>> searchPrograms(String query);
    
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM programs p " +
           "INNER JOIN universities u ON p.universityId = u.id " +
           "WHERE p.category = :category " + 
           "AND p.degreeLevel = :degreeLevel " +
           "AND p.minGPA <= :userGPA " +
           "AND u.country IN (:preferredCountries)")
    LiveData<List<Program>> getRecommendedPrograms(
        String category, 
        String degreeLevel, 
        double userGPA,
        List<String> preferredCountries);
    
    @Query("SELECT * FROM programs WHERE isScholarshipAvailable = 1")
    LiveData<List<Program>> getProgramsWithScholarship();
    
    @Query("SELECT DISTINCT category FROM programs")
    LiveData<List<String>> getAllCategories();
    
    @Query("SELECT DISTINCT degreeLevel FROM programs")
    LiveData<List<String>> getAllDegreeLevels();
    
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM programs p " +
           "INNER JOIN universities u ON p.universityId = u.id " +
           "WHERE (u.name LIKE '%' || :query || '%' OR " +
           "u.country LIKE '%' || :query || '%' OR " +
           "u.city LIKE '%' || :query || '%' OR " +
           "p.name LIKE '%' || :query || '%' OR " +
           "p.category LIKE '%' || :query || '%') " +
           "ORDER BY u.qsRanking ASC")
    LiveData<List<Program>> searchProgramsComprehensive(String query);
    
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM programs p " +
           "INNER JOIN universities u ON p.universityId = u.id " +
           "WHERE u.country IN (:countries) " +
           "AND (:category = '' OR p.category = :category) " +
           "AND (:degreeLevel = '' OR p.degreeLevel = :degreeLevel) " +
           "AND p.tuitionFee <= :maxFee " +
           "AND (:minRanking = 0 OR u.qsRanking >= :minRanking) " +
           "AND (:maxRanking = 1000 OR u.qsRanking <= :maxRanking) " +
           "ORDER BY u.qsRanking ASC")
    LiveData<List<Program>> advancedSearch(
        List<String> countries,
        String category,
        String degreeLevel,
        double maxFee,
        int minRanking,
        int maxRanking
    );
} 