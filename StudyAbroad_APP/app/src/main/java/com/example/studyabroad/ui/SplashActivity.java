package com.example.studyabroad.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studyabroad.MainActivity;
import com.example.studyabroad.R;
import com.example.studyabroad.ui.auth.LoginActivity;
import com.example.studyabroad.ui.onboarding.OnboardingActivity;
import com.example.studyabroad.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 1500; // 1.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Always clear login status on app restart - user needs to login each time
            SessionManager sessionManager = SessionManager.getInstance(this);
            sessionManager.logoutUser(); // Clear any existing login session
            
            Intent intent;
            
            // Check if first time launching the app
            if (isFirstTimeLaunch()) {
                // First time, show onboarding
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            } else {
                // Not first time, go to login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            
            startActivity(intent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
    
    // Helper method to check if this is the first time launching the app
    private boolean isFirstTimeLaunch() {
        // For simplicity, we'll just store a flag in SharedPreferences
        final String PREF_NAME = "StudyAbroadPref";
        final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
        
        android.content.SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
        
        if (isFirstTime) {
            // Set the flag to false for next time
            android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
            editor.apply();
        }
        
        return isFirstTime;
    }
} 