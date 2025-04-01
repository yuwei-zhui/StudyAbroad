package com.example.studyabroad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.adapters.SchoolAdapter;
import com.example.studyabroad.adapters.SpotlightAdapter;
import com.example.studyabroad.models.School;
import com.example.studyabroad.models.Spotlight;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewSchools;
    private RecyclerView recyclerViewSpotlights;
    private SchoolAdapter schoolAdapter;
    private SpotlightAdapter spotlightAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // 初始化RecyclerViews
        recyclerViewSchools = view.findViewById(R.id.recyclerViewSchools);
        recyclerViewSpotlights = view.findViewById(R.id.recyclerViewSpotlights);
        
        // 设置布局管理器
        recyclerViewSchools.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSpotlights.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // 初始化适配器
        schoolAdapter = new SchoolAdapter(getDummySchools());
        spotlightAdapter = new SpotlightAdapter(getDummySpotlights());
        
        // 设置适配器
        recyclerViewSchools.setAdapter(schoolAdapter);
        recyclerViewSpotlights.setAdapter(spotlightAdapter);
        
        return view;
    }
    
    private List<School> getDummySchools() {
        List<School> schools = new ArrayList<>();
        
        // 添加示例学校数据
        schools.add(new School(
                "The University of Melbourne",
                "Melbourne, Australia",
                13,
                R.color.orange_primary,
                R.drawable.ic_school
        ));
        
        schools.add(new School(
                "Imperial College London",
                "London, United Kingdom",
                8,
                R.color.blue_primary,
                R.drawable.ic_school
        ));
        
        schools.add(new School(
                "Stanford University",
                "California, USA",
                3,
                R.color.colorPrimary,
                R.drawable.ic_school
        ));
        
        return schools;
    }
    
    private List<Spotlight> getDummySpotlights() {
        List<Spotlight> spotlights = new ArrayList<>();
        
        // 添加示例Spotlight数据
        spotlights.add(new Spotlight(
                "Oxford Summer Camp",
                R.color.light_green,
                R.drawable.summer_camp_icon
        ));
        
        spotlights.add(new Spotlight(
                "New grants for STEM majors!",
                R.color.light_orange,
                R.drawable.orange_planet
        ));
        
        spotlights.add(new Spotlight(
                "Upcoming application deadlines",
                R.color.blue_primary,
                R.drawable.ic_school
        ));
        
        return spotlights;
    }
} 