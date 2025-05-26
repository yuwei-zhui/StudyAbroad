package com.example.studyabroad.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studyabroad.MainActivity;
import com.example.studyabroad.R;
import com.example.studyabroad.adapters.SpotlightAdapter;
import com.example.studyabroad.adapters.DreamSchoolAdapter;
import com.example.studyabroad.databinding.FragmentHomeBinding;
import com.example.studyabroad.database.entity.University;
import com.example.studyabroad.database.entity.User;
import com.example.studyabroad.models.School;
import com.example.studyabroad.models.Spotlight;
import com.example.studyabroad.models.DreamSchool;
import com.example.studyabroad.database.DreamListDatabaseHelper;
import com.example.studyabroad.viewmodel.UniversityViewModel;
import com.example.studyabroad.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private UserViewModel userViewModel;
    private UniversityViewModel universityViewModel;
    private SpotlightAdapter spotlightAdapter;
    private DreamSchoolAdapter dreamSchoolAdapter;
    private DreamListDatabaseHelper dreamListDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize view models
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        universityViewModel = new ViewModelProvider(requireActivity()).get(UniversityViewModel.class);
        
        // Initialize dream list database
        dreamListDb = DreamListDatabaseHelper.getInstance(requireContext());
        
        // Setup UI
        setupDreamSchoolsRecyclerView();
        setupSpotlightsRecyclerView();
        setupSearchButton();
        
        // Observe data
        observeData();
        
        // Load dream schools
        loadDreamSchools();
    }
    
    private void setupDreamSchoolsRecyclerView() {
        dreamSchoolAdapter = new DreamSchoolAdapter(requireContext());
        dreamSchoolAdapter.setOnDreamSchoolClickListener(new DreamSchoolAdapter.OnDreamSchoolClickListener() {
            @Override
            public void onDreamSchoolClick(DreamSchool dreamSchool) {
                // Handle dream school click - could navigate to university detail
            }

            @Override
            public void onRemoveClick(DreamSchool dreamSchool) {
                // Remove from dream list
                boolean removed = dreamListDb.removeDreamSchool(dreamSchool.getUniversityName(), dreamSchool.getProgramName());
                if (removed) {
                    dreamSchoolAdapter.removeDreamSchool(dreamSchool);
                    // Show toast or feedback
                }
            }
        });
        
        binding.recyclerViewSchools.setAdapter(dreamSchoolAdapter);
        binding.recyclerViewSchools.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }
    
    private void setupSpotlightsRecyclerView() {
        spotlightAdapter = new SpotlightAdapter(getDummySpotlights());
        binding.recyclerViewSpotlights.setAdapter(spotlightAdapter);
        binding.recyclerViewSpotlights.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }
    
    private void setupSearchButton() {
        binding.cardViewSearch.setOnClickListener(v -> {
            // Navigate to search fragment
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                // Use the bottom navigation to switch to search tab
                mainActivity.findViewById(R.id.bottomNavigationView)
                    .findViewById(R.id.navigation_search).performClick();
            }
        });
    }
    
    private void observeData() {
        // Observe current user for personalized content
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                updateRecommendations(user);
            }
        });
        
        // Observe universities
        universityViewModel.getAllUniversities().observe(getViewLifecycleOwner(), universities -> {
            if (universities != null && !universities.isEmpty()) {
                List<School> schools = convertToSchools(universities);
                // Since we're using dreamSchoolAdapter, we don't need to update schoolAdapter
                // The dream schools are loaded separately in loadDreamSchools()
            }
        });
    }
    
    private List<School> convertToSchools(List<University> universities) {
        List<School> schools = new ArrayList<>();
        for (University university : universities) {
            School school = new School(
                university.getName(),
                university.getCity() + ", " + university.getCountry(),
                university.getQsRanking(),
                getColorByUniversity(university.getName()),
                com.example.studyabroad.R.drawable.ic_school
            );
            schools.add(school);
        }
        return schools;
    }
    
    private int getColorByUniversity(String name) {
        if (name.contains("Melbourne")) {
            return com.example.studyabroad.R.color.orange_primary;
        } else if (name.contains("Imperial")) {
            return com.example.studyabroad.R.color.blue_primary;
        } else if (name.contains("Hongkong")) {
            return com.example.studyabroad.R.color.light_orange;
        } else {
            return com.example.studyabroad.R.color.colorPrimary;
        }
    }
    
    private void updateRecommendations(User user) {
        // In a real app, this would call an AI or recommendation service
        // For the demo, we'll just use sample data
    }
    
    private void loadDreamSchools() {
        // Load dream schools from database in background thread
        new Thread(() -> {
            List<DreamSchool> dreamSchools = dreamListDb.getAllDreamSchools();
            
            // Update UI on main thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (dreamSchoolAdapter != null) {
                        dreamSchoolAdapter.setDreamSchools(dreamSchools);
                    }
                });
            }
        }).start();
    }
    
    private List<School> getDummySchools() {
        List<School> schools = new ArrayList<>();
        
        schools.add(new School(
                "The University of Melbourne",
                "Melbourne, Australia",
                13,
                com.example.studyabroad.R.color.orange_primary,
                com.example.studyabroad.R.drawable.ic_school
        ));
        
        schools.add(new School(
                "Imperial College London",
                "London, United Kingdom",
                8,
                com.example.studyabroad.R.color.blue_primary,
                com.example.studyabroad.R.drawable.ic_school
        ));
        
        schools.add(new School(
                "Stanford University",
                "California, USA",
                3,
                com.example.studyabroad.R.color.colorPrimary,
                com.example.studyabroad.R.drawable.ic_school
        ));
        
        return schools;
    }
    
    private List<Spotlight> getDummySpotlights() {
        List<Spotlight> spotlights = new ArrayList<>();
        
        spotlights.add(new Spotlight(
                "Oxford Summer Camp",
                com.example.studyabroad.R.color.light_green,
                com.example.studyabroad.R.drawable.home_green
        ));
        
        spotlights.add(new Spotlight(
                "New grants for STEM majors!",
                com.example.studyabroad.R.color.light_orange,
                com.example.studyabroad.R.drawable.home_blue
        ));
        
        spotlights.add(new Spotlight(
                "Upcoming application deadlines",
                com.example.studyabroad.R.color.blue_primary,
                com.example.studyabroad.R.drawable.home_pink
        ));
        
        return spotlights;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh dream schools when fragment becomes visible
        loadDreamSchools();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}