package com.example.studyabroad.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "user_preferences",
        foreignKeys = @ForeignKey(entity = User.class,
                                parentColumns = "id",
                                childColumns = "userId",
                                onDelete = ForeignKey.CASCADE),
        indices = {@Index("userId")})
public class UserPreference {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private long userId;
    private String preferredCountries; // Comma-separated list of countries
    private int qsRankingRange; // Maximum QS rank to consider
    private String budgetRange; // Format: "min-max" in USD
    private String preferredMajor;
    private boolean documentsUploaded;
    
    // Constructors
    public UserPreference() {
    }
    
    @Ignore
    public UserPreference(long userId, String preferredCountries, int qsRankingRange) {
        this.userId = userId;
        this.preferredCountries = preferredCountries;
        this.qsRankingRange = qsRankingRange;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getUserId() {
        return userId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public String getPreferredCountries() {
        return preferredCountries;
    }
    
    public void setPreferredCountries(String preferredCountries) {
        this.preferredCountries = preferredCountries;
    }
    
    public int getQsRankingRange() {
        return qsRankingRange;
    }
    
    public void setQsRankingRange(int qsRankingRange) {
        this.qsRankingRange = qsRankingRange;
    }
    
    public String getBudgetRange() {
        return budgetRange;
    }
    
    public void setBudgetRange(String budgetRange) {
        this.budgetRange = budgetRange;
    }
    
    public String getPreferredMajor() {
        return preferredMajor;
    }
    
    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }
    
    public boolean isDocumentsUploaded() {
        return documentsUploaded;
    }
    
    public void setDocumentsUploaded(boolean documentsUploaded) {
        this.documentsUploaded = documentsUploaded;
    }
} 