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

import com.example.studyabroad.databinding.FragmentHomeBinding;
import com.example.studyabroad.database.entity.University;
import com.example.studyabroad.database.entity.User;
import com.example.studyabroad.viewmodel.UniversityViewModel;
import com.example.studyabroad.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private UserViewModel userViewModel;
    private UniversityViewModel universityViewModel;
    private DreamSchoolsAdapter dreamSchoolsAdapter;
    private SpotlightsAdapter spotlightsAdapter;

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
        setupDreamSchoolsRecyclerView();
        setupSpotlightsRecyclerView();
        
        // Observe data
        observeData();
    }
    
    private void setupDreamSchoolsRecyclerView() {
        dreamSchoolsAdapter = new DreamSchoolsAdapter();
        binding.recyclerViewDreamSchools.setAdapter(dreamSchoolsAdapter);
        binding.recyclerViewDreamSchools.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }
    
    private void setupSpotlightsRecyclerView() {
        spotlightsAdapter = new SpotlightsAdapter();
        binding.recyclerViewSpotlights.setAdapter(spotlightsAdapter);
        binding.recyclerViewSpotlights.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }
    
    private void observeData() {
        // Observe current user for personalized content
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.textViewGreeting.setText("Hello, " + user.getName() + "!");
                updateRecommendations(user);
            }
        });
        
        // Observe universities
        universityViewModel.getAllUniversities().observe(getViewLifecycleOwner(), universities -> {
            if (universities != null && !universities.isEmpty()) {
                dreamSchoolsAdapter.setUniversities(universities);
            }
        });
        
        // For spotlights, we're using sample data for the demo
        spotlightsAdapter.setSpotlights(getSampleSpotlights());
    }
    
    private void updateRecommendations(User user) {
        // In a real app, this would call an AI or recommendation service
        // For the demo, we'll just use sample data
        
        // Optional: Use these in a real implementation
        // String major = user.getMajor();
        // double gpa = user.getGpa();
    }
    
    private List<SpotlightItem> getSampleSpotlights() {
        List<SpotlightItem> spotlights = new ArrayList<>();
        
        SpotlightItem spotlight1 = new SpotlightItem();
        spotlight1.setTitle("Oxford Summer Camp");
        spotlight1.setImageResId(android.R.drawable.ic_dialog_map); // Placeholder
        spotlight1.setBackgroundColor("#4CAF50"); // Green
        
        SpotlightItem spotlight2 = new SpotlightItem();
        spotlight2.setTitle("New grants for STEM majors!");
        spotlight2.setImageResId(android.R.drawable.ic_dialog_info); // Placeholder
        spotlight2.setBackgroundColor("#FFC107"); // Amber
        
        SpotlightItem spotlight3 = new SpotlightItem();
        spotlight3.setTitle("Application deadlines coming up");
        spotlight3.setImageResId(android.R.drawable.ic_dialog_alert); // Placeholder
        spotlight3.setBackgroundColor("#F48FB1"); // Pink
        
        spotlights.add(spotlight1);
        spotlights.add(spotlight2);
        spotlights.add(spotlight3);
        
        return spotlights;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}