package com.example.studyabroad.models;

import java.util.Date;

public class TimelineItem {
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_COMPLETED = 1;
    public static final int STATUS_OVERDUE = 2;

    private long id;
    private String title;
    private String description;
    private Date date;
    private int status;
    private String universityId;
    private String universityName;
    private String programName;

    public TimelineItem(String title, String description, Date date, int status, String universityId) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
        this.universityId = universityId;
    }

    public TimelineItem(String title, String description, Date date, int status, String universityId, String universityName, String programName) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
        this.universityId = universityId;
        this.universityName = universityName;
        this.programName = programName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
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

    /**
     * Check if this represents the same university and program as another TimelineItem
     */
    public boolean isSameUniversityAndProgram(TimelineItem other) {
        if (other == null) return false;
        
        boolean sameUniversity = (this.universityName != null && this.universityName.equals(other.universityName)) ||
                                (this.universityId != null && this.universityId.equals(other.universityId));
        
        boolean sameProgram = this.programName != null && this.programName.equals(other.programName);
        
        return sameUniversity && sameProgram;
    }

    /**
     * Generate unique identifier for checking duplicates
     */
    public String getUniqueKey() {
        String university = universityName != null ? universityName : (universityId != null ? universityId : "");
        String program = programName != null ? programName : "";
        return university + "|" + program;
    }
} 