package com.example.studyabroad.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.List;

@Entity(tableName = "programs", 
        foreignKeys = @ForeignKey(
            entity = University.class,
            parentColumns = "id",
            childColumns = "universityId",
            onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("universityId")})
public class Program {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private long universityId;
    private String name;
    private String category; // Business, Engineering, Computer Science, etc.
    private String degreeLevel; // Bachelor, Master, PhD
    private String duration;
    private String mode; // On campus, online, hybrid
    private String location;
    private String intakeDate;
    private String applicationDeadline;
    private double tuitionFee;
    private String currency;
    private String language;
    private String description;
    private double minGPA;
    private String entryRequirements;
    private String careerProspects;
    private boolean isScholarshipAvailable;
    private String facultyName;
    private String programUrl;
    
    // Constructor
    public Program() {
    }
    
    // Getter and Setter methods
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getUniversityId() {
        return universityId;
    }
    
    public void setUniversityId(long universityId) {
        this.universityId = universityId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDegreeLevel() {
        return degreeLevel;
    }
    
    public void setDegreeLevel(String degreeLevel) {
        this.degreeLevel = degreeLevel;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public String getMode() {
        return mode;
    }
    
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getIntakeDate() {
        return intakeDate;
    }
    
    public void setIntakeDate(String intakeDate) {
        this.intakeDate = intakeDate;
    }
    
    public String getApplicationDeadline() {
        return applicationDeadline;
    }
    
    public void setApplicationDeadline(String applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }
    
    public double getTuitionFee() {
        return tuitionFee;
    }
    
    public void setTuitionFee(double tuitionFee) {
        this.tuitionFee = tuitionFee;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getMinGPA() {
        return minGPA;
    }
    
    public void setMinGPA(double minGPA) {
        this.minGPA = minGPA;
    }
    
    public String getEntryRequirements() {
        return entryRequirements;
    }
    
    public void setEntryRequirements(String entryRequirements) {
        this.entryRequirements = entryRequirements;
    }
    
    public String getCareerProspects() {
        return careerProspects;
    }
    
    public void setCareerProspects(String careerProspects) {
        this.careerProspects = careerProspects;
    }
    
    public boolean isScholarshipAvailable() {
        return isScholarshipAvailable;
    }
    
    public void setScholarshipAvailable(boolean scholarshipAvailable) {
        isScholarshipAvailable = scholarshipAvailable;
    }
    
    public String getFacultyName() {
        return facultyName;
    }
    
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
    
    public String getProgramUrl() {
        return programUrl;
    }
    
    public void setProgramUrl(String programUrl) {
        this.programUrl = programUrl;
    }
    
    @Override
    public String toString() {
        return "Program{" +
                "id=" + id +
                ", universityId=" + universityId +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", degreeLevel='" + degreeLevel + '\'' +
                '}';
    }
    
    public static List<String> getPopularCategories() {
        return Arrays.asList(
            "Computer Science",
            "Information Technology",
            "Business",
            "Engineering",
            "Medicine",
            "Law",
            "Arts",
            "Science",
            "Education",
            "Architecture"
        );
    }
} 