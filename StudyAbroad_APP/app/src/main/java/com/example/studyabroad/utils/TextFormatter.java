package com.example.studyabroad.utils;

import java.util.regex.Pattern;

public class TextFormatter {
    
    private static final Pattern BOLD_PATTERN = Pattern.compile("\\*\\*(.*?)\\*\\*");
    private static final Pattern ITALIC_PATTERN = Pattern.compile("\\*(.*?)\\*");
    private static final Pattern CODE_PATTERN = Pattern.compile("`(.*?)`");
    private static final Pattern HEADER_PATTERN = Pattern.compile("^#{1,6}\\s*(.*)$", Pattern.MULTILINE);
    private static final Pattern LIST_PATTERN = Pattern.compile("^[\\-\\*\\+]\\s+(.*)$", Pattern.MULTILINE);
    private static final Pattern NUMBERED_LIST_PATTERN = Pattern.compile("^\\d+\\.\\s+(.*)$", Pattern.MULTILINE);
    private static final Pattern MULTIPLE_NEWLINES = Pattern.compile("\n{3,}");
    private static final Pattern MULTIPLE_SPACES = Pattern.compile(" {2,}");
    
    public static String formatAIResponse(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return rawText;
        }
        
        String formatted = rawText;
        
        // Remove bold formatting but keep the text
        formatted = BOLD_PATTERN.matcher(formatted).replaceAll("$1");
        
        // Remove italic formatting but keep the text
        formatted = ITALIC_PATTERN.matcher(formatted).replaceAll("$1");
        
        // Remove code formatting but keep the text
        formatted = CODE_PATTERN.matcher(formatted).replaceAll("$1");
        
        // Clean up headers - remove # symbols
        formatted = HEADER_PATTERN.matcher(formatted).replaceAll("$1");
        
        // Format lists - replace bullets with proper bullets
        formatted = LIST_PATTERN.matcher(formatted).replaceAll("• $1");
        
        // Format numbered lists
        formatted = NUMBERED_LIST_PATTERN.matcher(formatted).replaceAll("$0");
        
        // Clean up excessive newlines
        formatted = MULTIPLE_NEWLINES.matcher(formatted).replaceAll("\n\n");
        
        // Clean up excessive spaces
        formatted = MULTIPLE_SPACES.matcher(formatted).replaceAll(" ");
        
        // Trim whitespace
        formatted = formatted.trim();
        
        return formatted;
    }
    
    public static String formatUserMessage(String message) {
        if (message == null) {
            return "";
        }
        return message.trim();
    }
    
    public static String cleanMarkdown(String text) {
        if (text == null) {
            return "";
        }
        
        // Remove all markdown formatting
        String cleaned = text
                .replaceAll("\\*\\*(.*?)\\*\\*", "$1")  // Bold
                .replaceAll("\\*(.*?)\\*", "$1")        // Italic
                .replaceAll("`(.*?)`", "$1")            // Code
                .replaceAll("^#{1,6}\\s*", "")          // Headers
                .replaceAll("^[\\-\\*\\+]\\s+", "• ")   // Lists
                .replaceAll("\n{3,}", "\n\n")          // Multiple newlines
                .replaceAll(" {2,}", " ")               // Multiple spaces
                .trim();
        
        return cleaned;
    }
} 