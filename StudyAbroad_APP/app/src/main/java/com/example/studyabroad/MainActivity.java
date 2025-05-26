package com.example.studyabroad;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyabroad.databinding.ActivityMainBinding;
import com.example.studyabroad.fragments.ChatFragment;
import com.example.studyabroad.ui.home.HomeFragment;
import com.example.studyabroad.fragments.ProfileFragment;
import com.example.studyabroad.fragments.SearchFragment;
import com.example.studyabroad.fragments.TimelineFragment;
import com.example.studyabroad.utils.DatabaseSeeder;
import com.example.studyabroad.utils.SessionManager;
import com.example.studyabroad.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        sessionManager = SessionManager.getInstance(this);
        
        // Load user from session
        userViewModel.setCurrentUser(sessionManager.getUser());
        
        setupBottomNavigation();
        
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
        
        DatabaseSeeder.forceReseedDatabase(this);
    }
    
    private void setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.navigation_search) {
                fragment = new SearchFragment();
            } else if (itemId == R.id.navigation_timeline) {
                fragment = new TimelineFragment();
            } else if (itemId == R.id.navigation_chat) {
                fragment = new ChatFragment();
            } else if (itemId == R.id.navigation_profile) {
                fragment = new ProfileFragment();
            }
            
            return loadFragment(fragment);
        });
    }
    
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}