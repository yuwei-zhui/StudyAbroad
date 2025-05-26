package com.example.studyabroad.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studyabroad.database.AppDatabase;
import com.example.studyabroad.database.dao.ProgramDao;
import com.example.studyabroad.database.entity.Program;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProgramRepository {
    
    private final ProgramDao programDao;
    private final ExecutorService executorService;
    
    public ProgramRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        programDao = database.programDao();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    // CRUD operations
    public void insert(Program program) {
        executorService.execute(() -> programDao.insert(program));
    }
    
    public void insertAll(List<Program> programs) {
        executorService.execute(() -> programDao.insertAll(programs));
    }
    
    public void update(Program program) {
        executorService.execute(() -> programDao.update(program));
    }
    
    public void delete(Program program) {
        executorService.execute(() -> programDao.delete(program));
    }
    
    // Basic queries
    public LiveData<Program> getProgramById(long id) {
        return programDao.getProgramById(id);
    }
    
    public LiveData<List<Program>> getAllPrograms() {
        return programDao.getAllPrograms();
    }
    
    public LiveData<List<Program>> getProgramsByUniversity(long universityId) {
        return programDao.getProgramsByUniversity(universityId);
    }
    
    // Search and filter methods
    public LiveData<List<Program>> getProgramsByCategory(String category) {
        return programDao.getProgramsByCategory(category);
    }
    
    public LiveData<List<Program>> getProgramsByDegreeLevel(String degreeLevel) {
        return programDao.getProgramsByDegreeLevel(degreeLevel);
    }
    
    public LiveData<List<Program>> getProgramsByMaxTuition(double maxFee) {
        return programDao.getProgramsByMaxTuition(maxFee);
    }
    
    public LiveData<List<Program>> getProgramsByCategoryAndDegree(String category, String degreeLevel) {
        return programDao.getProgramsByCategoryAndDegree(category, degreeLevel);
    }
    
    public LiveData<List<Program>> getProgramsByEligibility(double userGPA) {
        return programDao.getProgramsByEligibility(userGPA);
    }
    
    public LiveData<List<Program>> searchPrograms(String query) {
        return programDao.searchPrograms(query);
    }
    
    public LiveData<List<Program>> getRecommendedPrograms(
            String category, 
            String degreeLevel, 
            double userGPA, 
            List<String> preferredCountries) {
        return programDao.getRecommendedPrograms(category, degreeLevel, userGPA, preferredCountries);
    }
    
    public LiveData<List<Program>> getProgramsWithScholarship() {
        return programDao.getProgramsWithScholarship();
    }
    
    public LiveData<List<String>> getAllCategories() {
        return programDao.getAllCategories();
    }
    
    public LiveData<List<String>> getAllDegreeLevels() {
        return programDao.getAllDegreeLevels();
    }
    
    public LiveData<List<Program>> searchProgramsComprehensive(String query) {
        return programDao.searchProgramsComprehensive(query);
    }
    
    public LiveData<List<Program>> advancedSearch(
            List<String> countries,
            String category,
            String degreeLevel,
            double maxFee,
            int minRanking,
            int maxRanking) {
        return programDao.advancedSearch(countries, category, degreeLevel, maxFee, minRanking, maxRanking);
    }
} 