package com.example.studyabroad;

import android.app.Application;
import android.util.Log;

import com.example.studyabroad.database.AppDatabase;
import com.example.studyabroad.database.entity.University;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudyAbroadApp extends Application {
    
    private static final String TAG = "StudyAbroadApp";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize your database
        AppDatabase database = AppDatabase.getInstance(this);
        
        // Prepopulate the database with sample data if needed (for demo purposes)
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Check if we already have universities in the database
                if (database.universityDao().getAllUniversities().getValue() == null || 
                    database.universityDao().getAllUniversities().getValue().isEmpty()) {
                    
                    Log.d(TAG, "Prepopulating database with sample universities");
                    
                    // Add sample universities for demo
                    List<University> universities = getSampleUniversities();
                    database.universityDao().insertAll(universities);
                    
                    Log.d(TAG, "Sample data inserted successfully");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error prepopulating database", e);
            }
        });
    }
    
    private List<University> getSampleUniversities() {
        List<University> universities = new ArrayList<>();
        
        // University of Melbourne
        University melbourne = new University("The University of Melbourne", "Australia", 13);
        melbourne.setCity("Melbourne");
        melbourne.setWebsite("unimelb.edu.au");
        melbourne.setCourseName("Master of Computer Science");
        melbourne.setCourseDuration("2 years full time / 4 years part time");
        melbourne.setCourseLocation("On campus (Parkville)");
        melbourne.setIntakeDate("February intake");
        melbourne.setApplicationDueDate("31 August 2025");
        melbourne.setCourseFee(90250.0);
        melbourne.setEntryRequirements("Bachelor degree in a related field with a minimum GPA of 65%");
        universities.add(melbourne);
        
        // Imperial College London
        University imperial = new University("Imperial College London", "United Kingdom", 8);
        imperial.setCity("London");
        imperial.setWebsite("imperial.ac.uk");
        imperial.setCourseName("MSc Computing");
        imperial.setCourseDuration("1 year full time");
        imperial.setCourseLocation("On campus (South Kensington)");
        imperial.setIntakeDate("October intake");
        imperial.setApplicationDueDate("31 March 2025");
        imperial.setCourseFee(39950.0);
        imperial.setEntryRequirements("First class or 2:1 (upper second) UK honours degree in computing, mathematics or engineering");
        universities.add(imperial);
        
        // University of Hong Kong
        University hku = new University("The University of Hongkong", "Hong Kong", 21);
        hku.setCity("Hong Kong");
        hku.setWebsite("hku.hk");
        hku.setCourseName("Master of Data Science");
        hku.setCourseDuration("1 year full time / 2 years part time");
        hku.setCourseLocation("On campus");
        hku.setIntakeDate("September intake");
        hku.setApplicationDueDate("31 May 2025");
        hku.setCourseFee(42100.0);
        hku.setEntryRequirements("Bachelor's degree in computer science, statistics or a quantitative field");
        universities.add(hku);
        
        // University of Sydney
        University sydney = new University("The University of Sydney", "Australia", 19);
        sydney.setCity("Sydney");
        sydney.setWebsite("sydney.edu.au");
        sydney.setCourseName("Master of Data Science");
        sydney.setCourseDuration("1.5 years full time / 3 years part time");
        sydney.setCourseLocation("On campus (Camperdown)");
        sydney.setIntakeDate("February and July intakes");
        sydney.setApplicationDueDate("30 November 2024");
        sydney.setCourseFee(85000.0);
        sydney.setEntryRequirements("Bachelor's degree with a credit average (65% or above)");
        universities.add(sydney);
        
        return universities;
    }
} 