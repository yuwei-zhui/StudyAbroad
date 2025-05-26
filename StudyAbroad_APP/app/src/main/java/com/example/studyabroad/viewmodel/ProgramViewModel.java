package com.example.studyabroad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyabroad.database.entity.Program;
import com.example.studyabroad.repositories.ProgramRepository;
import com.example.studyabroad.utils.UserProfileManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramViewModel extends AndroidViewModel {
    
    private final ProgramRepository repository;
    private final MutableLiveData<Program> selectedProgram = new MutableLiveData<>();
    private final UserProfileManager profileManager;
    
    public ProgramViewModel(@NonNull Application application) {
        super(application);
        repository = new ProgramRepository(application);
        profileManager = UserProfileManager.getInstance(application);
    }
    
    // CRUD operations
    public void insert(Program program) {
        repository.insert(program);
    }
    
    public void insertAll(List<Program> programs) {
        repository.insertAll(programs);
    }
    
    public void update(Program program) {
        repository.update(program);
    }
    
    public void delete(Program program) {
        repository.delete(program);
    }
    
    // Basic queries
    public LiveData<Program> getProgramById(long id) {
        return repository.getProgramById(id);
    }
    
    public LiveData<List<Program>> getAllPrograms() {
        return repository.getAllPrograms();
    }
    
    public LiveData<List<Program>> getProgramsByUniversity(long universityId) {
        return repository.getProgramsByUniversity(universityId);
    }
    
    // Search and filter methods
    public LiveData<List<Program>> getProgramsByCategory(String category) {
        return repository.getProgramsByCategory(category);
    }
    
    public LiveData<List<Program>> getProgramsByDegreeLevel(String degreeLevel) {
        return repository.getProgramsByDegreeLevel(degreeLevel);
    }
    
    public LiveData<List<Program>> getProgramsByMaxTuition(double maxFee) {
        return repository.getProgramsByMaxTuition(maxFee);
    }
    
    public LiveData<List<Program>> getProgramsByCategoryAndDegree(String category, String degreeLevel) {
        return repository.getProgramsByCategoryAndDegree(category, degreeLevel);
    }
    
    public LiveData<List<Program>> getProgramsByEligibility(double userGPA) {
        return repository.getProgramsByEligibility(userGPA);
    }
    
    public LiveData<List<Program>> searchPrograms(String query) {
        return repository.searchPrograms(query);
    }
    
    public LiveData<List<Program>> getRecommendedProgramsForCurrentUser() {
        String major = profileManager.getMajor();
        String degree = profileManager.getDegree();
        String gpaStr = profileManager.getGPA();
        double gpa = 0.0;
        
        try {
            if (gpaStr != null && !gpaStr.isEmpty()) {
                gpa = Double.parseDouble(gpaStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        String preferredCountry = profileManager.getPreferredCountry();
        List<String> preferredCountries = new ArrayList<>();
        
        if (preferredCountry != null && !preferredCountry.isEmpty()) {
            preferredCountries.add(preferredCountry);
        } else {
            preferredCountries = Arrays.asList("United States", "United Kingdom", "Australia", "Canada");
        }
        
        return repository.getRecommendedPrograms(major, degree, gpa, preferredCountries);
    }
    
    public LiveData<List<Program>> getRecommendedPrograms(
            String category, 
            String degreeLevel, 
            double userGPA, 
            List<String> preferredCountries) {
        return repository.getRecommendedPrograms(category, degreeLevel, userGPA, preferredCountries);
    }
    
    public LiveData<List<Program>> getProgramsWithScholarship() {
        return repository.getProgramsWithScholarship();
    }
    
    public LiveData<List<String>> getAllCategories() {
        return repository.getAllCategories();
    }
    
    public LiveData<List<String>> getAllDegreeLevels() {
        return repository.getAllDegreeLevels();
    }
    
    public LiveData<List<Program>> searchProgramsComprehensive(String query) {
        return repository.searchProgramsComprehensive(query);
    }
    
    public LiveData<List<Program>> advancedSearch(
            List<String> countries,
            String category,
            String degreeLevel,
            double maxFee,
            int minRanking,
            int maxRanking) {
        return repository.advancedSearch(countries, category, degreeLevel, maxFee, minRanking, maxRanking);
    }
    
    // Selected program methods
    public LiveData<Program> getSelectedProgram() {
        return selectedProgram;
    }
    
    public void selectProgram(Program program) {
        selectedProgram.setValue(program);
    }
} 