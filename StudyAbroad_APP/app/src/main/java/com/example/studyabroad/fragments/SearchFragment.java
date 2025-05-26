package com.example.studyabroad.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;
import com.example.studyabroad.adapters.CategorySelectionAdapter;
import com.example.studyabroad.adapters.UniversitySearchAdapter;
import com.example.studyabroad.model.University;
import com.example.studyabroad.viewmodel.SearchViewModel;
import com.example.studyabroad.ui.UniversityDetailActivity;
import com.example.studyabroad.models.ChatContext;
import com.example.studyabroad.utils.ChatContextManager;
import com.example.studyabroad.models.DreamSchool;
import com.example.studyabroad.database.DreamListDatabaseHelper;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchFragment extends Fragment implements 
        UniversitySearchAdapter.OnUniversityClickListener,
        UniversitySearchAdapter.OnProgramClickListener,
        UniversitySearchAdapter.OnAddToTimelineListener,
        UniversitySearchAdapter.OnChatClickListener,
        UniversitySearchAdapter.OnHeartClickListener {

    private static final String TAG = "SearchFragment";
    private SearchViewModel viewModel;
    private RecyclerView recyclerResults;
    private UniversitySearchAdapter adapter;
    private EditText editSearch;
    private ImageView iconFilter;
    private LinearLayout emptyStateLayout;
    private BottomSheetBehavior<LinearLayout> filterBottomSheetBehavior;
    private LinearLayout filterBottomSheet;
    
    // Filter components
    private Spinner spinnerCountry;
    private Button btnAddCountry;
    private TextView textSelectedCountries;
    private Button btnClearCountries;
    private Set<String> selectedCountries = new HashSet<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        
        // Initialize views
        editSearch = view.findViewById(R.id.editTextSearch);
        iconFilter = view.findViewById(R.id.iconFilter);
        recyclerResults = view.findViewById(R.id.recyclerViewSearchResults);
        emptyStateLayout = view.findViewById(R.id.layoutEmptyState);
        filterBottomSheet = view.findViewById(R.id.bottomSheetFilter);
        
        // Setup filter bottom sheet
        setupFilterBottomSheet();
        
        // Setup search results list
        adapter = new UniversitySearchAdapter(getContext(), this, this);
        adapter.setOnAddToTimelineListener(this);
        adapter.setOnChatClickListener(this);
        adapter.setOnHeartClickListener(this);
        recyclerResults.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerResults.setAdapter(adapter);
        
        // Setup search box listener
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
            }
        });
        
        // Setup filter button click event
        iconFilter.setOnClickListener(v -> showFilterDialog());
        
        // Show initial layout elements, hide search results
        showInitialView();
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Observe search results changes
        viewModel.getUniversities().observe(getViewLifecycleOwner(), universities -> {
            Log.d(TAG, "Received search results, total: " + (universities != null ? universities.size() : 0));
            updateSearchResults(universities);
        });
        
        // Initial load of all universities
        Log.d(TAG, "Initial load of all universities");
        viewModel.loadAllUniversities();
    }
    
    /**
     * Perform search
     */
    private void performSearch(String query) {
        Log.d(TAG, "Performing search: " + query);
        if (query.isEmpty()) {
            showInitialView();
        } else {
            viewModel.searchUniversities(query);
            showSearchResultsView();
        }
    }
    
    /**
     * Show initial view (recommendations and other content)
     */
    private void showInitialView() {
        // Show recommendation content and categories, hide search results
        if (getView() != null) {
            View recentSearchesSection = getView().findViewById(R.id.textViewRecentSearches);
            View categoriesSection = getView().findViewById(R.id.textViewPopularCategories);
            View destinationsSection = getView().findViewById(R.id.textViewPopularDestinations);
            
            if (recentSearchesSection != null) recentSearchesSection.setVisibility(View.VISIBLE);
            if (categoriesSection != null) categoriesSection.setVisibility(View.VISIBLE);
            if (destinationsSection != null) destinationsSection.setVisibility(View.VISIBLE);
            
            recyclerResults.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
    
    /**
     * Show search results view
     */
    private void showSearchResultsView() {
        // Hide recommendation content and categories, show search results
        if (getView() != null) {
            View recentSearchesSection = getView().findViewById(R.id.textViewRecentSearches);
            View categoriesSection = getView().findViewById(R.id.textViewPopularCategories);
            View destinationsSection = getView().findViewById(R.id.textViewPopularDestinations);
            
            if (recentSearchesSection != null) recentSearchesSection.setVisibility(View.GONE);
            if (categoriesSection != null) categoriesSection.setVisibility(View.GONE);
            if (destinationsSection != null) destinationsSection.setVisibility(View.GONE);
            
            recyclerResults.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * Setup filter bottom sheet
     */
    private void setupFilterBottomSheet() {
        if (filterBottomSheet != null) {
            filterBottomSheetBehavior = BottomSheetBehavior.from(filterBottomSheet);
            filterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            
            // Initialize filter components
            spinnerCountry = filterBottomSheet.findViewById(R.id.spinnerCountry);
            btnAddCountry = filterBottomSheet.findViewById(R.id.btnAddCountry);
            textSelectedCountries = filterBottomSheet.findViewById(R.id.textSelectedCountries);
            btnClearCountries = filterBottomSheet.findViewById(R.id.btnClearCountries);
            
            // Setup country spinner
            setupCountrySpinner();
            
            // Setup close button
            ImageView btnCloseFilter = filterBottomSheet.findViewById(R.id.btnCloseFilter);
            if (btnCloseFilter != null) {
                btnCloseFilter.setOnClickListener(v -> hideFilterDialog());
            }
            
            // Setup apply filters button
            View btnApplyFilters = filterBottomSheet.findViewById(R.id.btnApplyFilters);
            if (btnApplyFilters != null) {
                btnApplyFilters.setOnClickListener(v -> {
                    applyFilters();
                    hideFilterDialog();
                });
            }
            
            // Setup reset filters button
            View btnResetFilters = filterBottomSheet.findViewById(R.id.btnResetFilters);
            if (btnResetFilters != null) {
                btnResetFilters.setOnClickListener(v -> resetFilters());
            }
            
            // Setup "More..." chip click
            Chip chipMore = filterBottomSheet.findViewById(R.id.chipMore);
            if (chipMore != null) {
                chipMore.setOnClickListener(v -> showCategorySelectionDialog());
            }
        }
    }
    
    /**
     * Setup country spinner with multiple selection capability
     */
    private void setupCountrySpinner() {
        if (spinnerCountry != null && btnAddCountry != null && textSelectedCountries != null && btnClearCountries != null) {
            // Get countries array from resources
            String[] countries = getResources().getStringArray(R.array.countries);
            
            // Create adapter for spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_spinner_item, countries);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCountry.setAdapter(adapter);
            
            // Setup add country button
            btnAddCountry.setOnClickListener(v -> {
                String selectedCountry = (String) spinnerCountry.getSelectedItem();
                if (selectedCountry != null && !selectedCountry.equals("All Countries") && 
                    !selectedCountries.contains(selectedCountry)) {
                    selectedCountries.add(selectedCountry);
                    updateSelectedCountriesDisplay();
                }
            });
            
            // Setup clear countries button
            btnClearCountries.setOnClickListener(v -> {
                selectedCountries.clear();
                updateSelectedCountriesDisplay();
            });
        }
    }
    
    /**
     * Update the display of selected countries
     */
    private void updateSelectedCountriesDisplay() {
        if (textSelectedCountries != null && btnClearCountries != null) {
            if (selectedCountries.isEmpty()) {
                textSelectedCountries.setText("");
                textSelectedCountries.setHint("Selected countries will appear here");
                btnClearCountries.setVisibility(View.GONE);
            } else {
                String countriesText = String.join(", ", selectedCountries);
                textSelectedCountries.setText(countriesText);
                textSelectedCountries.setHint("");
                btnClearCountries.setVisibility(View.VISIBLE);
            }
        }
    }
    
    /**
     * Show filter dialog
     */
    private void showFilterDialog() {
        Log.d(TAG, "Show filter dialog");
        if (filterBottomSheetBehavior != null) {
            filterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
    
    /**
     * Hide filter dialog
     */
    private void hideFilterDialog() {
        if (filterBottomSheetBehavior != null) {
            filterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }
    
    /**
     * Apply filters
     */
    private void applyFilters() {
        Log.d(TAG, "Apply filter conditions");
        // Get filter values and apply them
        // For now, just show a toast
        Toast.makeText(getContext(), "Filters applied", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Reset filters
     */
    private void resetFilters() {
        Log.d(TAG, "Reset filter conditions");
        // Reset all filter controls to default values
        selectedCountries.clear();
        updateSelectedCountriesDisplay();
        if (spinnerCountry != null) {
            spinnerCountry.setSelection(0); // Select "All Countries"
        }
        Toast.makeText(getContext(), "Filters reset", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Show category selection dialog with alphabetical organization
     */
    private void showCategorySelectionDialog() {
        // Get all categories from resources
        String[] categoriesArray = getResources().getStringArray(R.array.program_categories);
        List<String> categories = new ArrayList<>();
        for (String category : categoriesArray) {
            categories.add(category);
        }
        
        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_category_selection, null);
        builder.setView(dialogView);
        
        AlertDialog dialog = builder.create();
        
        // Initialize dialog components
        EditText editTextSearch = dialogView.findViewById(R.id.editTextCategorySearch);
        LinearLayout layoutAlphabetTabs = dialogView.findViewById(R.id.layoutAlphabetTabs);
        RecyclerView recyclerViewCategories = dialogView.findViewById(R.id.recyclerViewCategories);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelCategory);
        Button btnSelect = dialogView.findViewById(R.id.btnSelectCategory);
        
        // Setup category adapter
        CategorySelectionAdapter categoryAdapter = new CategorySelectionAdapter(categories, 
            selectedCategories -> {
                // Handle category selection changes
                Log.d(TAG, "Selected categories: " + selectedCategories.size());
            });
        
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCategories.setAdapter(categoryAdapter);
        
        // Create alphabet tabs
        createAlphabetTabs(layoutAlphabetTabs, categoryAdapter);
        
        // Setup search functionality
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                categoryAdapter.filterBySearch(s.toString());
            }
        });
        
        // Setup button listeners
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnSelect.setOnClickListener(v -> {
            Set<String> selectedCategories = categoryAdapter.getSelectedCategories();
            // Apply selected categories to filter
            applySelectedCategories(selectedCategories);
            dialog.dismiss();
        });
        
        dialog.show();
    }
    
    /**
     * Create alphabet tabs for category filtering
     */
    private void createAlphabetTabs(LinearLayout layoutAlphabetTabs, CategorySelectionAdapter adapter) {
        // Get unique first letters from categories
        String[] categoriesArray = getResources().getStringArray(R.array.program_categories);
        Set<String> letters = new HashSet<>();
        for (String category : categoriesArray) {
            if (!category.isEmpty()) {
                letters.add(String.valueOf(category.charAt(0)).toUpperCase());
            }
        }
        
        // Add "All" tab
        Button allButton = createAlphabetButton("All");
        allButton.setOnClickListener(v -> adapter.filterByLetter("All"));
        layoutAlphabetTabs.addView(allButton);
        
        // Add letter tabs
        List<String> sortedLetters = new ArrayList<>(letters);
        java.util.Collections.sort(sortedLetters);
        
        for (String letter : sortedLetters) {
            Button letterButton = createAlphabetButton(letter);
            letterButton.setOnClickListener(v -> adapter.filterByLetter(letter));
            layoutAlphabetTabs.addView(letterButton);
        }
    }
    
    /**
     * Create alphabet button for category filtering
     */
    private Button createAlphabetButton(String text) {
        Button button = new Button(getContext());
        button.setText(text);
        button.setTextSize(12);
        button.setPadding(24, 12, 24, 12);
        
        // Set button style
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 0, 8, 0);
        button.setLayoutParams(params);
        
        return button;
    }
    
    /**
     * Apply selected categories to the filter
     */
    private void applySelectedCategories(Set<String> selectedCategories) {
        Log.d(TAG, "Applying selected categories: " + selectedCategories);
        // Here you can implement the logic to apply the selected categories to your search filter
        if (!selectedCategories.isEmpty()) {
            String categoriesText = String.join(", ", selectedCategories);
            Toast.makeText(getContext(), "Selected categories: " + categoriesText, Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Update search results
     */
    private void updateSearchResults(List<University> universities) {
        if (universities == null || universities.isEmpty()) {
            recyclerResults.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            adapter.setUniversities(universities);
            recyclerResults.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUniversityClick(University university) {
        Log.d(TAG, "University clicked: " + university.getName());
        // Navigate to university detail page
        Intent intent = new Intent(getActivity(), UniversityDetailActivity.class);
        intent.putExtra("university_id", university.getId());
        intent.putExtra("university_name", university.getName());
        startActivity(intent);
    }

    @Override
    public void onProgramClick(com.example.studyabroad.model.Program program) {
        Log.d(TAG, "Program clicked: " + program.getName());
        
        // Get university name - search from currently displayed university list
        final String[] universityNameArray = {"University Name"};
        List<University> currentUniversities = viewModel.getUniversities().getValue();
        if (currentUniversities != null) {
            for (University university : currentUniversities) {
                if (university.getAdditionalInfo() != null && 
                    university.getAdditionalInfo().containsKey("programs")) {
                    @SuppressWarnings("unchecked")
                    List<com.example.studyabroad.model.Program> programs = 
                        (List<com.example.studyabroad.model.Program>) university.getAdditionalInfo().get("programs");
                    
                    if (programs != null) {
                        for (com.example.studyabroad.model.Program p : programs) {
                            if (p.getId().equals(program.getId())) {
                                universityNameArray[0] = university.getName();
                                break;
                            }
                        }
                    }
                }
                if (!universityNameArray[0].equals("University Name")) break;
            }
        }
        final String universityName = universityNameArray[0];
        
        // Asynchronously get complete program data
        new Thread(() -> {
            try {
                com.example.studyabroad.database.AppDatabase db = com.example.studyabroad.database.AppDatabase.getInstance(getContext());
                long programId = Long.parseLong(program.getId());
                com.example.studyabroad.database.entity.Program dbProgram = db.programDao().getProgramByIdSync(programId);
                
                if (dbProgram != null) {
                    // Start Activity in main thread
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Intent intent = new Intent(getActivity(), com.example.studyabroad.ui.ProgramDetailActivity.class);
                            intent.putExtra("program_name", dbProgram.getName());
                            intent.putExtra("university_name", universityName);
                            intent.putExtra("category", dbProgram.getCategory());
                            intent.putExtra("degree_level", dbProgram.getDegreeLevel());
                            intent.putExtra("duration", dbProgram.getDuration());
                            intent.putExtra("mode", dbProgram.getMode());
                            intent.putExtra("location", dbProgram.getLocation());
                            intent.putExtra("intake_date", dbProgram.getIntakeDate());
                            intent.putExtra("application_deadline", dbProgram.getApplicationDeadline());
                            intent.putExtra("tuition_fee", dbProgram.getTuitionFee());
                            intent.putExtra("currency", dbProgram.getCurrency());
                            intent.putExtra("language", dbProgram.getLanguage());
                            intent.putExtra("description", dbProgram.getDescription());
                            intent.putExtra("min_gpa", dbProgram.getMinGPA());
                            intent.putExtra("entry_requirements", dbProgram.getEntryRequirements());
                            intent.putExtra("career_prospects", dbProgram.getCareerProspects());
                            intent.putExtra("is_scholarship_available", dbProgram.isScholarshipAvailable());
                            intent.putExtra("faculty_name", dbProgram.getFacultyName());
                            intent.putExtra("program_url", dbProgram.getProgramUrl());
                            startActivity(intent);
                        });
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to get program details", e);
                // If retrieval fails, use original data
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Intent intent = new Intent(getActivity(), com.example.studyabroad.ui.ProgramDetailActivity.class);
                        intent.putExtra("program_name", program.getName());
                        intent.putExtra("university_name", universityName);
                        intent.putExtra("category", program.getFieldOfStudy());
                        intent.putExtra("degree_level", program.getDegreeType());
                        intent.putExtra("duration", program.getDurationYears() + " years");
                        intent.putExtra("mode", "On campus");
                        intent.putExtra("location", "Campus location");
                        intent.putExtra("intake_date", "February");
                        intent.putExtra("application_deadline", "31 August 2025");
                        intent.putExtra("tuition_fee", program.getTuitionFeeInternational());
                        intent.putExtra("currency", program.getCurrency());
                        intent.putExtra("language", "English");
                        intent.putExtra("description", program.getDescription());
                        intent.putExtra("min_gpa", program.getMinimumGPA());
                        intent.putExtra("entry_requirements", "Bachelor's degree with relevant background");
                        intent.putExtra("career_prospects", "Excellent career opportunities");
                        intent.putExtra("is_scholarship_available", program.hasScholarships());
                        intent.putExtra("faculty_name", "Faculty Information");
                        intent.putExtra("program_url", "university.edu/programs/" + program.getId());
                        startActivity(intent);
                    });
                }
            }
        }).start();
    }

    @Override
    public void onAddToTimeline(com.example.studyabroad.model.Program program, String universityName) {
        Log.d(TAG, "Adding to timeline: " + program.getName() + " - " + universityName);
        
        // Asynchronously get complete program data and add to timeline
        new Thread(() -> {
            try {
                com.example.studyabroad.database.AppDatabase db = com.example.studyabroad.database.AppDatabase.getInstance(getContext());
                long programId = Long.parseLong(program.getId());
                com.example.studyabroad.database.entity.Program dbProgram = db.programDao().getProgramByIdSync(programId);
                
                if (dbProgram != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        addProgramToTimeline(dbProgram, universityName);
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to get program data", e);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Failed to add to timeline", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }
    
    private void addProgramToTimeline(com.example.studyabroad.database.entity.Program program, String universityName) {
        try {
            Log.d(TAG, "Starting to add to timeline: " + program.getName() + ", deadline: " + program.getApplicationDeadline());
            
            // Parse application deadline
            java.util.Date applicationDeadline = parseApplicationDeadline(program.getApplicationDeadline());
            
            if (applicationDeadline != null) {
                Log.d(TAG, "Parsed date: " + applicationDeadline.toString());
                
                // Create timeline item
                com.example.studyabroad.models.TimelineItem timelineItem = new com.example.studyabroad.models.TimelineItem(
                    program.getName() + " - Application Deadline",
                    "Application deadline for " + program.getName() + " at " + universityName,
                    applicationDeadline,
                    com.example.studyabroad.models.TimelineItem.STATUS_PENDING,
                    String.valueOf(program.getUniversityId()),
                    universityName,
                    program.getName()
                );
                
                Log.d(TAG, "Created TimelineItem: " + timelineItem.getTitle());
                
                // Save to database
                com.example.studyabroad.repositories.TimelineRepository timelineRepository = 
                    com.example.studyabroad.repositories.TimelineRepository.getInstance(getContext());
                
                // Check uniqueness
                if (timelineRepository.isUniversityProgramExists(universityName, program.getName())) {
                    Toast.makeText(getContext(), "Application for this university and program already exists", Toast.LENGTH_LONG).show();
                    return;
                }
                
                long id = timelineRepository.saveTimelineItem(timelineItem);
                
                Log.d(TAG, "Save result ID: " + id);
                
                if (id > 0) {
                    // Verify save was successful
                    java.util.List<com.example.studyabroad.models.TimelineItem> allItems = timelineRepository.getAllTimelineItems();
                    Log.d(TAG, "Total timeline items in database: " + allItems.size());
                    
                    Toast.makeText(getContext(), "Added to timeline", Toast.LENGTH_SHORT).show();
                } else if (id == -1) {
                    Toast.makeText(getContext(), "Application for this university and program already exists", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Failed to add to timeline", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "Unable to parse application deadline: " + program.getApplicationDeadline());
                Toast.makeText(getContext(), "Unable to parse application deadline", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to add to timeline", e);
            Toast.makeText(getContext(), "Failed to add to timeline", Toast.LENGTH_SHORT).show();
        }
    }
    
    private java.util.Date parseApplicationDeadline(String deadlineStr) {
        if (deadlineStr == null || deadlineStr.isEmpty()) {
            return null;
        }
        
        try {
            // Try to parse different date formats
            java.text.SimpleDateFormat[] formats = {
                new java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale.ENGLISH),
                new java.text.SimpleDateFormat("d MMMM yyyy", java.util.Locale.ENGLISH),
                new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH),
                new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ENGLISH),
                new java.text.SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.ENGLISH)
            };
            
            for (java.text.SimpleDateFormat format : formats) {
                try {
                    return format.parse(deadlineStr);
                } catch (java.text.ParseException e) {
                    // Continue trying next format
                }
            }
            
            // If all formats fail, return a default date (one year later)
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.add(java.util.Calendar.YEAR, 1);
            return calendar.getTime();
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse date: " + deadlineStr, e);
            return null;
        }
    }

    @Override
    public void onChatClick(com.example.studyabroad.model.Program program, String universityName) {
        Log.d(TAG, "Chat clicked for program: " + program.getName() + " at " + universityName);
        
        // Create chat context for the program
        ChatContext chatContext = ChatContext.createProgramContext(
            universityName,
            program.getName(),
            program.getId(),
            program.getFieldOfStudy(),
            program.getDegreeType(),
            program.getTuitionFeeInternational(),
            program.getCurrency(),
            "Campus location", // You might want to get this from university data
            "Country" // You might want to get this from university data
        );
        
        // Set the context in ChatContextManager
        ChatContextManager.getInstance().setContext(chatContext);
        
        // Navigate to chat fragment
        if (getActivity() instanceof com.example.studyabroad.MainActivity) {
            com.example.studyabroad.MainActivity mainActivity = (com.example.studyabroad.MainActivity) getActivity();
            // Use the bottom navigation to switch to chat tab
            mainActivity.findViewById(R.id.bottomNavigationView)
                .findViewById(R.id.navigation_chat).performClick();
        }
    }

    @Override
    public void onHeartClick(com.example.studyabroad.model.Program program, String universityName, boolean isAdding) {
        Log.d(TAG, "Heart clicked for program: " + program.getName() + " at " + universityName);
        
        DreamListDatabaseHelper dreamListDb = DreamListDatabaseHelper.getInstance(getContext());
        
        // Check if already in dream list
        boolean isAlreadyDream = dreamListDb.isDreamSchool(universityName, program.getName());
        
        if (isAlreadyDream) {
            // Remove from dream list
            boolean removed = dreamListDb.removeDreamSchool(universityName, program.getName());
            if (removed) {
                Toast.makeText(getContext(), "Removed from dream list", Toast.LENGTH_SHORT).show();
                // Update heart icon to outline
                updateHeartIcon(program, universityName, false);
            } else {
                Toast.makeText(getContext(), "Failed to remove from dream list", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add to dream list
            DreamSchool dreamSchool = new DreamSchool(
                universityName,
                program.getName(),
                String.valueOf(program.getUniversityId()),
                program.getId()
            );
            
            // Set additional information if available
            dreamSchool.setTuitionFee(program.getCurrency() + " $" + String.format("%.0f", program.getTuitionFeeInternational()));
            dreamSchool.setApplicationDeadline("31 August 2025"); // You might want to get real deadline
            
            long id = dreamListDb.addDreamSchool(dreamSchool);
            if (id > 0) {
                Toast.makeText(getContext(), "Added to dream list", Toast.LENGTH_SHORT).show();
                // Update heart icon to filled
                updateHeartIcon(program, universityName, true);
            } else {
                Toast.makeText(getContext(), "Already in dream list", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void updateHeartIcon(com.example.studyabroad.model.Program program, String universityName, boolean isFavorited) {
        // This method would update the heart icon in the adapter
        // For now, we'll just notify the adapter to refresh
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
} 