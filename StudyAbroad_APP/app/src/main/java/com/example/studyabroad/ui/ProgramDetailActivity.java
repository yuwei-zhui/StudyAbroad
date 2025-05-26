package com.example.studyabroad.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studyabroad.R;
import com.example.studyabroad.databinding.ActivityProgramDetailBinding;

public class ProgramDetailActivity extends AppCompatActivity {

    private ActivityProgramDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgramDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up back button
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        // Get passed data
        Intent intent = getIntent();
        String programName = intent.getStringExtra("program_name");
        String universityName = intent.getStringExtra("university_name");
        String category = intent.getStringExtra("category");
        String degreeLevel = intent.getStringExtra("degree_level");
        String duration = intent.getStringExtra("duration");
        String mode = intent.getStringExtra("mode");
        String location = intent.getStringExtra("location");
        String intakeDate = intent.getStringExtra("intake_date");
        String applicationDeadline = intent.getStringExtra("application_deadline");
        double tuitionFee = intent.getDoubleExtra("tuition_fee", 0);
        String currency = intent.getStringExtra("currency");
        String language = intent.getStringExtra("language");
        String description = intent.getStringExtra("description");
        double minGPA = intent.getDoubleExtra("min_gpa", 0);
        String entryRequirements = intent.getStringExtra("entry_requirements");
        String careerProspects = intent.getStringExtra("career_prospects");
        boolean isScholarshipAvailable = intent.getBooleanExtra("is_scholarship_available", false);
        String facultyName = intent.getStringExtra("faculty_name");
        String programUrl = intent.getStringExtra("program_url");

        // Populate data
        populateData(programName, universityName, category, degreeLevel, duration, mode, location,
                intakeDate, applicationDeadline, tuitionFee, currency, language, description,
                minGPA, entryRequirements, careerProspects, isScholarshipAvailable, facultyName, programUrl);
    }

    private void populateData(String programName, String universityName, String category, String degreeLevel,
                             String duration, String mode, String location, String intakeDate,
                             String applicationDeadline, double tuitionFee, String currency, String language,
                             String description, double minGPA, String entryRequirements, String careerProspects,
                             boolean isScholarshipAvailable, String facultyName, String programUrl) {

        // Basic information
        binding.textViewProgramName.setText(programName != null ? programName : "N/A");
        binding.textViewUniversityName.setText(universityName != null ? universityName : "N/A");
        binding.textViewCategory.setText(category != null ? category : "N/A");
        binding.textViewDegreeLevel.setText(degreeLevel != null ? degreeLevel : "N/A");

        // Course details
        binding.textViewDuration.setText(duration != null ? duration : "N/A");
        binding.textViewMode.setText(mode != null ? mode : "N/A");
        binding.textViewLocation.setText(location != null ? location : "N/A");
        binding.textViewLanguage.setText(language != null ? language : "N/A");

        // Application information
        binding.textViewIntakeDate.setText(intakeDate != null ? intakeDate : "N/A");
        binding.textViewApplicationDeadline.setText(applicationDeadline != null ? applicationDeadline : "N/A");
        
        // Tuition fee information
        if (tuitionFee > 0 && currency != null) {
            binding.textViewTuitionFee.setText(currency + " $" + String.format("%.0f", tuitionFee));
        } else {
            binding.textViewTuitionFee.setText("Contact university for fee information");
        }

        // Entry requirements
        if (minGPA > 0) {
            binding.textViewMinGPA.setText(String.valueOf(minGPA));
        } else {
            binding.textViewMinGPA.setText("N/A");
        }
        binding.textViewEntryRequirements.setText(entryRequirements != null ? entryRequirements : "N/A");

        // Other information
        binding.textViewDescription.setText(description != null ? description : "N/A");
        binding.textViewCareerProspects.setText(careerProspects != null ? careerProspects : "N/A");
        binding.textViewFacultyName.setText(facultyName != null ? facultyName : "N/A");
        
        // Scholarship information
        binding.textViewScholarshipAvailable.setText(isScholarshipAvailable ? "Available" : "Not Available");
        binding.textViewScholarshipAvailable.setTextColor(getResources().getColor(
                isScholarshipAvailable ? R.color.success_green : R.color.text_secondary, null));

        // Set button click events
        binding.buttonApplyNow.setOnClickListener(v -> {
            if (programUrl != null && !programUrl.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + programUrl));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, "Program URL not available", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonContactUniversity.setOnClickListener(v -> {
            // Here you can implement contact university functionality, such as sending email
            Toast.makeText(this, "Contact feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }
} 