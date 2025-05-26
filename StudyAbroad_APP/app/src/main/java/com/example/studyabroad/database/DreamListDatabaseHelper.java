package com.example.studyabroad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studyabroad.models.DreamSchool;

import java.util.ArrayList;
import java.util.List;

public class DreamListDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dream_list_db";
    private static final int DATABASE_VERSION = 1;

    // Dream list table
    private static final String TABLE_DREAM_LIST = "dream_list";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_UNIVERSITY_NAME = "university_name";
    private static final String COLUMN_PROGRAM_NAME = "program_name";
    private static final String COLUMN_UNIVERSITY_ID = "university_id";
    private static final String COLUMN_PROGRAM_ID = "program_id";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_RANKING = "ranking";
    private static final String COLUMN_TUITION_FEE = "tuition_fee";
    private static final String COLUMN_APPLICATION_DEADLINE = "application_deadline";
    private static final String COLUMN_ADDED_TIMESTAMP = "added_timestamp";

    private static DreamListDatabaseHelper instance;

    private DreamListDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DreamListDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DreamListDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DREAM_LIST_TABLE = "CREATE TABLE " + TABLE_DREAM_LIST + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_UNIVERSITY_NAME + " TEXT NOT NULL,"
                + COLUMN_PROGRAM_NAME + " TEXT NOT NULL,"
                + COLUMN_UNIVERSITY_ID + " TEXT,"
                + COLUMN_PROGRAM_ID + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_RANKING + " TEXT,"
                + COLUMN_TUITION_FEE + " TEXT,"
                + COLUMN_APPLICATION_DEADLINE + " TEXT,"
                + COLUMN_ADDED_TIMESTAMP + " INTEGER,"
                + "UNIQUE(" + COLUMN_UNIVERSITY_NAME + ", " + COLUMN_PROGRAM_NAME + ")"
                + ")";
        db.execSQL(CREATE_DREAM_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DREAM_LIST);
        onCreate(db);
    }

    /**
     * Add dream school to list
     */
    public long addDreamSchool(DreamSchool dreamSchool) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_UNIVERSITY_NAME, dreamSchool.getUniversityName());
        values.put(COLUMN_PROGRAM_NAME, dreamSchool.getProgramName());
        values.put(COLUMN_UNIVERSITY_ID, dreamSchool.getUniversityId());
        values.put(COLUMN_PROGRAM_ID, dreamSchool.getProgramId());
        values.put(COLUMN_LOCATION, dreamSchool.getLocation());
        values.put(COLUMN_RANKING, dreamSchool.getRanking());
        values.put(COLUMN_TUITION_FEE, dreamSchool.getTuitionFee());
        values.put(COLUMN_APPLICATION_DEADLINE, dreamSchool.getApplicationDeadline());
        values.put(COLUMN_ADDED_TIMESTAMP, dreamSchool.getAddedTimestamp());

        long id = -1;
        try {
            id = db.insertOrThrow(TABLE_DREAM_LIST, null, values);
        } catch (Exception e) {
            // Handle duplicate entry
            android.util.Log.w("DreamListDB", "Duplicate entry: " + dreamSchool.getUniqueKey());
        } finally {
            db.close();
        }
        
        return id;
    }

    /**
     * Remove dream school from list
     */
    public boolean removeDreamSchool(String universityName, String programName) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        int rowsDeleted = db.delete(TABLE_DREAM_LIST,
                COLUMN_UNIVERSITY_NAME + "=? AND " + COLUMN_PROGRAM_NAME + "=?",
                new String[]{universityName, programName});
        
        db.close();
        return rowsDeleted > 0;
    }

    /**
     * Remove dream school by ID
     */
    public boolean removeDreamSchool(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        int rowsDeleted = db.delete(TABLE_DREAM_LIST,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        
        db.close();
        return rowsDeleted > 0;
    }

    /**
     * Check if university and program combination exists in dream list
     */
    public boolean isDreamSchool(String universityName, String programName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_DREAM_LIST + 
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

    /**
     * Get all dream schools
     */
    public List<DreamSchool> getAllDreamSchools() {
        List<DreamSchool> dreamSchools = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT * FROM " + TABLE_DREAM_LIST + " ORDER BY " + COLUMN_ADDED_TIMESTAMP + " DESC";
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                DreamSchool dreamSchool = cursorToDreamSchool(cursor);
                dreamSchools.add(dreamSchool);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return dreamSchools;
    }

    /**
     * Get dream schools count
     */
    public int getDreamSchoolsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_DREAM_LIST;
        Cursor cursor = db.rawQuery(query, null);
        
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        
        cursor.close();
        db.close();
        return count;
    }

    /**
     * Clear all dream schools
     */
    public void clearAllDreamSchools() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DREAM_LIST, null, null);
        db.close();
    }

    private DreamSchool cursorToDreamSchool(Cursor cursor) {
        DreamSchool dreamSchool = new DreamSchool();
        
        dreamSchool.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        dreamSchool.setUniversityName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIVERSITY_NAME)));
        dreamSchool.setProgramName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROGRAM_NAME)));
        dreamSchool.setUniversityId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIVERSITY_ID)));
        dreamSchool.setProgramId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROGRAM_ID)));
        dreamSchool.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
        dreamSchool.setRanking(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RANKING)));
        dreamSchool.setTuitionFee(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUITION_FEE)));
        dreamSchool.setApplicationDeadline(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APPLICATION_DEADLINE)));
        dreamSchool.setAddedTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ADDED_TIMESTAMP)));
        
        return dreamSchool;
    }
} 