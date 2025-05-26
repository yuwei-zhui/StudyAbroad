package com.example.studyabroad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyabroad.database.entity.University;
import com.example.studyabroad.models.UniversityWithPrograms;
import com.example.studyabroad.repositories.UniversityRepository;
import com.example.studyabroad.utils.UserProfileManager;

import java.util.List;

public class UniversityViewModel extends AndroidViewModel {
    
    private final UniversityRepository repository;
    private final LiveData<List<University>> allUniversities;
    private final MutableLiveData<University> selectedUniversity = new MutableLiveData<>();
    private final UserProfileManager profileManager;
    
    public UniversityViewModel(@NonNull Application application) {
        super(application);
        repository = new UniversityRepository(application);
        allUniversities = repository.getAllUniversities();
        profileManager = UserProfileManager.getInstance(application);
    }
    
    // CRUD operations
    public void insert(University university) {
        repository.insert(university);
    }
    
    public void insertAll(List<University> universities) {
        repository.insertAll(universities);
    }
    
    public void update(University university) {
        repository.update(university);
    }
    
    public void delete(University university) {
        repository.delete(university);
    }
    
    // University filtering methods
    public LiveData<List<University>> getAllUniversities() {
        return allUniversities;
    }
    
    public LiveData<University> getUniversityById(long id) {
        return repository.getUniversityById(id);
    }
    
    // Search and filter methods
    public LiveData<List<University>> getUniversitiesByRanking(int maxRanking) {
        return repository.getUniversitiesByRanking(maxRanking);
    }
    
    public LiveData<List<University>> getUniversitiesByCountry(String country) {
        return repository.getUniversitiesByCountry(country);
    }
    
    public LiveData<List<University>> getUniversitiesByRegion(String region) {
        return repository.getUniversitiesByRegion(region);
    }
    
    public LiveData<List<University>> getUniversitiesByCountryAndRanking(String country, int maxRanking) {
        return repository.getUniversitiesByCountryAndRanking(country, maxRanking);
    }
    
    public LiveData<List<String>> getAllCountries() {
        return repository.getAllCountries();
    }
    
    public LiveData<List<String>> getAllRegions() {
        return repository.getAllRegions();
    }
    
    public LiveData<List<University>> searchUniversities(String query) {
        return repository.searchUniversities(query);
    }
    
    public LiveData<List<University>> getUniversitiesWithScholarships() {
        return repository.getUniversitiesWithScholarships();
    }
    
    public LiveData<List<University>> getUniversitiesByRankingRange(int minRanking, int maxRanking) {
        return repository.getUniversitiesByRankingRange(minRanking, maxRanking);
    }
    
    // Related to programs
    public LiveData<UniversityWithPrograms> getUniversityWithPrograms(long id) {
        return repository.getUniversityWithPrograms(id);
    }
    
    public LiveData<List<UniversityWithPrograms>> getAllUniversitiesWithPrograms() {
        return repository.getAllUniversitiesWithPrograms();
    }
    
    public LiveData<List<UniversityWithPrograms>> searchUniversitiesWithPrograms(String query) {
        return repository.searchUniversitiesWithPrograms(query);
    }
    
    // Selected university methods
    public LiveData<University> getSelectedUniversity() {
        return selectedUniversity;
    }
    
    public void selectUniversity(University university) {
        selectedUniversity.setValue(university);
    }
    
    // Recommendation method based on user profile
    public LiveData<List<University>> getRecommendedUniversities() {
        String preferredCountry = profileManager.getPreferredCountry();
        String rankingFrom = profileManager.getRankingFrom();
        String rankingTo = profileManager.getRankingTo();
        
        int minRanking = 0;
        int maxRanking = 1000;
        
        try {
            if (rankingFrom != null && !rankingFrom.isEmpty()) {
                minRanking = Integer.parseInt(rankingFrom);
            }
            if (rankingTo != null && !rankingTo.isEmpty()) {
                maxRanking = Integer.parseInt(rankingTo);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        if (preferredCountry != null && !preferredCountry.isEmpty()) {
            return repository.getUniversitiesByCountryAndRanking(preferredCountry, maxRanking);
        } else {
            return repository.getUniversitiesByRankingRange(minRanking, maxRanking);
        }
    }
}