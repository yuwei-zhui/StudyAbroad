package com.example.studyabroad.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {
    // Basic program information
    private String id;
    private String name;
    private String universityId;
    private String departmentId;
    private String degreeType; // "bachelor", "master", "phd", "certificate", "diploma"
    private String fieldOfStudy;
    private String description;
    private boolean isActive;
    
    // Duration and schedule
    private int durationMonths;
    private int durationYears;
    private String academicSchedule; // "semester", "quarter", "trimester", "year-round"
    private String studyFormat; // "full-time", "part-time", "flexible", "online", "hybrid"
    
    // Admissions details
    private List<Date> applicationDeadlines;
    private String admissionsEmail;
    private String admissionsPhone;
    private String admissionsWebsite;
    private Map<String, Object> admissionRequirements;
    
    // Financial information
    private double tuitionFeeLocal; // In local currency
    private double tuitionFeeInternational; // In local currency
    private String currency;
    private double applicationFee;
    private boolean hasScholarships;
    private List<Map<String, Object>> scholarships;
    
    // Academic details
    private int totalCredits;
    private List<String> availableSpecializations;
    private List<Map<String, Object>> curriculum; // Courses, modules, etc.
    private Map<String, Integer> testScoreRequirements; // e.g., "TOEFL": 80
    private double minimumGPA;
    
    // Career prospects
    private List<String> careerOpportunities;
    private double employmentRate; // Percentage of graduates employed within a year
    private double averageStartingSalary;
    
    // Additional information
    private String languageOfInstruction;
    private List<String> requiredDocuments;
    private List<String> programFeatures; // Special features of the program
    private Map<String, Object> additionalInfo;
    
    // Static program data (e.g., rankings, statistics)
    private int ranking;
    private int internationalStudentPercentage;
    private double studentSatisfactionRate;
    
    // Empty constructor for Firebase
    public Program() {
        applicationDeadlines = new ArrayList<>();
        admissionRequirements = new HashMap<>();
        scholarships = new ArrayList<>();
        availableSpecializations = new ArrayList<>();
        curriculum = new ArrayList<>();
        testScoreRequirements = new HashMap<>();
        careerOpportunities = new ArrayList<>();
        requiredDocuments = new ArrayList<>();
        programFeatures = new ArrayList<>();
        additionalInfo = new HashMap<>();
    }
    
    public Program(String id, String name, String universityId, String degreeType, String fieldOfStudy) {
        this.id = id;
        this.name = name;
        this.universityId = universityId;
        this.degreeType = degreeType;
        this.fieldOfStudy = fieldOfStudy;
        this.isActive = true;
        
        applicationDeadlines = new ArrayList<>();
        admissionRequirements = new HashMap<>();
        scholarships = new ArrayList<>();
        availableSpecializations = new ArrayList<>();
        curriculum = new ArrayList<>();
        testScoreRequirements = new HashMap<>();
        careerOpportunities = new ArrayList<>();
        requiredDocuments = new ArrayList<>();
        programFeatures = new ArrayList<>();
        additionalInfo = new HashMap<>();
    }
    
    // Getters and setters for basic program information
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
    
    public String getUniversityId() {
        return universityId;
    }
    
    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }
    
    public String getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getDegreeType() {
        return degreeType;
    }
    
    public void setDegreeType(String degreeType) {
        this.degreeType = degreeType;
    }
    
    public String getFieldOfStudy() {
        return fieldOfStudy;
    }
    
    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    // Getters and setters for duration and schedule
    public int getDurationMonths() {
        return durationMonths;
    }
    
    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }
    
    public int getDurationYears() {
        return durationYears;
    }
    
    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }
    
    public String getAcademicSchedule() {
        return academicSchedule;
    }
    
    public void setAcademicSchedule(String academicSchedule) {
        this.academicSchedule = academicSchedule;
    }
    
    public String getStudyFormat() {
        return studyFormat;
    }
    
    public void setStudyFormat(String studyFormat) {
        this.studyFormat = studyFormat;
    }
    
    // Getters and setters for admissions details
    public List<Date> getApplicationDeadlines() {
        return applicationDeadlines;
    }
    
    public void setApplicationDeadlines(List<Date> applicationDeadlines) {
        this.applicationDeadlines = applicationDeadlines;
    }
    
    public void addApplicationDeadline(Date deadline) {
        this.applicationDeadlines.add(deadline);
    }
    
    public String getAdmissionsEmail() {
        return admissionsEmail;
    }
    
    public void setAdmissionsEmail(String admissionsEmail) {
        this.admissionsEmail = admissionsEmail;
    }
    
    public String getAdmissionsPhone() {
        return admissionsPhone;
    }
    
    public void setAdmissionsPhone(String admissionsPhone) {
        this.admissionsPhone = admissionsPhone;
    }
    
    public String getAdmissionsWebsite() {
        return admissionsWebsite;
    }
    
    public void setAdmissionsWebsite(String admissionsWebsite) {
        this.admissionsWebsite = admissionsWebsite;
    }
    
    public Map<String, Object> getAdmissionRequirements() {
        return admissionRequirements;
    }
    
    public void setAdmissionRequirements(Map<String, Object> admissionRequirements) {
        this.admissionRequirements = admissionRequirements;
    }
    
    public void addAdmissionRequirement(String key, Object value) {
        this.admissionRequirements.put(key, value);
    }
    
    // Getters and setters for financial information
    public double getTuitionFeeLocal() {
        return tuitionFeeLocal;
    }
    
    public void setTuitionFeeLocal(double tuitionFeeLocal) {
        this.tuitionFeeLocal = tuitionFeeLocal;
    }
    
    public double getTuitionFeeInternational() {
        return tuitionFeeInternational;
    }
    
    public void setTuitionFeeInternational(double tuitionFeeInternational) {
        this.tuitionFeeInternational = tuitionFeeInternational;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public double getApplicationFee() {
        return applicationFee;
    }
    
    public void setApplicationFee(double applicationFee) {
        this.applicationFee = applicationFee;
    }
    
    public boolean hasScholarships() {
        return hasScholarships;
    }
    
    public void setHasScholarships(boolean hasScholarships) {
        this.hasScholarships = hasScholarships;
    }
    
    public List<Map<String, Object>> getScholarships() {
        return scholarships;
    }
    
    public void setScholarships(List<Map<String, Object>> scholarships) {
        this.scholarships = scholarships;
        this.hasScholarships = scholarships != null && !scholarships.isEmpty();
    }
    
    public void addScholarship(Map<String, Object> scholarship) {
        this.scholarships.add(scholarship);
        this.hasScholarships = true;
    }
    
    // Getters and setters for academic details
    public int getTotalCredits() {
        return totalCredits;
    }
    
    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }
    
    public List<String> getAvailableSpecializations() {
        return availableSpecializations;
    }
    
    public void setAvailableSpecializations(List<String> availableSpecializations) {
        this.availableSpecializations = availableSpecializations;
    }
    
    public void addSpecialization(String specialization) {
        this.availableSpecializations.add(specialization);
    }
    
    public List<Map<String, Object>> getCurriculum() {
        return curriculum;
    }
    
    public void setCurriculum(List<Map<String, Object>> curriculum) {
        this.curriculum = curriculum;
    }
    
    public void addCourse(Map<String, Object> course) {
        this.curriculum.add(course);
    }
    
    public Map<String, Integer> getTestScoreRequirements() {
        return testScoreRequirements;
    }
    
    public void setTestScoreRequirements(Map<String, Integer> testScoreRequirements) {
        this.testScoreRequirements = testScoreRequirements;
    }
    
    public void addTestScoreRequirement(String testName, int minimumScore) {
        this.testScoreRequirements.put(testName, minimumScore);
    }
    
    public double getMinimumGPA() {
        return minimumGPA;
    }
    
    public void setMinimumGPA(double minimumGPA) {
        this.minimumGPA = minimumGPA;
    }
    
    // Getters and setters for career prospects
    public List<String> getCareerOpportunities() {
        return careerOpportunities;
    }
    
    public void setCareerOpportunities(List<String> careerOpportunities) {
        this.careerOpportunities = careerOpportunities;
    }
    
    public void addCareerOpportunity(String careerOpportunity) {
        this.careerOpportunities.add(careerOpportunity);
    }
    
    public double getEmploymentRate() {
        return employmentRate;
    }
    
    public void setEmploymentRate(double employmentRate) {
        this.employmentRate = employmentRate;
    }
    
    public double getAverageStartingSalary() {
        return averageStartingSalary;
    }
    
    public void setAverageStartingSalary(double averageStartingSalary) {
        this.averageStartingSalary = averageStartingSalary;
    }
    
    // Getters and setters for additional information
    public String getLanguageOfInstruction() {
        return languageOfInstruction;
    }
    
    public void setLanguageOfInstruction(String languageOfInstruction) {
        this.languageOfInstruction = languageOfInstruction;
    }
    
    public List<String> getRequiredDocuments() {
        return requiredDocuments;
    }
    
    public void setRequiredDocuments(List<String> requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
    }
    
    public void addRequiredDocument(String document) {
        this.requiredDocuments.add(document);
    }
    
    public List<String> getProgramFeatures() {
        return programFeatures;
    }
    
    public void setProgramFeatures(List<String> programFeatures) {
        this.programFeatures = programFeatures;
    }
    
    public void addProgramFeature(String feature) {
        this.programFeatures.add(feature);
    }
    
    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }
    
    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    
    public void addAdditionalInfo(String key, Object value) {
        this.additionalInfo.put(key, value);
    }
    
    // Getters and setters for static program data
    public int getRanking() {
        return ranking;
    }
    
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
    
    public int getInternationalStudentPercentage() {
        return internationalStudentPercentage;
    }
    
    public void setInternationalStudentPercentage(int internationalStudentPercentage) {
        this.internationalStudentPercentage = internationalStudentPercentage;
    }
    
    public double getStudentSatisfactionRate() {
        return studentSatisfactionRate;
    }
    
    public void setStudentSatisfactionRate(double studentSatisfactionRate) {
        this.studentSatisfactionRate = studentSatisfactionRate;
    }
    
    // Helper methods
    public String getFormattedDuration() {
        if (durationYears > 0 && durationMonths > 0) {
            return durationYears + " years, " + durationMonths + " months";
        } else if (durationYears > 0) {
            return durationYears + " years";
        } else if (durationMonths > 0) {
            return durationMonths + " months";
        } else {
            return "Duration not specified";
        }
    }
    
    public String getFormattedDegree() {
        String formattedDegree = "";
        
        switch (degreeType.toLowerCase()) {
            case "bachelor":
                formattedDegree = "Bachelor's in ";
                break;
            case "master":
                formattedDegree = "Master's in ";
                break;
            case "phd":
                formattedDegree = "PhD in ";
                break;
            case "certificate":
                formattedDegree = "Certificate in ";
                break;
            case "diploma":
                formattedDegree = "Diploma in ";
                break;
            default:
                formattedDegree = degreeType + " in ";
                break;
        }
        
        return formattedDegree + fieldOfStudy;
    }
    
    public Date getNextDeadline() {
        if (applicationDeadlines == null || applicationDeadlines.isEmpty()) {
            return null;
        }
        
        Date now = new Date();
        Date nextDeadline = null;
        
        for (Date deadline : applicationDeadlines) {
            if (deadline.after(now)) {
                if (nextDeadline == null || deadline.before(nextDeadline)) {
                    nextDeadline = deadline;
                }
            }
        }
        
        return nextDeadline;
    }
    
    public long getDaysUntilNextDeadline() {
        Date nextDeadline = getNextDeadline();
        if (nextDeadline == null) {
            return -1;
        }
        
        long diff = nextDeadline.getTime() - new Date().getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    public boolean meetsRequirement(String testName, int score) {
        if (!testScoreRequirements.containsKey(testName)) {
            return true; // Test not required
        }
        
        int requiredScore = testScoreRequirements.get(testName);
        return score >= requiredScore;
    }
    
    public boolean meetsGPARequirement(double gpa) {
        return gpa >= minimumGPA;
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("universityId", universityId);
        map.put("departmentId", departmentId);
        map.put("degreeType", degreeType);
        map.put("fieldOfStudy", fieldOfStudy);
        map.put("description", description);
        map.put("isActive", isActive);
        map.put("formattedDegree", getFormattedDegree());
        
        return map;
    }
} 