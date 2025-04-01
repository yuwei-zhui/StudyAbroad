package com.example.studyabroad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studyabroad.R;

public class AcademicProfileFragment extends Fragment {

    private EditText etInstitution, etGPA, etRankingFrom, etRankingTo;
    private Spinner spinnerMajor, spinnerDegree, spinnerCountries;
    private Button btnSave;
    private ImageView btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_academic_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupSpinners();
        setupListeners();
    }

    private void initViews(View view) {
        etInstitution = view.findViewById(R.id.etInstitution);
        etGPA = view.findViewById(R.id.etGPA);
        etRankingFrom = view.findViewById(R.id.etRankingFrom);
        etRankingTo = view.findViewById(R.id.etRankingTo);
        spinnerMajor = view.findViewById(R.id.spinnerMajor);
        spinnerDegree = view.findViewById(R.id.spinnerDegree);
        spinnerCountries = view.findViewById(R.id.spinnerCountries);
        btnSave = view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);
    }

    private void setupSpinners() {
        // Setup Major Spinner
        String[] majors = {"Computer Science", "Engineering", "Business", "Arts", "Medicine", "Law"};
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                majors
        );
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(majorAdapter);

        // Setup Degree Spinner
        String[] degrees = {"Bachelor", "Master", "PhD"};
        ArrayAdapter<String> degreeAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                degrees
        );
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDegree.setAdapter(degreeAdapter);

        // Setup Countries Spinner
        String[] countries = {"United States", "United Kingdom", "Australia", "Canada", "Germany", "Japan"};
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                countries
        );
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        // Get values from inputs
        String institution = etInstitution.getText().toString();
        String gpa = etGPA.getText().toString();
        String major = spinnerMajor.getSelectedItem().toString();
        String degree = spinnerDegree.getSelectedItem().toString();
        String country = spinnerCountries.getSelectedItem().toString();
        String rankingFrom = etRankingFrom.getText().toString();
        String rankingTo = etRankingTo.getText().toString();

        // TODO: Save to database or shared preferences

        Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        if (etInstitution.getText().toString().trim().isEmpty()) {
            etInstitution.setError("Please enter your institution");
            return false;
        }

        if (etGPA.getText().toString().trim().isEmpty()) {
            etGPA.setError("Please enter your GPA");
            return false;
        }

        try {
            float gpa = Float.parseFloat(etGPA.getText().toString());
            if (gpa < 0 || gpa > 4.0) {
                etGPA.setError("GPA must be between 0 and 4.0");
                return false;
            }
        } catch (NumberFormatException e) {
            etGPA.setError("Please enter a valid GPA");
            return false;
        }

        if (!etRankingFrom.getText().toString().trim().isEmpty() 
            && !etRankingTo.getText().toString().trim().isEmpty()) {
            try {
                int from = Integer.parseInt(etRankingFrom.getText().toString());
                int to = Integer.parseInt(etRankingTo.getText().toString());
                if (from > to) {
                    etRankingFrom.setError("From ranking should be less than To ranking");
                    return false;
                }
            } catch (NumberFormatException e) {
                etRankingFrom.setError("Please enter valid ranking numbers");
                return false;
            }
        }

        return true;
    }
} 