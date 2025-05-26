package com.example.studyabroad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studyabroad.models.TimelineItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimelineDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timeline_db";
    private static final int DATABASE_VERSION = 2;

    // Timeline table
    private static final String TABLE_TIMELINE = "timeline";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_UNIVERSITY_ID = "university_id";
    private static final String COLUMN_UNIVERSITY_NAME = "university_name";
    private static final String COLUMN_PROGRAM_NAME = "program_name";

    private static TimelineDatabaseHelper instance;
    private final SimpleDateFormat dateFormat;

    private TimelineDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public static synchronized TimelineDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TimelineDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TIMELINE_TABLE = "CREATE TABLE " + TABLE_TIMELINE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_STATUS + " INTEGER,"
                + COLUMN_UNIVERSITY_ID + " TEXT,"
                + COLUMN_UNIVERSITY_NAME + " TEXT,"
                + COLUMN_PROGRAM_NAME + " TEXT,"
                + "UNIQUE(" + COLUMN_UNIVERSITY_NAME + ", " + COLUMN_PROGRAM_NAME + ")"
                + ")";
        db.execSQL(CREATE_TIMELINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_TIMELINE + " ADD COLUMN " + COLUMN_UNIVERSITY_NAME + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_TIMELINE + " ADD COLUMN " + COLUMN_PROGRAM_NAME + " TEXT");
        }
    }

    /**
     * Check if timeline item with same university and program already exists
     */
    public boolean isUniversityProgramExists(String universityName, String programName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_TIMELINE + 
                      " WHERE " + COLUMN_UNIVERSITY_NAME + "=? AND " + COLUMN_PROGRAM_NAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{universityName, programName});
        
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;
        }
        
        cursor.close();
        db.close();
        return exists;
    }

    public long saveTimelineItem(TimelineItem item) {
        android.util.Log.d("TimelineDatabaseHelper", "Saving timeline item: " + item.getTitle());
        
        if (item.getUniversityName() != null && item.getProgramName() != null) {
            if (isUniversityProgramExists(item.getUniversityName(), item.getProgramName())) {
                android.util.Log.w("TimelineDatabaseHelper", "Timeline item for this university and program already exists");
                return -1;
            }
        }
        
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_DESCRIPTION, item.getDescription());
        values.put(COLUMN_DATE, dateFormat.format(item.getDate()));
        values.put(COLUMN_STATUS, item.getStatus());
        values.put(COLUMN_UNIVERSITY_ID, item.getUniversityId());
        values.put(COLUMN_UNIVERSITY_NAME, item.getUniversityName());
        values.put(COLUMN_PROGRAM_NAME, item.getProgramName());

        long id = db.insert(TABLE_TIMELINE, null, values);
        android.util.Log.d("TimelineDatabaseHelper", "Save result ID: " + id);
        db.close();

        return id;
    }

    public List<TimelineItem> getAllTimelineItems() {
        List<TimelineItem> timelineItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TIMELINE + " ORDER BY " + COLUMN_DATE + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        android.util.Log.d("TimelineDatabaseHelper", "Querying timeline data, cursor count: " + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                TimelineItem item = cursorToTimelineItem(cursor);
                timelineItems.add(item);
                android.util.Log.d("TimelineDatabaseHelper", "Loading timeline item: " + item.getTitle());
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        android.util.Log.d("TimelineDatabaseHelper", "Total loaded " + timelineItems.size() + " timeline items");
        return timelineItems;
    }

    public TimelineItem getTimelineItem(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TIMELINE, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        TimelineItem item = null;
        if (cursor.moveToFirst()) {
            item = cursorToTimelineItem(cursor);
        }

        cursor.close();
        db.close();
        return item;
    }

    public int updateTimelineItem(TimelineItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_DESCRIPTION, item.getDescription());
        values.put(COLUMN_DATE, dateFormat.format(item.getDate()));
        values.put(COLUMN_STATUS, item.getStatus());
        values.put(COLUMN_UNIVERSITY_ID, item.getUniversityId());
        values.put(COLUMN_UNIVERSITY_NAME, item.getUniversityName());
        values.put(COLUMN_PROGRAM_NAME, item.getProgramName());

        int rowsAffected = db.update(TABLE_TIMELINE, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(item.getId())});
        db.close();

        return rowsAffected;
    }

    public int deleteTimelineItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_TIMELINE, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();

        return rowsAffected;
    }

    private TimelineItem cursorToTimelineItem(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
        int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
        int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
        int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
        int universityIdIndex = cursor.getColumnIndex(COLUMN_UNIVERSITY_ID);
        int universityNameIndex = cursor.getColumnIndex(COLUMN_UNIVERSITY_NAME);
        int programNameIndex = cursor.getColumnIndex(COLUMN_PROGRAM_NAME);

        long id = cursor.getLong(idIndex);
        String title = cursor.getString(titleIndex);
        String description = cursor.getString(descriptionIndex);
        String dateStr = cursor.getString(dateIndex);
        int status = cursor.getInt(statusIndex);
        String universityId = cursor.getString(universityIdIndex);
        String universityName = universityNameIndex >= 0 ? cursor.getString(universityNameIndex) : null;
        String programName = programNameIndex >= 0 ? cursor.getString(programNameIndex) : null;

        Date date;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            date = new Date();
        }

        TimelineItem timelineItem = new TimelineItem(title, description, date, status, universityId, universityName, programName);
        timelineItem.setId(id);

        return timelineItem;
    }
} 