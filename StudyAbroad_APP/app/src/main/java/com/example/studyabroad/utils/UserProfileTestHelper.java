package com.example.studyabroad.utils;

import android.content.Context;
import com.example.studyabroad.database.entity.User;

/**
 * Test helper for creating sample user sessions to test AI chat functionality
 */
public class UserProfileTestHelper {
    
    /**
     * Create a sample user session for testing AI chat with real user data
     * This method can be called from any activity to test the AI chat functionality
     */
    public static void createSampleUserSession(Context context) {
        // Create a sample user with realistic data
        User sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setName("张小明"); // Chinese name for testing
        sampleUser.setEmail("zhangxiaoming@example.com");
        sampleUser.setPhoneNumber("+86 138 0013 8000");
        sampleUser.setDateOfBirth("1998-05-15");
        sampleUser.setNationality("Chinese");
        sampleUser.setCurrentInstitution("Beijing University");
        sampleUser.setMajor("Computer Science");
        sampleUser.setGpa(3.8);
        sampleUser.setTargetDegreeLevel("Master's Degree");
        sampleUser.setPreferredCountries("United States, Canada, United Kingdom");
        sampleUser.setQsRanking("Top 50");
        
        // Create login session
        SessionManager sessionManager = SessionManager.getInstance(context);
        sessionManager.createLoginSession(sampleUser);
    }
    
    /**
     * Create another sample user session with different data
     */
    public static void createSampleUserSession2(Context context) {
        User sampleUser = new User();
        sampleUser.setId(2);
        sampleUser.setName("李小红"); // Another Chinese name
        sampleUser.setEmail("lixiaohong@example.com");
        sampleUser.setPhoneNumber("+86 139 0013 9000");
        sampleUser.setDateOfBirth("1999-08-22");
        sampleUser.setNationality("Chinese");
        sampleUser.setCurrentInstitution("Tsinghua University");
        sampleUser.setMajor("Business Administration");
        sampleUser.setGpa(3.9);
        sampleUser.setTargetDegreeLevel("MBA");
        sampleUser.setPreferredCountries("Australia, Singapore");
        sampleUser.setQsRanking("Top 30");
        
        SessionManager sessionManager = SessionManager.getInstance(context);
        sessionManager.createLoginSession(sampleUser);
    }
    
    /**
     * Get user background information for testing
     */
    public static String getUserBackgroundForTesting(Context context) {
        UserProfileManager profileManager = UserProfileManager.getInstance(context);
        return profileManager.getUserBackgroundForAI();
    }
    
    /**
     * Clear user session for testing
     */
    public static void clearUserSession(Context context) {
        SessionManager sessionManager = SessionManager.getInstance(context);
        sessionManager.logoutUser();
    }
} 