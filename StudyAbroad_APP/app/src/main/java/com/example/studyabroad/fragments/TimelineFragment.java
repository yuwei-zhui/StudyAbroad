package com.example.studyabroad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;
import com.example.studyabroad.adapters.TimelineAdapter;
import com.example.studyabroad.dialogs.AddTimelineDialog;
import com.example.studyabroad.dialogs.EditTimelineDialog;
import com.example.studyabroad.models.TimelineItem;
import com.example.studyabroad.repositories.TimelineRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimelineFragment extends Fragment implements 
        AddTimelineDialog.OnTimelineItemAddedListener,
        EditTimelineDialog.OnTimelineItemEditedListener,
        TimelineAdapter.OnTimelineItemActionListener {

    private RecyclerView recyclerViewTimeline;
    private FloatingActionButton fabAdd;
    private android.widget.ImageView ivAddTimeline;
    private TimelineAdapter timelineAdapter;
    private List<TimelineItem> timelineItems;
    private TimelineRepository timelineRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.util.Log.d("TimelineFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        recyclerViewTimeline = view.findViewById(R.id.recyclerViewTimeline);
        fabAdd = view.findViewById(R.id.fabAdd);
        ivAddTimeline = view.findViewById(R.id.ivAddTimeline);
        
        timelineRepository = TimelineRepository.getInstance(requireContext());
        android.util.Log.d("TimelineFragment", "TimelineRepository initialized");

        setupRecyclerView();
        setupListeners();

        android.util.Log.d("TimelineFragment", "onCreateView completed");
        return view;
    }

    private void setupRecyclerView() {
        recyclerViewTimeline.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize empty list and adapter first
        timelineItems = new ArrayList<>();
        timelineAdapter = new TimelineAdapter(timelineItems, requireContext());
        timelineAdapter.setOnTimelineItemActionListener(this);
        recyclerViewTimeline.setAdapter(timelineAdapter);
        
        // Then load data
        loadTimelineItems();
    }

    private void loadTimelineItems() {
        android.util.Log.d("TimelineFragment", "Starting to load timeline data");
        List<TimelineItem> items = timelineRepository.getAllTimelineItems();
        android.util.Log.d("TimelineFragment", "Retrieved " + items.size() + " timeline items from database");
        
        // If database is empty, add sample data
        if (items.isEmpty()) {
            android.util.Log.d("TimelineFragment", "Database is empty, adding sample data");
            addSampleTimelineItems();
            items = timelineRepository.getAllTimelineItems();
            android.util.Log.d("TimelineFragment", "After adding sample data, retrieved " + items.size() + " timeline items");
        }
        
        // Update the adapter with new data
        timelineItems.clear();
        timelineItems.addAll(items);
        if (timelineAdapter != null) {
            timelineAdapter.notifyDataSetChanged();
            android.util.Log.d("TimelineFragment", "Updated adapter data");
        }
    }
    
    private void refreshTimelineItems() {
        // Reload all timeline items from database
        List<TimelineItem> updatedItems = timelineRepository.getAllTimelineItems();
        android.util.Log.d("TimelineFragment", "Refreshing timeline data, retrieved " + updatedItems.size() + " items from database");
        
        if (timelineAdapter != null && timelineItems != null) {
            // Update data in adapter
            timelineItems.clear();
            timelineItems.addAll(updatedItems);
            timelineAdapter.notifyDataSetChanged();
            android.util.Log.d("TimelineFragment", "Updated adapter, currently displaying " + timelineItems.size() + " items");
        } else {
            // If adapter not created yet, reload
            android.util.Log.d("TimelineFragment", "Adapter not created, reloading");
            loadTimelineItems();
        }
    }

    private void setupListeners() {
        fabAdd.setOnClickListener(v -> showAddTimelineDialog());
        ivAddTimeline.setOnClickListener(v -> showAddTimelineDialog());
    }
    
    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.d("TimelineFragment", "onResume called");
        // Refresh timeline data in case new items were added from other screens
        refreshTimelineItems();
    }

    private void showAddTimelineDialog() {
        AddTimelineDialog dialog = new AddTimelineDialog();
        dialog.setOnTimelineItemAddedListener(this);
        dialog.show(getChildFragmentManager(), "AddTimelineDialog");
    }

    @Override
    public void onTimelineItemAdded(TimelineItem item) {
        // Check uniqueness
        if (item.getUniversityName() != null && item.getProgramName() != null) {
            if (timelineRepository.isUniversityProgramExists(item.getUniversityName(), item.getProgramName())) {
                Toast.makeText(getContext(), "Application for this university and program already exists", Toast.LENGTH_LONG).show();
                return;
            }
        }
        
        long id = timelineRepository.saveTimelineItem(item);
        if (id > 0) {
            timelineItems.add(item);
            timelineAdapter.notifyItemInserted(timelineItems.size() - 1);
            recyclerViewTimeline.scrollToPosition(timelineItems.size() - 1);
            Toast.makeText(getContext(), "Timeline item added successfully", Toast.LENGTH_SHORT).show();
        } else if (id == -1) {
            Toast.makeText(getContext(), "Application for this university and program already exists", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Failed to add timeline item", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditItem(TimelineItem item) {
        EditTimelineDialog dialog = EditTimelineDialog.newInstance(item);
        dialog.setOnTimelineItemEditedListener(this);
        dialog.show(getChildFragmentManager(), "EditTimelineDialog");
    }

    @Override
    public void onDeleteItem(TimelineItem item) {
        new AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this timeline item?")
            .setPositiveButton("Delete", (dialog, which) -> {
                // Delete from database
                boolean success = timelineRepository.deleteTimelineItem(item.getId());
                if (success) {
                    // Remove from list and update adapter
                    int position = timelineItems.indexOf(item);
                    if (position != -1) {
                        timelineItems.remove(position);
                        timelineAdapter.notifyItemRemoved(position);
                        Toast.makeText(getContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    public void onTimelineItemEdited(TimelineItem item) {
        boolean success = timelineRepository.updateTimelineItem(item);
        if (success) {
            // Find and update the item in the list
            for (int i = 0; i < timelineItems.size(); i++) {
                if (timelineItems.get(i).getId() == item.getId()) {
                    timelineItems.set(i, item);
                    timelineAdapter.notifyItemChanged(i);
                    Toast.makeText(getContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        } else {
            Toast.makeText(getContext(), "Failed to update item", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSampleTimelineItems() {
        Calendar calendar = Calendar.getInstance();

        // Add Melbourne University application deadline (31 August for start-year intake)
        calendar.set(2025, Calendar.AUGUST, 31);
        TimelineItem item1 = new TimelineItem(
            "Application Deadline",
            "Submit your application for Master of Computer Science (2026 intake)",
            calendar.getTime(),
            TimelineItem.STATUS_PENDING,
            "university1",
            "The University of Melbourne",
            "Master of Computer Science"
        );
        timelineRepository.saveTimelineItem(item1);

        // Add Imperial College London application deadline (30 June for visa applicants)
        calendar.set(2025, Calendar.JUNE, 30);
        TimelineItem item2 = new TimelineItem(
            "Application Deadline",
            "Submit your application for MSc Computing (international students)",
            calendar.getTime(),
            TimelineItem.STATUS_PENDING,
            "university2",
            "Imperial College London",
            "MSc Computing"
        );
        timelineRepository.saveTimelineItem(item2);

        // Add University of Hong Kong application deadline (varies by program, using typical May deadline)
        calendar.set(2025, Calendar.MAY, 31);
        TimelineItem item3 = new TimelineItem(
            "Application Deadline",
            "Submit your application for Master of Science in Computer Science",
            calendar.getTime(),
            TimelineItem.STATUS_PENDING,
            "university3",
            "The University of Hong Kong",
            "Master of Science in Computer Science"
        );
        timelineRepository.saveTimelineItem(item3);

        // Add University of Sydney application deadline (1 December for Semester 1, 2026)
        calendar.set(2025, Calendar.DECEMBER, 1);
        TimelineItem item4 = new TimelineItem(
            "Application Deadline",
            "Submit your application for Master of Information Technology (Semester 1, 2026)",
            calendar.getTime(),
            TimelineItem.STATUS_PENDING,
            "university4",
            "The University of Sydney",
            "Master of Information Technology"
        );
        timelineRepository.saveTimelineItem(item4);
    }

    /**
     * Add timeline item from external source (e.g., from search screen)
     */
    public void addTimelineItemFromExternal(String universityName, String programName, java.util.Date deadline) {
        // Check uniqueness
        if (timelineRepository.isUniversityProgramExists(universityName, programName)) {
            Toast.makeText(getContext(), "Application for this university and program already exists", Toast.LENGTH_LONG).show();
            return;
        }
        
        TimelineItem item = new TimelineItem(
            "Application Deadline",
            "Submit your application for " + programName,
            deadline,
            TimelineItem.STATUS_PENDING,
            "external",
            universityName,
            programName
        );
        
        long id = timelineRepository.saveTimelineItem(item);
        if (id > 0) {
            refreshTimelineItems();
            Toast.makeText(getContext(), "Added to application timeline", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to add", Toast.LENGTH_SHORT).show();
        }
    }
} 