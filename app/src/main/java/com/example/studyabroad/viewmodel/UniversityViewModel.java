package com.example.studyabroad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyabroad.database.entity.University;
import com.example.studyabroad.repository.UniversityRepository;

import java.util.List;

public class UniversityViewModel extends AndroidViewModel {
    
    private UniversityRepository repository;
    private LiveData<List<University>> allUniversities;
    private MutableLiveData<University> selectedUniversity = new MutableLiveData<>();
    
    public UniversityViewModel(@NonNull Application application) {
        super(application);
        repository = new UniversityRepository(application);
        allUniversities = repository.getAllUniversities();
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
    
    public LiveData<List<University>> getUniversitiesByRanking(int maxRanking) {
        return repository.getUniversitiesByRanking(maxRanking);
    }
    
    public LiveData<List<University>> getUniversitiesByCountry(String country) {
        return repository.getUniversitiesByCountry(country);
    }
    
    public LiveData<List<University>> getUniversitiesByCountryAndRanking(String country, int maxRanking) {
        return repository.getUniversitiesByCountryAndRanking(country, maxRanking);
    }
    
    public LiveData<List<University>> getUniversitiesByMajor(String major) {
        return repository.getUniversitiesByMajor(major);
    }
    
    public LiveData<List<University>> getUniversitiesByRankingAndFee(int maxRanking, double maxFee) {
        return repository.getUniversitiesByRankingAndFee(maxRanking, maxFee);
    }
    
    // Selected university methods
    public LiveData<University> getSelectedUniversity() {
        return selectedUniversity;
    }
    
    public void selectUniversity(University university) {
        selectedUniversity.setValue(university);
    }
} 