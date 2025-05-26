package com.example.studyabroad.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.studyabroad.models.AIRequest;
import com.example.studyabroad.models.AIResponse;
import com.example.studyabroad.models.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DebugHelper {
    
    private static final String TAG = "DebugHelper";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    
    public static void logChatMessage(ChatMessage message, String action) {
        String timestamp = dateFormat.format(new Date(message.getTimestamp()));
        String type = message.isUser() ? "USER" : "AI";
        
        Log.d(TAG, String.format("[%s] %s Message %s: ID=%d, Content='%s', Time=%s", 
            action, type, message.isUser() ? "sent" : "received", 
            message.getId(), 
            truncateString(message.getMessage(), 50),
            timestamp));
    }
    
    public static void logAIRequest(AIRequest request) {
        Log.d(TAG, "=== AI REQUEST ===");
        Log.d(TAG, "Message: " + truncateString(request.getMessage(), 100));
        Log.d(TAG, "User ID: " + request.getUserId());
        Log.d(TAG, "Academic Profile: " + truncateString(request.getAcademicProfile(), 100));
        Log.d(TAG, "================");
    }
    
    public static void logAIResponse(AIResponse response) {
        Log.d(TAG, "=== AI RESPONSE ===");
        Log.d(TAG, "Status: " + response.getStatus());
        Log.d(TAG, "Response: " + truncateString(response.getResponse(), 100));
        Log.d(TAG, "Error: " + response.getError());
        Log.d(TAG, "==================");
    }
    
    public static void logNetworkError(String endpoint, int statusCode, String errorMessage) {
        Log.e(TAG, String.format("Network Error - Endpoint: %s, Status: %d, Message: %s", 
            endpoint, statusCode, errorMessage));
    }
    
    public static void logDatabaseOperation(String operation, boolean success, String details) {
        String level = success ? "INFO" : "ERROR";
        Log.d(TAG, String.format("[DB %s] %s - %s", level, operation, details));
    }
    
    public static void logMessageList(List<ChatMessage> messages, String context) {
        Log.d(TAG, String.format("Message List [%s]: %d messages", context, messages.size()));
        for (int i = 0; i < Math.min(messages.size(), 5); i++) {
            ChatMessage msg = messages.get(i);
            Log.d(TAG, String.format("  [%d] %s: %s", i, 
                msg.isUser() ? "USER" : "AI", 
                truncateString(msg.getMessage(), 30)));
        }
        if (messages.size() > 5) {
            Log.d(TAG, String.format("  ... and %d more messages", messages.size() - 5));
        }
    }
    
    public static void showDebugToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, "DEBUG: " + message, Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "DEBUG TOAST: " + message);
    }
    
    public static void logUserProfile(String userProfile) {
        Log.d(TAG, "=== USER PROFILE ===");
        if (userProfile != null && !userProfile.trim().isEmpty()) {
            String[] lines = userProfile.split("\n");
            for (String line : lines) {
                Log.d(TAG, "Profile: " + line.trim());
            }
        } else {
            Log.d(TAG, "Profile: Empty or null");
        }
        Log.d(TAG, "===================");
    }
    
    public static void logApiConfiguration(String baseUrl, String endpoint) {
        Log.d(TAG, "=== API CONFIG ===");
        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Endpoint: " + endpoint);
        Log.d(TAG, "Full URL: " + baseUrl + endpoint);
        Log.d(TAG, "==================");
    }
    
    public static void logThreadInfo(String context) {
        Thread currentThread = Thread.currentThread();
        boolean isMainThread = currentThread.getName().equals("main");
        Log.d(TAG, String.format("[%s] Thread: %s (Main: %s)", 
            context, currentThread.getName(), isMainThread));
    }
    
    private static String truncateString(String str, int maxLength) {
        if (str == null) return "null";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength) + "...";
    }
    
    public static void logException(String context, Exception e) {
        Log.e(TAG, String.format("Exception in %s: %s", context, e.getMessage()), e);
    }
    
    public static void logTimestamp(String event) {
        String timestamp = dateFormat.format(new Date());
        Log.d(TAG, String.format("[%s] %s", timestamp, event));
    }
} 