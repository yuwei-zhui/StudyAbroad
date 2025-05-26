package com.example.studyabroad.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.example.studyabroad.R;
import com.example.studyabroad.database.entity.User;
import com.example.studyabroad.utils.SessionManager;
import com.example.studyabroad.utils.UserProfileManager;

public class ProfileFragment extends Fragment {

    private static final String PREFS_NAME = "UserPrefs";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    // UI components
    private ShapeableImageView imageViewAvatar;
    private ImageView imageViewEditAvatar;
    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewPhone;
    private CardView cardViewPersonalInfo;
    private CardView cardViewAcademicProfile;
    private CardView cardViewNotifications;
    private CardView cardViewLanguage;
    private CardView cardViewTheme;
    private SwitchMaterial switchNotifications;
    private TextView textViewLanguage;
    private TextView textViewTheme;

    // Data
    private UserProfileManager profileManager;
    private SessionManager sessionManager;
    private SharedPreferences sharedPreferences;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize components
        initViews(view);
        initData();
        setupListeners();
        
        // Set up image picker launcher
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        imageViewAvatar.setImageURI(selectedImageUri);
                        profileManager.setProfilePicture(selectedImageUri.toString());
                    }
                }
            }
        );
        
        // Load user profile data
        loadUserProfile();
    }
    
    private void initViews(View view) {
        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);
        imageViewEditAvatar = view.findViewById(R.id.imageViewEditAvatar);
        textViewName = view.findViewById(R.id.textViewName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewPhone = view.findViewById(R.id.textViewPhone);
        cardViewPersonalInfo = view.findViewById(R.id.cardViewPersonalInfo);
        cardViewAcademicProfile = view.findViewById(R.id.cardViewAcademicProfile);
        cardViewNotifications = view.findViewById(R.id.cardViewNotifications);
        cardViewLanguage = view.findViewById(R.id.cardViewLanguage);
        cardViewTheme = view.findViewById(R.id.cardViewTheme);
        switchNotifications = view.findViewById(R.id.switchNotifications);
        textViewLanguage = view.findViewById(R.id.textViewLanguage);
        textViewTheme = view.findViewById(R.id.textViewTheme);
    }
    
    private void initData() {
        Context context = requireContext();
        profileManager = UserProfileManager.getInstance(context);
        sessionManager = SessionManager.getInstance(context);
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    private void setupListeners() {
        // Avatar edit button
        imageViewEditAvatar.setOnClickListener(v -> openImagePicker());
        
        // Personal information card
        cardViewPersonalInfo.setOnClickListener(v -> {
            // Use fragment transaction since NavController is not available in MainActivity
            Fragment personalInfoEditFragment = new com.example.studyabroad.fragments.PersonalInfoEditFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, personalInfoEditFragment)
                    .addToBackStack(null)
                    .commit();
        });
        
        // Academic profile card
        cardViewAcademicProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), com.example.studyabroad.ui.profile.BuildAcademicProfileActivity.class);
            startActivity(intent);
        });
        
        // Notifications switch
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            profileManager.setNotificationsEnabled(isChecked);
            Toast.makeText(requireContext(), 
                    isChecked ? "Notifications enabled" : "Notifications disabled", 
                    Toast.LENGTH_SHORT).show();
        });
        
        // Language settings
        cardViewLanguage.setOnClickListener(v -> showLanguageOptions());
        
        // Theme settings
        cardViewTheme.setOnClickListener(v -> showThemeOptions());
    }
    
    private void loadUserProfile() {
        // Get current logged-in user
        User currentUser = sessionManager.getUser();
        
        if (currentUser != null) {
            // Use actual user data from session
            String name = currentUser.getName() != null ? currentUser.getName() : "User Name";
            String email = currentUser.getEmail() != null ? currentUser.getEmail() : "user@example.com";
            String phone = currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "No phone number";
            
            // Update UI with actual user data
            textViewName.setText(name);
            textViewEmail.setText(email);
            textViewPhone.setText(phone);
        } else {
            // Fallback to default values if no user is logged in
            textViewName.setText("Guest User");
            textViewEmail.setText("guest@example.com");
            textViewPhone.setText("No phone number");
        }
        
        // Load other preferences from UserProfileManager
        String profilePicture = profileManager.getProfilePicture();
        boolean notificationsEnabled = profileManager.isNotificationsEnabled();
        String language = profileManager.getLanguage();
        String theme = profileManager.getTheme();
        
        switchNotifications.setChecked(notificationsEnabled);
        textViewLanguage.setText(language);
        textViewTheme.setText(theme);
        
        // Set profile picture if available
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                Uri imageUri = Uri.parse(profilePicture);
                imageViewAvatar.setImageURI(imageUri);
            } catch (Exception e) {
                // Use default avatar if loading fails
                imageViewAvatar.setImageResource(R.drawable.profile);
            }
        } else {
            // Use default avatar
            imageViewAvatar.setImageResource(R.drawable.profile);
        }
    }
    
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
    
    private void showLanguageOptions() {
        // Display language selection dialog
        String[] languages = {"English", "Chinese", "Spanish", "French", "German"};
        
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Select Language")
                .setItems(languages, (dialog, which) -> {
                    String selectedLanguage = languages[which];
                    textViewLanguage.setText(selectedLanguage);
                    profileManager.setLanguage(selectedLanguage);
                    Toast.makeText(requireContext(), "Language set to " + selectedLanguage, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void showThemeOptions() {
        // Display theme selection dialog
        String[] themes = {"Light", "Dark", "System Default"};
        
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Select Theme")
                .setItems(themes, (dialog, which) -> {
                    String selectedTheme = themes[which];
                    textViewTheme.setText(selectedTheme);
                    profileManager.setTheme(selectedTheme);
                    Toast.makeText(requireContext(), "Theme set to " + selectedTheme, Toast.LENGTH_SHORT).show();
                    // TODO: Apply theme change
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadUserProfile(); // Refresh profile data when fragment resumes
    }
} 