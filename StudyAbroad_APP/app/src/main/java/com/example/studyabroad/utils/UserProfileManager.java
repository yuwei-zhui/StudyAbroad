package com.example.studyabroad.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.studyabroad.database.entity.User;

/**
 * User Profile Manager - Using Singleton Pattern
 * Responsible for storing and managing user's personal information and preferences
 */
public class UserProfileManager {
    
    private static final String PREFS_NAME = "UserProfile";
    
    // Personal information keys
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_BIRTH_DATE = "birth_date";
    private static final String KEY_NATIONALITY = "nationality";
    private static final String KEY_INTRODUCTION = "introduction";
    private static final String KEY_PROFILE_PICTURE = "profile_picture";
    
    // Academic profile keys
    private static final String KEY_INSTITUTION = "institution";
    private static final String KEY_GPA = "gpa";
    private static final String KEY_MAJOR = "major";
    private static final String KEY_DEGREE = "degree";
    private static final String KEY_PREFERRED_COUNTRY = "preferred_country";
    private static final String KEY_RANKING_FROM = "ranking_from";
    private static final String KEY_RANKING_TO = "ranking_to";
    
    // Preference keys
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_THEME = "theme";
    
    private static UserProfileManager instance;
    private final SharedPreferences sharedPreferences;
    private final Context context;
    
    // Private constructor
    private UserProfileManager(Context context) {
        this.context = context.getApplicationContext();
        sharedPreferences = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    // Singleton getter method
    public static synchronized UserProfileManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserProfileManager(context.getApplicationContext());
        }
        return instance;
    }
    
    // ============== Personal Information Methods ==============
    
    public String getName() {
        // First try to get from preferences, then from session, then default
        String name = sharedPreferences.getString(KEY_NAME, null);
        if (name == null || name.isEmpty()) {
            SessionManager sessionManager = SessionManager.getInstance(context);
            User currentUser = sessionManager.getUser();
            if (currentUser != null && currentUser.getName() != null && !currentUser.getName().isEmpty()) {
                return currentUser.getName();
            }
            return "User"; // Default fallback
        }
        return name;
    }
    
    public void setName(String name) {
        sharedPreferences.edit().putString(KEY_NAME, name).apply();
    }
    
    public String getEmail() {
        // First try to get from preferences, then from session, then default
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        if (email == null || email.isEmpty()) {
            SessionManager sessionManager = SessionManager.getInstance(context);
            User currentUser = sessionManager.getUser();
            if (currentUser != null && currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
                return currentUser.getEmail();
            }
            return "user@example.com"; // Default fallback
        }
        return email;
    }
    
    public void setEmail(String email) {
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply();
    }
    
    public String getPhone() {
        // First try to get from preferences, then from session, then default
        String phone = sharedPreferences.getString(KEY_PHONE, null);
        if (phone == null || phone.isEmpty()) {
            SessionManager sessionManager = SessionManager.getInstance(context);
            User currentUser = sessionManager.getUser();
            if (currentUser != null && currentUser.getPhoneNumber() != null && !currentUser.getPhoneNumber().isEmpty()) {
                return currentUser.getPhoneNumber();
            }
            return "No phone number"; // Default fallback
        }
        return phone;
    }
    
    public void setPhone(String phone) {
        sharedPreferences.edit().putString(KEY_PHONE, phone).apply();
    }
    
    public String getBirthDate() {
        return sharedPreferences.getString(KEY_BIRTH_DATE, "");
    }
    
    public void setBirthDate(String birthDate) {
        sharedPreferences.edit().putString(KEY_BIRTH_DATE, birthDate).apply();
    }
    
    public String getNationality() {
        return sharedPreferences.getString(KEY_NATIONALITY, "");
    }
    
    public void setNationality(String nationality) {
        sharedPreferences.edit().putString(KEY_NATIONALITY, nationality).apply();
    }
    
    public String getIntroduction() {
        return sharedPreferences.getString(KEY_INTRODUCTION, "");
    }
    
    public void setIntroduction(String introduction) {
        sharedPreferences.edit().putString(KEY_INTRODUCTION, introduction).apply();
    }
    
    public String getProfilePicture() {
        return sharedPreferences.getString(KEY_PROFILE_PICTURE, "");
    }
    
    public void setProfilePicture(String profilePicture) {
        sharedPreferences.edit().putString(KEY_PROFILE_PICTURE, profilePicture).apply();
    }
    
    // ============== Academic Profile Methods ==============
    
    public String getInstitution() {
        return sharedPreferences.getString(KEY_INSTITUTION, "");
    }
    
    public void setInstitution(String institution) {
        sharedPreferences.edit().putString(KEY_INSTITUTION, institution).apply();
    }
    
    public String getGPA() {
        return sharedPreferences.getString(KEY_GPA, "");
    }
    
    public void setGPA(String gpa) {
        sharedPreferences.edit().putString(KEY_GPA, gpa).apply();
    }
    
    public String getMajor() {
        return sharedPreferences.getString(KEY_MAJOR, "");
    }
    
    public void setMajor(String major) {
        sharedPreferences.edit().putString(KEY_MAJOR, major).apply();
    }
    
    public String getDegree() {
        return sharedPreferences.getString(KEY_DEGREE, "");
    }
    
    public void setDegree(String degree) {
        sharedPreferences.edit().putString(KEY_DEGREE, degree).apply();
    }
    
    public String getPreferredCountry() {
        return sharedPreferences.getString(KEY_PREFERRED_COUNTRY, "");
    }
    
    public void setPreferredCountry(String preferredCountry) {
        sharedPreferences.edit().putString(KEY_PREFERRED_COUNTRY, preferredCountry).apply();
    }
    
    public String getRankingFrom() {
        return sharedPreferences.getString(KEY_RANKING_FROM, "");
    }
    
    public void setRankingFrom(String rankingFrom) {
        sharedPreferences.edit().putString(KEY_RANKING_FROM, rankingFrom).apply();
    }
    
    public String getRankingTo() {
        return sharedPreferences.getString(KEY_RANKING_TO, "");
    }
    
    public void setRankingTo(String rankingTo) {
        sharedPreferences.edit().putString(KEY_RANKING_TO, rankingTo).apply();
    }
    
    // ============== Preference Methods ==============
    
    public boolean isNotificationsEnabled() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }
    
    public void setNotificationsEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply();
    }
    
    public String getLanguage() {
        return sharedPreferences.getString(KEY_LANGUAGE, "English");
    }
    
    public void setLanguage(String language) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply();
    }
    
    public String getTheme() {
        return sharedPreferences.getString(KEY_THEME, "Light");
    }
    
    public void setTheme(String theme) {
        sharedPreferences.edit().putString(KEY_THEME, theme).apply();
    }
    
    // ============== AI Chat Related Methods ==============
    
    /**
     * Get user background data for AI chat
     * @return String containing user background information
     */
    public String getUserBackgroundForAI() {
        StringBuilder background = new StringBuilder();
        
        // Get current logged-in user from session
        SessionManager sessionManager = SessionManager.getInstance(context);
        User currentUser = sessionManager.getUser();
        
        if (currentUser != null) {
            // Use actual user data from session
            String name = currentUser.getName();
            if (name != null && !name.isEmpty()) {
                background.append("Name: ").append(name).append("\n");
            }
            
            String nationality = currentUser.getNationality();
            if (nationality != null && !nationality.isEmpty()) {
                background.append("Nationality: ").append(nationality).append("\n");
            }
            
            String dateOfBirth = currentUser.getDateOfBirth();
            if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                background.append("Date of Birth: ").append(dateOfBirth).append("\n");
            }
            
            String currentInstitution = currentUser.getCurrentInstitution();
            if (currentInstitution != null && !currentInstitution.isEmpty()) {
                background.append("Current Institution: ").append(currentInstitution).append("\n");
            }
            
            String major = currentUser.getMajor();
            if (major != null && !major.isEmpty()) {
                background.append("Major: ").append(major).append("\n");
            }
            
            double gpa = currentUser.getGpa();
            if (gpa > 0) {
                background.append("GPA: ").append(gpa).append("\n");
            }
            
            String targetDegreeLevel = currentUser.getTargetDegreeLevel();
            if (targetDegreeLevel != null && !targetDegreeLevel.isEmpty()) {
                background.append("Target Degree: ").append(targetDegreeLevel).append("\n");
            }
            
            String preferredCountries = currentUser.getPreferredCountries();
            if (preferredCountries != null && !preferredCountries.isEmpty()) {
                background.append("Preferred Countries: ").append(preferredCountries).append("\n");
            }
            
            String qsRanking = currentUser.getQsRanking();
            if (qsRanking != null && !qsRanking.isEmpty()) {
                background.append("QS Ranking Preference: ").append(qsRanking).append("\n");
            }
        }
        
        // Also include any additional information from preferences
        String introduction = getIntroduction();
        if (!introduction.isEmpty()) {
            background.append("Personal Introduction: ").append(introduction).append("\n");
        }
        
        // Add preference-based academic information if not available from user entity
        if (currentUser == null || currentUser.getCurrentInstitution() == null || currentUser.getCurrentInstitution().isEmpty()) {
            String institution = getInstitution();
            if (!institution.isEmpty()) {
                background.append("Current Institution: ").append(institution).append("\n");
            }
        }
        
        if (currentUser == null || currentUser.getGpa() <= 0) {
            String gpa = getGPA();
            if (!gpa.isEmpty()) {
                background.append("GPA: ").append(gpa).append("\n");
            }
        }
        
        if (currentUser == null || currentUser.getMajor() == null || currentUser.getMajor().isEmpty()) {
            String major = getMajor();
            if (!major.isEmpty()) {
                background.append("Major: ").append(major).append("\n");
            }
        }
        
        if (currentUser == null || currentUser.getTargetDegreeLevel() == null || currentUser.getTargetDegreeLevel().isEmpty()) {
            String degree = getDegree();
            if (!degree.isEmpty()) {
                background.append("Target Degree: ").append(degree).append("\n");
            }
        }
        
        if (currentUser == null || currentUser.getPreferredCountries() == null || currentUser.getPreferredCountries().isEmpty()) {
            String preferredCountry = getPreferredCountry();
            if (!preferredCountry.isEmpty()) {
                background.append("Preferred Country: ").append(preferredCountry).append("\n");
            }
        }
        
        String rankingFrom = getRankingFrom();
        String rankingTo = getRankingTo();
        if (!rankingFrom.isEmpty() && !rankingTo.isEmpty()) {
            background.append("QS Ranking Range: ").append(rankingFrom).append("-").append(rankingTo).append("\n");
        }
        
        return background.toString();
    }
    
    /**
     * Clear user data (for logout or reset)
     */
    public void clearUserData() {
        sharedPreferences.edit().clear().apply();
    }
} 