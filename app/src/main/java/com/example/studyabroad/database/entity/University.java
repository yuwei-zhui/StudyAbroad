package com.example.studyabroad.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "universities")
public class University {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String name;
    private String country;
    private String city;
    private String website;
    private int qsRanking;
    private String logoUrl;
    
    // Course related information
    private String courseName;
    private String courseDuration;
    private String courseLocation;
    private String intakeDate;
    private String applicationDueDate;
    private double courseFee;
    private String entryRequirements;
    
    // Constructors
    public University() {
    }
    
    @Ignore
    public University(String name, String country, int qsRanking) {
        this.name = name;
        this.country = country;
        this.qsRanking = qsRanking;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public int getQsRanking() {
        return qsRanking;
    }
    
    public void setQsRanking(int qsRanking) {
        this.qsRanking = qsRanking;
    }
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getCourseDuration() {
        return courseDuration;
    }
    
    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }
    
    public String getCourseLocation() {
        return courseLocation;
    }
    
    public void setCourseLocation(String courseLocation) {
        this.courseLocation = courseLocation;
    }
    
    public String getIntakeDate() {
        return intakeDate;
    }
    
    public void setIntakeDate(String intakeDate) {
        this.intakeDate = intakeDate;
    }
    
    public String getApplicationDueDate() {
        return applicationDueDate;
    }
    
    public void setApplicationDueDate(String applicationDueDate) {
        this.applicationDueDate = applicationDueDate;
    }
    
    public double getCourseFee() {
        return courseFee;
    }
    
    public void setCourseFee(double courseFee) {
        this.courseFee = courseFee;
    }
    
    public String getEntryRequirements() {
        return entryRequirements;
    }
    
    public void setEntryRequirements(String entryRequirements) {
        this.entryRequirements = entryRequirements;
    }
} 