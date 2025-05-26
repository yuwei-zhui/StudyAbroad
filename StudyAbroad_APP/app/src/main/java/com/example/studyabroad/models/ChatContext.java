package com.example.studyabroad.models;

import java.io.Serializable;

public class ChatContext implements Serializable {
    
    public static final String CONTEXT_UNIVERSITY = "university";
    public static final String CONTEXT_PROGRAM = "program";
    public static final String CONTEXT_GENERAL = "general";
    
    private String contextType;
    private String universityName;
    private String programName;
    private String programId;
    private String universityId;
    private String fieldOfStudy;
    private String degreeLevel;
    private double tuitionFee;
    private String currency;
    private String location;
    private String country;
    
    public ChatContext() {
        this.contextType = CONTEXT_GENERAL;
    }
    
    public ChatContext(String contextType) {
        this.contextType = contextType;
    }
    
    public static ChatContext createUniversityContext(String universityName, String universityId, String location, String country) {
        ChatContext context = new ChatContext(CONTEXT_UNIVERSITY);
        context.setUniversityName(universityName);
        context.setUniversityId(universityId);
        context.setLocation(location);
        context.setCountry(country);
        return context;
    }
    
    public static ChatContext createProgramContext(String universityName, String programName, 
                                                  String programId, String fieldOfStudy, 
                                                  String degreeLevel, double tuitionFee, 
                                                  String currency, String location, String country) {
        ChatContext context = new ChatContext(CONTEXT_PROGRAM);
        context.setUniversityName(universityName);
        context.setProgramName(programName);
        context.setProgramId(programId);
        context.setFieldOfStudy(fieldOfStudy);
        context.setDegreeLevel(degreeLevel);
        context.setTuitionFee(tuitionFee);
        context.setCurrency(currency);
        context.setLocation(location);
        context.setCountry(country);
        return context;
    }
    
    public String getContextDescription() {
        switch (contextType) {
            case CONTEXT_UNIVERSITY:
                return "University: " + universityName + " in " + location;
            case CONTEXT_PROGRAM:
                return "Program: " + programName + " at " + universityName;
            default:
                return "General chat";
        }
    }
    
    public String generateWelcomeMessage() {
        switch (contextType) {
            case CONTEXT_UNIVERSITY:
                return "Hello! I see you're interested in " + universityName + " in " + location + 
                       ". I can help you with information about this university. What would you like to know?";
            case CONTEXT_PROGRAM:
                return "Hello! I see you're interested in the " + programName + " program at " + universityName + 
                       ". I can help you with information about this program and university. What would you like to know?";
            default:
                return "Hello! How can I help you with your study abroad plans?";
        }
    }
    
    public String getContextForAI() {
        StringBuilder context = new StringBuilder();
        context.append("User is inquiring about: ");
        
        switch (contextType) {
            case CONTEXT_UNIVERSITY:
                context.append("University: ").append(universityName);
                if (location != null) context.append(" located in ").append(location);
                if (country != null) context.append(", ").append(country);
                break;
            case CONTEXT_PROGRAM:
                context.append("Program: ").append(programName);
                context.append(" at ").append(universityName);
                if (fieldOfStudy != null) context.append(" (Field: ").append(fieldOfStudy).append(")");
                if (degreeLevel != null) context.append(" (Level: ").append(degreeLevel).append(")");
                if (tuitionFee > 0) context.append(" (Fee: ").append(currency).append(" ").append(tuitionFee).append(")");
                if (location != null) context.append(" in ").append(location);
                break;
            default:
                context.append("General study abroad information");
                break;
        }
        
        return context.toString();
    }
    
    // Getters and setters
    public String getContextType() { return contextType; }
    public void setContextType(String contextType) { this.contextType = contextType; }
    
    public String getUniversityName() { return universityName; }
    public void setUniversityName(String universityName) { this.universityName = universityName; }
    
    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }
    
    public String getProgramId() { return programId; }
    public void setProgramId(String programId) { this.programId = programId; }
    
    public String getUniversityId() { return universityId; }
    public void setUniversityId(String universityId) { this.universityId = universityId; }
    
    public String getFieldOfStudy() { return fieldOfStudy; }
    public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }
    
    public String getDegreeLevel() { return degreeLevel; }
    public void setDegreeLevel(String degreeLevel) { this.degreeLevel = degreeLevel; }
    
    public double getTuitionFee() { return tuitionFee; }
    public void setTuitionFee(double tuitionFee) { this.tuitionFee = tuitionFee; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
} 