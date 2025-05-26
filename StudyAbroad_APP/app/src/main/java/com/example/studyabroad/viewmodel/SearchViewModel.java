package com.example.studyabroad.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.studyabroad.database.AppDatabase;
import com.example.studyabroad.database.dao.ProgramDao;
import com.example.studyabroad.database.dao.UniversityDao;
import com.example.studyabroad.database.entity.Program;
import com.example.studyabroad.model.University;
import com.example.studyabroad.models.UniversityWithPrograms;
import com.example.studyabroad.repositories.UniversityRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel for search page, handles search and filter logic
 */
public class SearchViewModel extends AndroidViewModel {
    
    private static final String TAG = "SearchViewModel";
    private final UniversityRepository repository;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<SearchFilter> searchFilter = new MutableLiveData<>(new SearchFilter());
    
    private final LiveData<List<University>> universities;
    
    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new UniversityRepository(application);
        Log.d(TAG, "Initialize ViewModel and Repository");
        
        // Get university list based on search criteria
        universities = Transformations.switchMap(searchQuery, query -> {
            Log.d(TAG, "Execute search query: " + (query != null ? query : "empty"));
            if (query == null || query.isEmpty()) {
                Log.d(TAG, "Get all university data");
                return Transformations.map(repository.getAllUniversitiesWithPrograms(), this::mapToModelUniversities);
            } else {
                Log.d(TAG, "Search universities: " + query);
                return Transformations.map(repository.searchUniversitiesWithPrograms(query), this::mapToModelUniversities);
            }
        });
    }
    
    /**
     * Convert database entities to UI models
     */
    private List<University> mapToModelUniversities(List<UniversityWithPrograms> universityWithPrograms) {
        List<University> result = new ArrayList<>();
        
        Log.d(TAG, "Convert database results: " + (universityWithPrograms != null ? universityWithPrograms.size() : 0) + " universities");
        
        if (universityWithPrograms != null) {
            for (UniversityWithPrograms uwp : universityWithPrograms) {
                com.example.studyabroad.database.entity.University entity = uwp.university;
                
                Log.d(TAG, "Processing university: " + entity.getName() + ", ID: " + entity.getId() + ", programs: " + (uwp.programs != null ? uwp.programs.size() : 0));
                
                // Create model object
                University model = new University(
                        String.valueOf(entity.getId()),
                        entity.getName(),
                        entity.getCountry()
                );
                
                // Set other properties
                model.setCity(entity.getCity());
                model.setRanking(entity.getQsRanking());
                model.setWebsiteUrl(entity.getWebsite());
                model.setImageUrl(entity.getLogoUrl());
                
                // Add program list
                List<com.example.studyabroad.model.Program> programs = new ArrayList<>();
                if (uwp.programs != null) {
                    for (Program p : uwp.programs) {
                        com.example.studyabroad.model.Program programModel = new com.example.studyabroad.model.Program(
                                String.valueOf(p.getId()),
                                p.getName(),
                                String.valueOf(p.getUniversityId()),
                                p.getDegreeLevel(),
                                p.getCategory()
                        );
                        
                        // Set program details
                        programModel.setDurationYears(parseDurationYears(p.getDuration()));
                        programModel.setTuitionFeeInternational(p.getTuitionFee());
                        programModel.setCurrency(p.getCurrency());
                        programModel.setDescription(p.getDescription());
                        
                        programs.add(programModel);
                    }
                }
                
                model.setProgramIds(new ArrayList<>()); // Initialize as empty list
                
                // Add program object list to additional info
                if (!programs.isEmpty()) {
                    Map<String, Object> additionalInfo = new HashMap<>();
                    additionalInfo.put("programs", programs);
                    model.setAdditionalInfo(additionalInfo);
                }
                
                result.add(model);
            }
        }
        
        // Apply filter conditions
        List<University> filtered = applyFilters(result, searchFilter.getValue());
        Log.d(TAG, "Filtered results: " + filtered.size() + " universities");
        return filtered;
    }
    
    /**
     * Parse years from duration string
     */
    private int parseDurationYears(String duration) {
        if (duration == null || duration.isEmpty()) {
            return 0;
        }
        
        // Simple parsing, actual situation may need more complex logic
        if (duration.contains("1.5")) {
            return 1;
        } else if (duration.contains("2")) {
            return 2;
        } else if (duration.contains("3")) {
            return 3;
        } else if (duration.contains("4")) {
            return 4;
        } else if (duration.contains("1")) {
            return 1;
        }
        
        return 0;
    }
    
    /**
     * Apply filter conditions
     */
    private List<University> applyFilters(
            List<University> universities, 
            SearchFilter filter) {
        
        if (filter == null || universities == null) {
            return universities;
        }
        
        List<University> filtered = new ArrayList<>();
        
        for (University university : universities) {
            // Apply country filter
            if (filter.country != null && !filter.country.isEmpty() && 
                !filter.country.equals(university.getCountry())) {
                continue;
            }
            
            // Apply ranking filter
            if (university.getRanking() < filter.minRanking || 
                (filter.maxRanking > 0 && university.getRanking() > filter.maxRanking)) {
                continue;
            }
            
            filtered.add(university);
        }
        
        return filtered;
    }
    
    /**
     * Load all universities
     */
    public void loadAllUniversities() {
        Log.d(TAG, "Load all universities");
        searchQuery.setValue("");
    }
    
    /**
     * Search universities
     */
    public void searchUniversities(String query) {
        Log.d(TAG, "Search universities: " + query);
        searchQuery.setValue(query);
    }
    
    /**
     * Apply filter conditions
     */
    public void applyFilter(SearchFilter filter) {
        Log.d(TAG, "Apply filter conditions");
        searchFilter.setValue(filter);
        // Manually trigger re-search
        searchQuery.setValue(searchQuery.getValue());
    }
    
    /**
     * Get universities LiveData
     */
    public LiveData<List<University>> getUniversities() {
        return universities;
    }
    
    /**
     * Search filter conditions class
     */
    public static class SearchFilter {
        public String country;
        public String degreeLevel;
        public int minRanking;
        public int maxRanking;
        public double maxTuition;
        
        public SearchFilter() {
            this.country = "";
            this.degreeLevel = "";
            this.minRanking = 0;
            this.maxRanking = 0;
            this.maxTuition = 0;
        }
    }
} 