package com.example.studyabroad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studyabroad.models.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "chat.db";
    public static final int DATABASE_VERSION = 2; // Increment version for schema change
    
    public static final String TABLE_MESSAGES = "messages";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_IS_USER = "is_user";
    public static final String COLUMN_USER_ID = "user_id"; // New column for user ID

    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_MESSAGES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MESSAGE + " TEXT NOT NULL, " +
                COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                COLUMN_IS_USER + " INTEGER NOT NULL, " +
                COLUMN_USER_ID + " INTEGER NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add user_id column to existing table
            db.execSQL("ALTER TABLE " + TABLE_MESSAGES + " ADD COLUMN " + COLUMN_USER_ID + " INTEGER DEFAULT 0");
        }
    }

    public void addMessage(ChatMessage message, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE, message.getMessage());
        values.put(COLUMN_TIMESTAMP, message.getTimestamp());
        values.put(COLUMN_IS_USER, message.isUser() ? 1 : 0);
        values.put(COLUMN_USER_ID, userId);
        long id = db.insert(TABLE_MESSAGES, null, values);
        message.setId(id);
        db.close();
    }

    public void saveMessage(ChatMessage message, long userId) {
        addMessage(message, userId);
    }

    public void deleteMessage(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, null, null);
        db.close();
    }

    public void deleteAllMessagesForUser(long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public List<ChatMessage> getAllMessages(long userId) {
        List<ChatMessage> messages = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGES + 
                           " WHERE " + COLUMN_USER_ID + " = ?" +
                           " ORDER BY " + COLUMN_TIMESTAMP + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                ChatMessage message = cursorToChatMessage(cursor);
                messages.add(message);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return messages;
    }

    public List<ChatMessage> searchMessages(String query, long userId) {
        List<ChatMessage> messages = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGES + 
                           " WHERE " + COLUMN_MESSAGE + " LIKE ? AND " + COLUMN_USER_ID + " = ?" +
                           " ORDER BY " + COLUMN_TIMESTAMP + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%", String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                ChatMessage message = cursorToChatMessage(cursor);
                messages.add(message);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return messages;
    }

    private ChatMessage cursorToChatMessage(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int messageIndex = cursor.getColumnIndex(COLUMN_MESSAGE);
        int timestampIndex = cursor.getColumnIndex(COLUMN_TIMESTAMP);
        int isUserIndex = cursor.getColumnIndex(COLUMN_IS_USER);
        
        if (idIndex == -1 || messageIndex == -1 || timestampIndex == -1 || isUserIndex == -1) {
            throw new IllegalArgumentException("One or more columns not found in cursor");
        }
        
        long id = cursor.getLong(idIndex);
        String message = cursor.getString(messageIndex);
        long timestamp = cursor.getLong(timestampIndex);
        boolean isUser = cursor.getInt(isUserIndex) == 1;

        ChatMessage chatMessage = new ChatMessage(message, isUser);
        chatMessage.setId(id);
        chatMessage.setTimestamp(timestamp);
        return chatMessage;
    }
} 