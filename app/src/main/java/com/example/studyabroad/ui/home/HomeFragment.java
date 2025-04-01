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

import com.example.studyabroad.adapters.SchoolAdapter;
import com.example.studyabroad.adapters.SpotlightAdapter;
import com.example.studyabroad.databinding.FragmentHomeBinding;
import com.example.studyabroad.database.entity.University;
import com.example.studyabroad.database.entity.User;
import com.example.studyabroad.models.School;
import com.example.studyabroad.models.Spotlight;
import com.example.studyabroad.viewmodel.UniversityViewModel;
import com.example.studyabroad.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private UserViewModel userViewModel;
    private UniversityViewModel universityViewModel;
    private SchoolAdapter schoolAdapter;
    private SpotlightAdapter spotlightAdapter;

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
        
        // Setup UI
        setupSchoolsRecyclerView();
        setupSpotlightsRecyclerView();
        
        // Observe data
        observeData();
    }
    
    private void setupSchoolsRecyclerView() {
        schoolAdapter = new SchoolAdapter(getDummySchools());
        binding.recyclerViewSchools.setAdapter(schoolAdapter);
        binding.recyclerViewSchools.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }
    
    private void setupSpotlightsRecyclerView() {
        spotlightAdapter = new SpotlightAdapter(getDummySpotlights());
        binding.recyclerViewSpotlights.setAdapter(spotlightAdapter);
        binding.recyclerViewSpotlights.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
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
                schoolAdapter.notifyDataSetChanged();
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
                com.example.studyabroad.R.drawable.summer_camp_icon
        ));
        
        spotlights.add(new Spotlight(
                "New grants for STEM majors!",
                com.example.studyabroad.R.color.light_orange,
                com.example.studyabroad.R.drawable.orange_planet
        ));
        
        spotlights.add(new Spotlight(
                "Upcoming application deadlines",
                com.example.studyabroad.R.color.blue_primary,
                com.example.studyabroad.R.drawable.ic_school
        ));
        
        return spotlights;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}