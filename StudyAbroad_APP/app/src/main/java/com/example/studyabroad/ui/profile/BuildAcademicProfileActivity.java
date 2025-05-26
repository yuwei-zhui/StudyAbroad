package com.example.studyabroad.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyabroad.MainActivity;
import com.example.studyabroad.R;
import com.example.studyabroad.database.entity.User;
import com.example.studyabroad.utils.SessionManager;
import com.example.studyabroad.viewmodel.UserViewModel;

public class BuildAcademicProfileActivity extends AppCompatActivity {

    private EditText etInstitution;
    private EditText etGPA;
    private EditText etRankingFrom;
    private EditText etRankingTo;
    private Spinner spinnerMajor;
    private Spinner spinnerDegree;
    private Spinner spinnerCountries;
    private Button btnSave;
    private ImageView btnBack;

    private UserViewModel userViewModel;
    private SessionManager sessionManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_academic_profile);

        // Initialize ViewModel and SessionManager
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        sessionManager = SessionManager.getInstance(this);
        currentUser = sessionManager.getUser();

        // Initialize views
        initViews();
        setupSpinners();
        setupClickListeners();
        loadExistingData();
    }

    private void initViews() {
        etInstitution = findViewById(R.id.etInstitution);
        etGPA = findViewById(R.id.etGPA);
        etRankingFrom = findViewById(R.id.etRankingFrom);
        etRankingTo = findViewById(R.id.etRankingTo);
        spinnerMajor = findViewById(R.id.spinnerMajor);
        spinnerDegree = findViewById(R.id.spinnerDegree);
        spinnerCountries = findViewById(R.id.spinnerCountries);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupSpinners() {
        // Setup Major Spinner
        String[] majors = {"Computer Science", "Engineering", "Business", "Arts", "Medicine", "Law"};
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                majors
        );
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(majorAdapter);

        // Setup Degree Spinner
        String[] degrees = {"Bachelor", "Master", "PhD"};
        ArrayAdapter<String> degreeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                degrees
        );
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDegree.setAdapter(degreeAdapter);

        // Setup Countries Spinner
        String[] countries = {"United States", "United Kingdom", "Australia", "Canada", "Germany", "Japan"};
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                countries
        );
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryAdapter);
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveProfile());
        btnBack.setOnClickListener(v -> finish());
    }
    
    private void loadExistingData() {
        if (currentUser != null) {
            // Load existing data if available
            if (currentUser.getCurrentInstitution() != null && !currentUser.getCurrentInstitution().isEmpty()) {
                etInstitution.setText(currentUser.getCurrentInstitution());
            }
            
            if (currentUser.getGpa() > 0) {
                etGPA.setText(String.valueOf(currentUser.getGpa()));
            }
            
            // Set spinner selections
            if (currentUser.getMajor() != null && !currentUser.getMajor().isEmpty()) {
                setSpinnerSelection(spinnerMajor, currentUser.getMajor());
            }
            
            if (currentUser.getTargetDegreeLevel() != null && !currentUser.getTargetDegreeLevel().isEmpty()) {
                setSpinnerSelection(spinnerDegree, currentUser.getTargetDegreeLevel());
            }
            
            if (currentUser.getPreferredCountries() != null && !currentUser.getPreferredCountries().isEmpty()) {
                // Use the first country from the list
                String[] countries = currentUser.getPreferredCountries().split(",");
                if (countries.length > 0) {
                    setSpinnerSelection(spinnerCountries, countries[0].trim());
                }
            }
        }
    }
    
    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void saveProfile() {
        // Get input values
        String currentInstitution = etInstitution.getText().toString().trim();
        String gpaStr = etGPA.getText().toString().trim();
        String major = spinnerMajor.getSelectedItem() != null ? 
            spinnerMajor.getSelectedItem().toString() : "";
        String targetDegreeLevel = spinnerDegree.getSelectedItem() != null ? 
            spinnerDegree.getSelectedItem().toString() : "";
        String preferredCountries = spinnerCountries.getSelectedItem() != null ? 
            spinnerCountries.getSelectedItem().toString() : "";
        String rankingFrom = etRankingFrom.getText().toString().trim();
        String rankingTo = etRankingTo.getText().toString().trim();

        // Validate inputs
        if (currentInstitution.isEmpty()) {
            etInstitution.setError("Current institution is required");
            etInstitution.requestFocus();
            return;
        }

        if (gpaStr.isEmpty()) {
            etGPA.setError("GPA is required");
            etGPA.requestFocus();
            return;
        }

        try {
            double gpa = Double.parseDouble(gpaStr);
            if (gpa < 0 || gpa > 4.0) {
                etGPA.setError("GPA must be between 0 and 4.0");
                etGPA.requestFocus();
                return;
            }

            // Validate ranking range if provided
            if (!rankingFrom.isEmpty() && !rankingTo.isEmpty()) {
                try {
                    int from = Integer.parseInt(rankingFrom);
                    int to = Integer.parseInt(rankingTo);
                    if (from > to) {
                        etRankingFrom.setError("From ranking should be less than To ranking");
                        return;
                    }
                } catch (NumberFormatException e) {
                    etRankingFrom.setError("Please enter valid ranking numbers");
                    return;
                }
            }

            // Update current user with new information
            if (currentUser != null) {
                currentUser.setCurrentInstitution(currentInstitution);
                currentUser.setGpa(gpa);
                currentUser.setMajor(major);
                currentUser.setTargetDegreeLevel(targetDegreeLevel);
                currentUser.setPreferredCountries(preferredCountries);
                
                // Combine ranking range into QS ranking field
                String qsRanking = "";
                if (!rankingFrom.isEmpty() && !rankingTo.isEmpty()) {
                    qsRanking = rankingFrom + "-" + rankingTo;
                }
                currentUser.setQsRanking(qsRanking);

                // Update user in database
                userViewModel.update(currentUser);

                // Update session
                sessionManager.updateUserSession(currentUser);

                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                
                // Add a small delay to ensure database operation completes
                new android.os.Handler().postDelayed(() -> {
                    // Additional validation or logging can be added here
                }, 100);

                // Check if this is called from settings or from registration
                boolean isFromRegistration = getIntent().getBooleanExtra("isFromRegistration", false);
                
                if (isFromRegistration) {
                    // Navigate to main activity for new users
                    Intent intent = new Intent(BuildAcademicProfileActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Just finish to return to previous activity (settings)
                    finish();
                }
            } else {
                Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            etGPA.setError("Invalid GPA format");
            etGPA.requestFocus();
        }
    }
} 