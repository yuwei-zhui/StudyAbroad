package com.example.studyabroad.repositories;

import android.content.Context;
import java.util.List;
import com.example.studyabroad.database.ChatDatabaseHelper;
import com.example.studyabroad.models.ChatMessage;

public class ChatRepository {
    private static ChatRepository instance;
    private final ChatDatabaseHelper dbHelper;
    
    public ChatRepository(Context context) {
        dbHelper = new ChatDatabaseHelper(context);
    }
    
    public static ChatRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ChatRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    public void saveMessage(ChatMessage message, long userId) {
        dbHelper.saveMessage(message, userId);
    }
    
    public List<ChatMessage> getAllMessages(long userId) {
        return dbHelper.getAllMessages(userId);
    }
    
    public void deleteMessage(long id) {
        dbHelper.deleteMessage(id);
    }
    
    public void deleteAllMessages() {
        dbHelper.deleteAllMessages();
    }
    
    public void deleteAllMessagesForUser(long userId) {
        dbHelper.deleteAllMessagesForUser(userId);
    }
    
    public List<ChatMessage> searchMessages(String query, long userId) {
        return dbHelper.searchMessages(query, userId);
    }
} 