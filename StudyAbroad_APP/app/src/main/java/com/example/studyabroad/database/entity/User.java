package com.example.studyabroad.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String name;
    private String email;
    private String password;
    private String dateOfBirth;
    private String nationality;
    private String phoneNumber;
    private String profilePicturePath;
    private String currentInstitution;
    private String major;
    private double gpa;
    private String targetDegreeLevel;
    private String preferredCountries;
    private String qsRanking;
    
    // Constructors
    public User() {
    }
    
    @Ignore
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getProfilePicturePath() {
        return profilePicturePath;
    }
    
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
    
    public String getCurrentInstitution() {
        return currentInstitution;
    }
    
    public void setCurrentInstitution(String currentInstitution) {
        this.currentInstitution = currentInstitution;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public double getGpa() {
        return gpa;
    }
    
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
    
    public String getTargetDegreeLevel() {
        return targetDegreeLevel;
    }
    
    public void setTargetDegreeLevel(String targetDegreeLevel) {
        this.targetDegreeLevel = targetDegreeLevel;
    }
    
    public String getPreferredCountries() {
        return preferredCountries;
    }
    
    public void setPreferredCountries(String preferredCountries) {
        this.preferredCountries = preferredCountries;
    }
    
    public String getQsRanking() {
        return qsRanking;
    }
    
    public void setQsRanking(String qsRanking) {
        this.qsRanking = qsRanking;
    }
} 