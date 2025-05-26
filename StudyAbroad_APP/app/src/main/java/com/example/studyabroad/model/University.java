package com.example.studyabroad.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class University {
    private String id;
    private String name;
    private String country;
    private String city;
    private String description;
    private int foundedYear;
    private int ranking;
    private String websiteUrl;
    private String imageUrl;
    private int studentCount;
    private double internationalStudentPercentage;
    private List<String> availableDepartments;
    private List<String> availableDegrees; // Bachelor's, Master's, PhD
    private Map<String, Double> admissionRates; // By degree type
    private Map<String, String> contactInfo; // Email, phone, etc.
    private Map<String, Object> facilities; // Campus facilities, libraries, etc.
    private double averageTuitionFee;
    private double averageLivingCost;
    private List<String> programIds; // IDs of programs offered by this university
    private Map<String, Object> additionalInfo; // For storing additional/temporary data
    
    // Empty constructor for Firebase
    public University() {
        availableDepartments = new ArrayList<>();
        availableDegrees = new ArrayList<>();
        admissionRates = new HashMap<>();
        contactInfo = new HashMap<>();
        facilities = new HashMap<>();
        programIds = new ArrayList<>();
        additionalInfo = new HashMap<>();
    }
    
    public University(String id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
        availableDepartments = new ArrayList<>();
        availableDegrees = new ArrayList<>();
        admissionRates = new HashMap<>();
        contactInfo = new HashMap<>();
        facilities = new HashMap<>();
        programIds = new ArrayList<>();
        additionalInfo = new HashMap<>();
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getFoundedYear() {
        return foundedYear;
    }
    
    public void setFoundedYear(int foundedYear) {
        this.foundedYear = foundedYear;
    }
    
    public int getRanking() {
        return ranking;
    }
    
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
    
    public String getWebsiteUrl() {
        return websiteUrl;
    }
    
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public int getStudentCount() {
        return studentCount;
    }
    
    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }
    
    public double getInternationalStudentPercentage() {
        return internationalStudentPercentage;
    }
    
    public void setInternationalStudentPercentage(double internationalStudentPercentage) {
        this.internationalStudentPercentage = internationalStudentPercentage;
    }
    
    public List<String> getAvailableDepartments() {
        return availableDepartments;
    }
    
    public void setAvailableDepartments(List<String> availableDepartments) {
        this.availableDepartments = availableDepartments;
    }
    
    public void addDepartment(String department) {
        if (!this.availableDepartments.contains(department)) {
            this.availableDepartments.add(department);
        }
    }
    
    public List<String> getAvailableDegrees() {
        return availableDegrees;
    }
    
    public void setAvailableDegrees(List<String> availableDegrees) {
        this.availableDegrees = availableDegrees;
    }
    
    public void addDegree(String degree) {
        if (!this.availableDegrees.contains(degree)) {
            this.availableDegrees.add(degree);
        }
    }
    
    public Map<String, Double> getAdmissionRates() {
        return admissionRates;
    }
    
    public void setAdmissionRates(Map<String, Double> admissionRates) {
        this.admissionRates = admissionRates;
    }
    
    public void setAdmissionRate(String degreeType, double rate) {
        this.admissionRates.put(degreeType, rate);
    }
    
    public Double getAdmissionRate(String degreeType) {
        return this.admissionRates.get(degreeType);
    }
    
    public Map<String, String> getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(Map<String, String> contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public void addContactInfo(String type, String value) {
        this.contactInfo.put(type, value);
    }
    
    public Map<String, Object> getFacilities() {
        return facilities;
    }
    
    public void setFacilities(Map<String, Object> facilities) {
        this.facilities = facilities;
    }
    
    public void addFacility(String name, Object details) {
        this.facilities.put(name, details);
    }
    
    public double getAverageTuitionFee() {
        return averageTuitionFee;
    }
    
    public void setAverageTuitionFee(double averageTuitionFee) {
        this.averageTuitionFee = averageTuitionFee;
    }
    
    public double getAverageLivingCost() {
        return averageLivingCost;
    }
    
    public void setAverageLivingCost(double averageLivingCost) {
        this.averageLivingCost = averageLivingCost;
    }
    
    public List<String> getProgramIds() {
        return programIds;
    }
    
    public void setProgramIds(List<String> programIds) {
        this.programIds = programIds;
    }
    
    public void addProgramId(String programId) {
        if (!this.programIds.contains(programId)) {
            this.programIds.add(programId);
        }
    }
    
    public void removeProgramId(String programId) {
        this.programIds.remove(programId);
    }
    
    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }
    
    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    
    public void addAdditionalInfo(String key, Object value) {
        if (this.additionalInfo == null) {
            this.additionalInfo = new HashMap<>();
        }
        this.additionalInfo.put(key, value);
    }
    
    public Object getAdditionalInfo(String key) {
        if (this.additionalInfo == null) {
            return null;
        }
        return this.additionalInfo.get(key);
    }
    
    // Helper methods
    public String getFormattedRanking() {
        return "Ranked #" + ranking + " globally";
    }
    
    public String getFormattedTuition() {
        return String.format("$%,.0f per year", averageTuitionFee);
    }
    
    public String getFormattedLivingCost() {
        return String.format("$%,.0f per year", averageLivingCost);
    }
    
    public String getLocation() {
        return city + ", " + country;
    }
    
    public int calculateTotalAnnualCost() {
        return (int) (averageTuitionFee + averageLivingCost);
    }
    
    public String getFormattedTotalCost() {
        return String.format("$%,d per year", calculateTotalAnnualCost());
    }
} 