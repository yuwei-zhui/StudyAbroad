package com.example.studyabroad.repositories;

import android.content.Context;

import com.example.studyabroad.database.TimelineDatabaseHelper;
import com.example.studyabroad.models.TimelineItem;

import java.util.List;

public class TimelineRepository {

    private static TimelineRepository instance;
    private final TimelineDatabaseHelper dbHelper;

    private TimelineRepository(Context context) {
        dbHelper = TimelineDatabaseHelper.getInstance(context);
    }

    public static synchronized TimelineRepository getInstance(Context context) {
        if (instance == null) {
            instance = new TimelineRepository(context.getApplicationContext());
        }
        return instance;
    }

    public List<TimelineItem> getAllTimelineItems() {
        return dbHelper.getAllTimelineItems();
    }

    public TimelineItem getTimelineItem(long id) {
        return dbHelper.getTimelineItem(id);
    }

    /**
     * Check if timeline item with same university and program already exists
     */
    public boolean isUniversityProgramExists(String universityName, String programName) {
        return dbHelper.isUniversityProgramExists(universityName, programName);
    }

    public long saveTimelineItem(TimelineItem item) {
        long id = dbHelper.saveTimelineItem(item);
        if (id > 0) {
            item.setId(id);
        }
        return id;
    }

    public boolean updateTimelineItem(TimelineItem item) {
        return dbHelper.updateTimelineItem(item) > 0;
    }

    public boolean deleteTimelineItem(long id) {
        return dbHelper.deleteTimelineItem(id) > 0;
    }
} 