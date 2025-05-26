package com.example.studyabroad.models;

public class DreamSchool {
    private long id;
    private String universityName;
    private String programName;
    private String universityId;
    private String programId;
    private String location;
    private String ranking;
    private String tuitionFee;
    private String applicationDeadline;
    private long addedTimestamp;

    public DreamSchool() {}

    public DreamSchool(String universityName, String programName, String universityId, String programId) {
        this.universityName = universityName;
        this.programName = programName;
        this.universityId = universityId;
        this.programId = programId;
        this.addedTimestamp = System.currentTimeMillis();
    }

    public DreamSchool(String universityName, String programName, String universityId, String programId,
                      String location, String ranking, String tuitionFee, String applicationDeadline) {
        this.universityName = universityName;
        this.programName = programName;
        this.universityId = universityId;
        this.programId = programId;
        this.location = location;
        this.ranking = ranking;
        this.tuitionFee = tuitionFee;
        this.applicationDeadline = applicationDeadline;
        this.addedTimestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getTuitionFee() {
        return tuitionFee;
    }

    public void setTuitionFee(String tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    public String getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(String applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public long getAddedTimestamp() {
        return addedTimestamp;
    }

    public void setAddedTimestamp(long addedTimestamp) {
        this.addedTimestamp = addedTimestamp;
    }

    /**
     * Generate unique key for checking duplicates
     */
    public String getUniqueKey() {
        return universityName + "|" + programName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        DreamSchool that = (DreamSchool) obj;
        return getUniqueKey().equals(that.getUniqueKey());
    }

    @Override
    public int hashCode() {
        return getUniqueKey().hashCode();
    }
} 