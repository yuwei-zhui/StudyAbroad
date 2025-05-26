package com.example.studyabroad.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studyabroad.R;
import com.example.studyabroad.adapters.ProgramAdapter;
import com.example.studyabroad.model.Program;
import com.example.studyabroad.model.University;
import com.example.studyabroad.viewmodel.UniversityViewModel;
import com.example.studyabroad.models.DreamSchool;
import com.example.studyabroad.database.DreamListDatabaseHelper;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * University detail page, displays university and program information
 */
public class UniversityDetailActivity extends AppCompatActivity implements ProgramAdapter.OnProgramClickListener {

    private UniversityViewModel viewModel;
    private long universityId;
    private long programId = -1;

    // UI components
    private MaterialToolbar toolbar;
    private ImageView universityLogo;
    private TextView universityName;
    private TextView universityLocation;
    private TextView universityRanking;
    private TextView universityWebsite;
    private ImageView imageViewHeart;
    private RecyclerView programsRecyclerView;
    private ProgramAdapter programAdapter;
    private Button btnAddToTimeline;
    
    // Dream list functionality
    private DreamListDatabaseHelper dreamListDb;
    private String currentUniversityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_detail);

        // Get university and possible program ID
        if (getIntent().hasExtra("university_id")) {
            universityId = getIntent().getLongExtra("university_id", -1);
        }
        if (getIntent().hasExtra("program_id")) {
            programId = getIntent().getLongExtra("program_id", -1);
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(UniversityViewModel.class);
        
        // Initialize dream list database
        dreamListDb = DreamListDatabaseHelper.getInstance(this);

        // Initialize UI components
        initViews();

        // Load university details
        if (universityId != -1) {
            loadUniversityDetails();
        } else {
            finish(); // If no valid university ID, end activity
        }
    }

    private void initViews() {
        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(""); // Remove title, keep only back arrow

        // University basic information components
        universityLogo = findViewById(R.id.imageUniversity);
        universityName = findViewById(R.id.textUniversityName);
        universityLocation = findViewById(R.id.textLocation);
        universityRanking = findViewById(R.id.textRanking);
        universityWebsite = findViewById(R.id.textWebsite);
        imageViewHeart = findViewById(R.id.imageViewHeart);

        // Program list
        programsRecyclerView = findViewById(R.id.recyclerPrograms);
        programAdapter = new ProgramAdapter(this);
        programsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        programsRecyclerView.setAdapter(programAdapter);

        // Add to timeline button
        btnAddToTimeline = findViewById(R.id.buttonAddToTimeline);
        btnAddToTimeline.setOnClickListener(v -> addToTimeline());
        
        // Heart button click listener
        imageViewHeart.setOnClickListener(v -> toggleDreamList());
    }

    private void loadUniversityDetails() {
        viewModel.getUniversityWithPrograms(universityId).observe(this, universityWithPrograms -> {
            if (universityWithPrograms != null) {
                // Update UI to display university information
                updateUniversityUI(universityWithPrograms);
                
                // Update program list
                if (universityWithPrograms.programs != null) {
                    List<Program> modelPrograms = convertToModelPrograms(universityWithPrograms.programs);
                    programAdapter.setPrograms(modelPrograms);
                    
                    // If there's a specific program ID, scroll to that program position
                    if (programId != -1) {
                        highlightProgram(modelPrograms, programId);
                    }
                }
            }
        });
    }

    /**
     * Convert database entities to UI models
     */
    private List<Program> convertToModelPrograms(List<com.example.studyabroad.database.entity.Program> programs) {
        List<Program> result = new ArrayList<>();
        
        for (com.example.studyabroad.database.entity.Program entity : programs) {
            Program model = new Program(
                    String.valueOf(entity.getId()),
                    entity.getName(),
                    String.valueOf(entity.getUniversityId()),
                    entity.getDegreeLevel(),
                    entity.getCategory()
            );
            
            // Set other properties
            model.setDurationYears(parseDurationYears(entity.getDuration()));
            model.setDurationMonths(0); // Simplified handling here
            model.setTuitionFeeInternational(entity.getTuitionFee());
            model.setCurrency(entity.getCurrency());
            model.setDescription(entity.getDescription());
            model.setLanguageOfInstruction(entity.getLanguage());
            
            result.add(model);
        }
        
        return result;
    }
    
    /**
     * Parse years from duration string
     */
    private int parseDurationYears(String duration) {
        if (duration == null || duration.isEmpty()) {
            return 0;
        }
        
        // Simple parsing, actual situation may need more complex logic
        if (duration.contains("1.5")) {
            return 1;
        } else if (duration.contains("2")) {
            return 2;
        } else if (duration.contains("3")) {
            return 3;
        } else if (duration.contains("4")) {
            return 4;
        } else if (duration.contains("1")) {
            return 1;
        }
        
        return 0;
    }
    
    /**
     * Highlight specific program
     */
    private void highlightProgram(List<Program> programs, long programId) {
        for (int i = 0; i < programs.size(); i++) {
            if (Long.parseLong(programs.get(i).getId()) == programId) {
                programsRecyclerView.scrollToPosition(i);
                // TODO: Add highlight effect
                break;
            }
        }
    }

    /**
     * Update UI to display university information
     */
    private void updateUniversityUI(com.example.studyabroad.models.UniversityWithPrograms universityWithPrograms) {
        com.example.studyabroad.database.entity.University entity = universityWithPrograms.university;
        
        currentUniversityName = entity.getName();
        universityName.setText(currentUniversityName);
        universityLocation.setText(entity.getCity() + ", " + entity.getCountry());
        
        // Display ranking
        if (entity.getQsRanking() > 0) {
            universityRanking.setVisibility(View.VISIBLE);
            universityRanking.setText(getString(R.string.ranking_format, entity.getQsRanking()));
        } else {
            universityRanking.setVisibility(View.GONE);
        }
        
        // Update heart icon based on dream list status
        updateHeartIcon();
        
                // Display website
        universityWebsite.setText(entity.getWebsite());

        // Load university logo
        if (entity.getLogoUrl() != null && !entity.getLogoUrl().isEmpty()) {
            Glide.with(this)
                 .load(entity.getLogoUrl())
                 .placeholder(R.drawable.placeholder_university)
                 .error(R.drawable.placeholder_university)
                 .into(universityLogo);
        } else {
            universityLogo.setImageResource(R.drawable.placeholder_university);
        }
    }

    /**
     * Add to timeline
     */
    private void addToTimeline() {
        // TODO: Implement add to timeline functionality
    }
    
    /**
     * Toggle dream list status
     */
    private void toggleDreamList() {
        if (currentUniversityName == null) {
            return;
        }
        
        // Check if university is already in dream list (using general program name)
        String generalProgramName = "General Interest";
        boolean isAlreadyDream = dreamListDb.isDreamSchool(currentUniversityName, generalProgramName);
        
        if (isAlreadyDream) {
            // Remove from dream list
            boolean removed = dreamListDb.removeDreamSchool(currentUniversityName, generalProgramName);
            if (removed) {
                Toast.makeText(this, "Removed from dream list", Toast.LENGTH_SHORT).show();
                updateHeartIcon();
            } else {
                Toast.makeText(this, "Failed to remove from dream list", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add to dream list
            DreamSchool dreamSchool = new DreamSchool(
                currentUniversityName,
                generalProgramName,
                String.valueOf(universityId),
                "general"
            );
            
            long id = dreamListDb.addDreamSchool(dreamSchool);
            if (id > 0) {
                Toast.makeText(this, "Added to dream list", Toast.LENGTH_SHORT).show();
                updateHeartIcon();
            } else {
                Toast.makeText(this, "Already in dream list", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * Update heart icon based on dream list status
     */
    private void updateHeartIcon() {
        if (currentUniversityName == null || imageViewHeart == null) {
            return;
        }
        
        String generalProgramName = "General Interest";
        boolean isDreamSchool = dreamListDb.isDreamSchool(currentUniversityName, generalProgramName);
        imageViewHeart.setImageResource(isDreamSchool ? R.drawable.ic_heart : R.drawable.ic_heart_outline);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgramClick(Program program) {
        // Can handle program click events here, such as displaying more details
    }
} 