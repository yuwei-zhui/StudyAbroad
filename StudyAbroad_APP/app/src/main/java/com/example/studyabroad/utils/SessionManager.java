package com.example.studyabroad.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.studyabroad.database.entity.User;
import com.google.gson.Gson;

public class SessionManager {
    
    private static final String PREF_NAME = "StudyAbroadSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_DATA = "userData";

    
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    
    private static SessionManager instance;
    
    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }
    
    private SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    
    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
    
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putLong(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NAME, user.getName());
        
        // Store the entire user object as JSON
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_DATA, userJson);
        
        editor.apply();
    }
    
    public User getUser() {
        String userJson = pref.getString(KEY_USER_DATA, null);
        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }
    
    public long getUserId() {
        return pref.getLong(KEY_USER_ID, -1);
    }
    
    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, null);
    }
    
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, null);
    }
    
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
    
    public void updateUserSession(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_DATA, userJson);
        editor.putString(KEY_USER_NAME, user.getName());
        editor.apply();
    }
} 