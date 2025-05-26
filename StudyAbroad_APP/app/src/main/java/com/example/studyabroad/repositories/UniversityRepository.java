package com.example.studyabroad.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studyabroad.database.AppDatabase;
import com.example.studyabroad.database.dao.ProgramDao;
import com.example.studyabroad.database.dao.UniversityDao;
import com.example.studyabroad.database.entity.University;
import com.example.studyabroad.models.UniversityWithPrograms;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * University data repository, provides data operations related to universities and programs
 */
public class UniversityRepository {
    
    private final UniversityDao universityDao;
    private final ProgramDao programDao;
    private final LiveData<List<University>> allUniversities;
    private final ExecutorService executorService;
    
    public UniversityRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        universityDao = database.universityDao();
        programDao = database.programDao();
        allUniversities = universityDao.getAllUniversities();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    public void insert(University university) {
        executorService.execute(() -> universityDao.insert(university));
    }
    
    public void insertAll(List<University> universities) {
        executorService.execute(() -> universityDao.insertAll(universities));
    }
    
    public void update(University university) {
        executorService.execute(() -> universityDao.update(university));
    }
    
    public void delete(University university) {
        executorService.execute(() -> universityDao.delete(university));
    }
    
    public LiveData<List<University>> getAllUniversities() {
        return allUniversities;
    }
    
    public LiveData<University> getUniversityById(long id) {
        return universityDao.getUniversityById(id);
    }
    
    public LiveData<List<University>> getUniversitiesByRanking(int maxRanking) {
        return universityDao.getUniversitiesByRanking(maxRanking);
    }
    
    public LiveData<List<University>> getUniversitiesByCountry(String country) {
        return universityDao.getUniversitiesByCountry(country);
    }
    
    public LiveData<List<University>> getUniversitiesByRegion(String region) {
        return universityDao.getUniversitiesByRegion(region);
    }
    
    public LiveData<List<University>> getUniversitiesByCountryAndRanking(String country, int maxRanking) {
        return universityDao.getUniversitiesByCountryAndRanking(country, maxRanking);
    }
    
    public LiveData<List<String>> getAllCountries() {
        return universityDao.getAllCountries();
    }
    
    public LiveData<List<String>> getAllRegions() {
        return universityDao.getAllRegions();
    }
    
    public LiveData<List<University>> searchUniversities(String query) {
        return universityDao.searchUniversities(query);
    }
    
    public LiveData<List<University>> getUniversitiesWithScholarships() {
        return universityDao.getUniversitiesWithScholarships();
    }
    
    public LiveData<List<University>> getUniversitiesByRankingRange(int minRanking, int maxRanking) {
        return universityDao.getUniversitiesByRankingRange(minRanking, maxRanking);
    }
    
    /**
     * Get university and its programs by specified ID
     */
    public LiveData<UniversityWithPrograms> getUniversityWithPrograms(long universityId) {
        return universityDao.getUniversityWithPrograms(universityId);
    }
    
    /**
     * Get all universities and their programs
     */
    public LiveData<List<UniversityWithPrograms>> getAllUniversitiesWithPrograms() {
        return universityDao.getAllUniversitiesWithPrograms();
    }
    
    /**
     * Search universities and their programs
     */
    public LiveData<List<UniversityWithPrograms>> searchUniversitiesWithPrograms(String query) {
        return universityDao.searchUniversitiesWithPrograms(query);
    }
} 