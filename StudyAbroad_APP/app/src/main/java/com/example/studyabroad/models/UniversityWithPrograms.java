package com.example.studyabroad.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.studyabroad.database.entity.Program;
import com.example.studyabroad.database.entity.University;

import java.util.List;
import java.util.ArrayList;

/**
 * University and programs association class, used for Room database one-to-many relationship queries
 */
public class UniversityWithPrograms {
    @Embedded
    public University university;
    
    @Relation(
        parentColumn = "id",
        entityColumn = "universityId"
    )
    public List<Program> programs;
    
    public UniversityWithPrograms() {
    }
    
    /**
     * Returns a short description of the university suitable for display in a list
     * @return A summary of the university
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(university.getName());
        
        if (university.getCity() != null && !university.getCity().isEmpty()) {
            sb.append(", ").append(university.getCity());
        }
        
        if (university.getCountry() != null && !university.getCountry().isEmpty()) {
            sb.append(", ").append(university.getCountry());
        }
        
        if (university.getQsRanking() > 0) {
            sb.append(" (QS Ranking: ").append(university.getQsRanking()).append(")");
        }
        
        return sb.toString();
    }
    
    /**
     * Returns the number of programs offered by this university
     * @return The number of programs
     */
    public int getProgramCount() {
        return programs != null ? programs.size() : 0;
    }
    
    /**
     * Check if the university has any program matching the provided category
     * @param category The program category to check
     * @return true if a matching program exists
     */
    public boolean hasProgramInCategory(String category) {
        if (programs == null || programs.isEmpty() || category == null || category.isEmpty()) {
            return false;
        }
        
        for (Program program : programs) {
            if (category.equals(program.getCategory())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if the university has any program matching the provided degree level
     * @param degreeLevel The degree level to check
     * @return true if a matching program exists
     */
    public boolean hasProgramWithDegreeLevel(String degreeLevel) {
        if (programs == null || programs.isEmpty() || degreeLevel == null || degreeLevel.isEmpty()) {
            return false;
        }
        
        for (Program program : programs) {
            if (degreeLevel.equals(program.getDegreeLevel())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get all programs matching the specified category
     * @param category The program category to filter by
     * @return A list of matching programs
     */
    public List<Program> getProgramsByCategory(String category) {
        if (programs == null || programs.isEmpty() || category == null || category.isEmpty()) {
            return programs;
        }
        
        List<Program> result = new ArrayList<>();
        for (Program program : programs) {
            if (category.equals(program.getCategory())) {
                result.add(program);
            }
        }
        
        return result;
    }
} 