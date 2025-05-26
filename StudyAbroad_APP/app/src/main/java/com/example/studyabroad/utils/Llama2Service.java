package com.example.studyabroad.utils;

import android.util.Log;

import com.example.studyabroad.database.entity.University;
import com.example.studyabroad.database.entity.User;
import com.example.studyabroad.database.entity.UserPreference;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Llama2Service {
    
    private static final String TAG = "Llama2Service";
    private static final String API_ENDPOINT = "https://api.llama2.ai/recommend";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    
    private static Llama2Service instance;
    private OkHttpClient client;
    
    public static synchronized Llama2Service getInstance() {
        if (instance == null) {
            instance = new Llama2Service();
        }
        return instance;
    }
    
    private Llama2Service() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }
    
    public interface Llama2Callback {
        void onSuccess(String response);
        void onError(String errorMessage);
    }
    
    // Method to get university recommendations
    public void getUniversityRecommendations(User user, UserPreference preferences, Llama2Callback callback) {
        try {
            JSONObject requestBody = new JSONObject();
            
            // User details
            JSONObject userObj = new JSONObject();
            userObj.put("name", user.getName());
            userObj.put("gpa", user.getGpa());
            userObj.put("currentInstitution", user.getCurrentInstitution());
            userObj.put("major", user.getMajor());
            userObj.put("targetDegreeLevel", user.getTargetDegreeLevel());
            
            // User preferences
            JSONObject preferencesObj = new JSONObject();
            preferencesObj.put("preferredCountries", preferences.getPreferredCountries());
            preferencesObj.put("qsRankingRange", preferences.getQsRankingRange());
            preferencesObj.put("budgetRange", preferences.getBudgetRange());
            preferencesObj.put("preferredMajor", preferences.getPreferredMajor());
            
            // Combine all
            requestBody.put("user", userObj);
            requestBody.put("preferences", preferencesObj);
            requestBody.put("requestType", "universityRecommendations");
            
            // Make network request in a background thread
            new Thread(() -> {
                try {
                    String response = makeRequest(requestBody.toString());
                    if (response != null) {
                        callback.onSuccess(response);
                    } else {
                        callback.onError("Failed to get recommendations");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error getting recommendations", e);
                    callback.onError("Error: " + e.getMessage());
                }
            }).start();
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating request body", e);
            callback.onError("Error: " + e.getMessage());
        }
    }
    
    // Method to analyze documents (like essays or resumes)
    public void analyzeDocument(String documentText, Llama2Callback callback) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("documentText", documentText);
            requestBody.put("requestType", "documentAnalysis");
            
            // Make network request in a background thread
            new Thread(() -> {
                try {
                    String response = makeRequest(requestBody.toString());
                    if (response != null) {
                        callback.onSuccess(response);
                    } else {
                        callback.onError("Failed to analyze document");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error analyzing document", e);
                    callback.onError("Error: " + e.getMessage());
                }
            }).start();
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating request body", e);
            callback.onError("Error: " + e.getMessage());
        }
    }
    
    // Method for AI chat conversations
    public void getChatResponse(String userQuery, Llama2Callback callback) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("userQuery", userQuery);
            requestBody.put("requestType", "chatResponse");
            
            // Make network request in a background thread
            new Thread(() -> {
                try {
                    String response = makeRequest(requestBody.toString());
                    if (response != null) {
                        callback.onSuccess(response);
                    } else {
                        callback.onError("Failed to get chat response");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error getting chat response", e);
                    callback.onError("Error: " + e.getMessage());
                }
            }).start();
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating request body", e);
            callback.onError("Error: " + e.getMessage());
        }
    }
    
    // Helper method to make HTTP requests
    private String makeRequest(String jsonBody) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .post(body)
                .build();
                
        Response response = client.newCall(request).execute();
        try (ResponseBody responseBody = response.body()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            
            return responseBody != null ? responseBody.string() : null;
        }
    }
    
    // For testing/demo purposes - simulates AI responses when API isn't available
    public String getSimulatedResponse(String requestType) {
        switch (requestType) {
            case "universityRecommendations":
                return "{\n" +
                        "  \"recommendations\": [\n" +
                        "    {\n" +
                        "      \"name\": \"University of Melbourne\",\n" +
                        "      \"country\": \"Australia\",\n" +
                        "      \"qsRanking\": 13,\n" +
                        "      \"matchScore\": 92,\n" +
                        "      \"courses\": [\"Master of Computer Science\", \"Master of Information Technology\"],\n" +
                        "      \"reasonForMatch\": \"Strong academic fit based on your GPA and matches your interest in Data Science.\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"Imperial College London\",\n" +
                        "      \"country\": \"United Kingdom\",\n" +
                        "      \"qsRanking\": 8,\n" +
                        "      \"matchScore\": 88,\n" +
                        "      \"courses\": [\"MSc Computing\", \"MSc Advanced Computing\"],\n" +
                        "      \"reasonForMatch\": \"Excellent research opportunities in your field and matches your country preference.\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";
            case "documentAnalysis":
                return "{\n" +
                        "  \"analysis\": {\n" +
                        "    \"summary\": \"The document demonstrates strong analytical skills and interest in data science applications.\",\n" +
                        "    \"keyThemes\": [\"Data Analysis\", \"Machine Learning\", \"Research\", \"Problem Solving\"],\n" +
                        "    \"suggestedMajors\": [\"Data Science\", \"Computer Science\", \"Applied Mathematics\"],\n" +
                        "    \"strengths\": [\"Technical knowledge\", \"Project experience\", \"Clear communication\"],\n" +
                        "    \"improvementAreas\": [\"Consider adding more international perspective\", \"Emphasize teamwork examples\"]\n" +
                        "  }\n" +
                        "}";
            case "chatResponse":
                return "{\n" +
                        "  \"response\": \"Based on your interest in data science and preference for Australia, I recommend looking into the University of Melbourne's Master of Data Science program. It has a strong reputation in this field and offers excellent research opportunities. The application deadline for the February intake is August 31st, and you would need to prepare your academic transcripts, CV, and a statement of purpose.\"\n" +
                        "}";
            default:
                return "{\n" +
                        "  \"error\": \"Unknown request type\"\n" +
                        "}";
        }
    }
} 