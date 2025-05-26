package com.example.studyabroad.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonLoginWithGoogle;
    private TextView textViewRegister;
    
    private UserViewModel userViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        
        // Initialize SessionManager
        sessionManager = SessionManager.getInstance(this);
        
        // Find views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLoginWithGoogle = findViewById(R.id.buttonLoginWithGoogle);
        textViewRegister = findViewById(R.id.textViewRegister);
        
        // Set click listeners
        buttonLogin.setOnClickListener(v -> attemptLogin());
        
        buttonLoginWithGoogle.setOnClickListener(v -> {
            // For the demo, we'll just simulate Google sign-in
            Toast.makeText(LoginActivity.this, "Google Sign-In would happen here", Toast.LENGTH_SHORT).show();
        });
        
        textViewRegister.setOnClickListener(v -> {
            // Navigate to Register screen
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    
    private void attemptLogin() {
        // Reset errors
        editTextEmail.setError(null);
        editTextPassword.setError(null);
        
        // Get input values
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        
        // Check for valid input
        boolean cancel = false;
        View focusView = null;
        
        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_occurred));
            focusView = editTextPassword;
            cancel = true;
        } else if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            focusView = editTextPassword;
            cancel = true;
        }
        
        // Check for a valid email address
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_occurred));
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
            // And attempt to sign in
            userViewModel.login(email, password, new UserRepository.LoginCallback() {
                @Override
                public void onLoginResult(User user) {
                    runOnUiThread(() -> {
                        if (user != null) {
                            // Login successful
                            sessionManager.createLoginSession(user);
                            
                            // Navigate to main activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // Login failed
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
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