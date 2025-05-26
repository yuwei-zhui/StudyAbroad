package com.example.studyabroad.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyabroad.MainActivity;
import com.example.studyabroad.R;
import com.example.studyabroad.database.entity.User;
import com.example.studyabroad.repositories.UserRepository;
import com.example.studyabroad.utils.SessionManager;
import com.example.studyabroad.viewmodel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private Button buttonRegisterWithGoogle;
    private TextView textViewLogin;
    
    private UserViewModel userViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        
        // Initialize SessionManager
        sessionManager = SessionManager.getInstance(this);
        
        // Find views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegisterWithGoogle = findViewById(R.id.buttonRegisterWithGoogle);
        textViewLogin = findViewById(R.id.textViewLogin);
        
        // Set click listeners
        buttonRegister.setOnClickListener(v -> attemptRegister());
        
        buttonRegisterWithGoogle.setOnClickListener(v -> {
            // For the demo, we'll just simulate Google sign-in
            Toast.makeText(RegisterActivity.this, "Google Sign-Up would happen here", Toast.LENGTH_SHORT).show();
        });
        
        textViewLogin.setOnClickListener(v -> {
            // Navigate back to Login screen
            finish();
        });
    }
    
    private void attemptRegister() {
        // Reset errors
        editTextName.setError(null);
        editTextEmail.setError(null);
        editTextPassword.setError(null);
        
        // Get input values
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        
        // Check for valid input
        boolean cancel = false;
        View focusView = null;
        
        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            focusView = editTextName;
            cancel = true;
        }
        
        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            focusView = editTextPassword;
            cancel = true;
        } else if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            focusView = editTextPassword;
            cancel = true;
        }
        
        // Check for a valid email address
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required");
            focusView = editTextEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editTextEmail.setError("Invalid email format");
            focusView = editTextEmail;
            cancel = true;
        }
        
        if (cancel) {
            // There was an error; focus the first form field with an error
            focusView.requestFocus();
        } else {
            // Show a progress spinner (not implemented here for simplicity)
            // First check if email already exists
            userViewModel.checkEmailExists(email, new UserRepository.EmailCheckCallback() {
                @Override
                public void onEmailChecked(boolean exists) {
                    runOnUiThread(() -> {
                        if (exists) {
                            // Email already exists
                            editTextEmail.setError("Email already in use");
                            editTextEmail.requestFocus();
                        } else {
                            // Proceed with registration
                            User newUser = new User(name, email, password);
                            
                            try {
                                // Use the improved repository method that properly waits for the DB operation
                                long userId = userViewModel.insert(newUser);
                                Log.d(TAG, "User registration - generated ID: " + userId);
                                
                                if (userId > 0) {
                                    // Set the generated ID
                                    newUser.setId(userId);
                                    
                                    // Create login session
                                    sessionManager.createLoginSession(newUser);
                                    
                                    // Navigate to academic profile builder for new users
                                    Intent intent = new Intent(RegisterActivity.this, com.example.studyabroad.ui.profile.BuildAcademicProfileActivity.class);
                                    intent.putExtra("isFromRegistration", true);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Registration failed
                                    Log.e(TAG, "User registration failed - invalid ID returned: " + userId);
                                    Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Exception during user registration: " + e.getMessage(), e);
                                Toast.makeText(RegisterActivity.this, "Registration error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }
    
    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }
} 