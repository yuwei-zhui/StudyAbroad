package com.example.studyabroad.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studyabroad.R;
import com.example.studyabroad.utils.UserProfileManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PersonalInfoEditFragment extends Fragment {
    
    private EditText etName, etEmail, etPhone, etBirthDate, etNationality, etIntroduction;
    private ImageView btnBack;
    private Button btnSave;
    private UserProfileManager profileManager;
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_info_edit, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupListeners();
        loadUserData();
    }
    
    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etPhone);
        etBirthDate = view.findViewById(R.id.etBirthDate);
        etNationality = view.findViewById(R.id.etNationality);
        etIntroduction = view.findViewById(R.id.etIntroduction);
        btnBack = view.findViewById(R.id.btnBack);
        btnSave = view.findViewById(R.id.btnSave);
        
        profileManager = UserProfileManager.getInstance(requireContext());
    }
    
    private void setupListeners() {
        // Back button
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        
        // Date picker
        etBirthDate.setOnClickListener(v -> showDatePicker());
        
        // Save button
        btnSave.setOnClickListener(v -> saveUserData());
    }
    
    private void loadUserData() {
        // Load user data
        etName.setText(profileManager.getName());
        etEmail.setText(profileManager.getEmail());
        etPhone.setText(profileManager.getPhone());
        etBirthDate.setText(profileManager.getBirthDate());
        etNationality.setText(profileManager.getNationality());
        etIntroduction.setText(profileManager.getIntroduction());
    }
    
    private void saveUserData() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }
        
        // Save user data
        profileManager.setName(etName.getText().toString());
        profileManager.setEmail(etEmail.getText().toString());
        profileManager.setPhone(etPhone.getText().toString());
        profileManager.setBirthDate(etBirthDate.getText().toString());
        profileManager.setNationality(etNationality.getText().toString());
        profileManager.setIntroduction(etIntroduction.getText().toString());
        
        // Update AI chat data after saving
        updateAIChatBackgroundData();
        
        Toast.makeText(requireContext(), "Information saved successfully", Toast.LENGTH_SHORT).show();
        
        // Return to previous page
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
    
    private boolean validateInputs() {
        // Validate name
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Name cannot be empty");
            return false;
        }
        
        // Validate email
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("Email cannot be empty");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            return false;
        }
        
        // Validate phone number
        String phone = etPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            etPhone.setError("Phone number cannot be empty");
            return false;
        }
        
        return true;
    }
    
    private void showDatePicker() {
        // Parse existing date
        String currentDate = etBirthDate.getText().toString();
        if (!currentDate.isEmpty()) {
            try {
                calendar.setTime(dateFormat.parse(currentDate));
            } catch (Exception e) {
                calendar.setTimeInMillis(System.currentTimeMillis());
            }
        }
        
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    
                    etBirthDate.setText(dateFormat.format(calendar.getTime()));
                },
                year, month, day);
        
        datePickerDialog.show();
    }
    
    private void updateAIChatBackgroundData() {
    }
} 