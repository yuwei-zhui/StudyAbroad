package com.example.studyabroad.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studyabroad.database.AppDatabase;
import com.example.studyabroad.database.dao.UniversityDao;
import com.example.studyabroad.database.entity.University;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UniversityRepository {
    
    private UniversityDao universityDao;
    private LiveData<List<University>> allUniversities;
    private ExecutorService executorService;
    
    public UniversityRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        universityDao = database.universityDao();
        allUniversities = universityDao.getAllUniversities();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    public void insert(University university) {
        executorService.execute(() -> {
            universityDao.insert(university);
        });
    }
    
    public void insertAll(List<University> universities) {
        executorService.execute(() -> {
            universityDao.insertAll(universities);
        });
    }
    
    public void update(University university) {
        executorService.execute(() -> {
            universityDao.update(university);
        });
    }
    
    public void delete(University university) {
        executorService.execute(() -> {
            universityDao.delete(university);
        });
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
    
    public LiveData<List<University>> getUniversitiesByCountryAndRanking(String country, int maxRanking) {
        return universityDao.getUniversitiesByCountryAndRanking(country, maxRanking);
    }
    
    public LiveData<List<University>> getUniversitiesByMajor(String major) {
        return universityDao.getUniversitiesByMajor(major);
    }
    
    public LiveData<List<University>> getUniversitiesByRankingAndFee(int maxRanking, double maxFee) {
        return universityDao.getUniversitiesByRankingAndFee(maxRanking, maxFee);
    }
} 