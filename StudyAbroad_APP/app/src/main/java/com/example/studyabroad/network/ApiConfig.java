package com.example.studyabroad.network;

public class ApiConfig {
    // For Android emulator, use 10.0.2.2 to access host machine's localhost
    // For physical device, replace with your computer's IP address (e.g., "http://192.168.1.100:5000/")
    public static final String BASE_URL_EMULATOR = "http://10.0.2.2:5000/";
    public static final String BASE_URL_DEVICE = "http://192.168.1.100:5000/"; // Replace with your actual IP
    
    // Default to emulator URL - change this if testing on physical device
    public static final String BASE_URL = BASE_URL_EMULATOR;
    
    // API endpoints
    public static final String CHAT_ENDPOINT = "chat";
    
    /**
     * Get the appropriate base URL based on the environment
     * @param useDeviceUrl true if using physical device, false for emulator
     * @return the base URL
     */
    public static String getBaseUrl(boolean useDeviceUrl) {
        return useDeviceUrl ? BASE_URL_DEVICE : BASE_URL_EMULATOR;
    }
} 