package com.example.studyabroad.utils;

import com.example.studyabroad.models.ChatContext;
import java.util.ArrayList;
import java.util.List;

public class ChatContextManager {
    
    private static ChatContextManager instance;
    private ChatContext currentContext;
    private boolean contextProcessed = false;
    
    private ChatContextManager() {
        this.currentContext = new ChatContext();
    }
    
    public static ChatContextManager getInstance() {
        if (instance == null) {
            instance = new ChatContextManager();
        }
        return instance;
    }
    
    public void setContext(ChatContext context) {
        this.currentContext = context;
        this.contextProcessed = false; // Mark as new context that needs processing
    }
    
    public ChatContext getCurrentContext() {
        return currentContext;
    }
    
    public void clearContext() {
        this.currentContext = new ChatContext();
        this.contextProcessed = false;
    }
    
    public boolean hasContext() {
        return currentContext != null && 
               !ChatContext.CONTEXT_GENERAL.equals(currentContext.getContextType());
    }
    
    public boolean hasNewContext() {
        return hasContext() && !contextProcessed;
    }
    
    public void markContextAsProcessed() {
        this.contextProcessed = true;
    }
    
    public String getContextForAI() {
        if (currentContext != null) {
            return currentContext.getContextForAI();
        }
        return "";
    }
    
    public String getWelcomeMessage() {
        if (currentContext != null) {
            return currentContext.generateWelcomeMessage();
        }
        return "Hello! How can I help you with your study abroad plans?";
    }
    
    public List<String> getQuickReplyOptions() {
        List<String> options = new ArrayList<>();
        
        if (currentContext == null || ChatContext.CONTEXT_GENERAL.equals(currentContext.getContextType())) {
            // General quick replies
            options.add("Find universities");
            options.add("Application requirements");
            options.add("Scholarship information");
            options.add("Study destinations");
            options.add("Visa requirements");
        } else if (ChatContext.CONTEXT_UNIVERSITY.equals(currentContext.getContextType())) {
            // University-specific quick replies
            options.add("Admission requirements");
            options.add("Available programs");
            options.add("Tuition fees");
            options.add("Campus facilities");
            options.add("Application deadlines");
            options.add("Scholarship opportunities");
        } else if (ChatContext.CONTEXT_PROGRAM.equals(currentContext.getContextType())) {
            // Program-specific quick replies
            options.add("Admission requirements");
            options.add("Course curriculum");
            options.add("Career prospects");
            options.add("Application process");
            options.add("Tuition and fees");
            options.add("Success stories");
        }
        
        return options;
    }
    
    public String generateContextualPrompt(String userMessage) {
        StringBuilder prompt = new StringBuilder();
        
        // Add context information
        if (hasContext()) {
            prompt.append(getContextForAI()).append("\n\n");
        }
        
        // Add user message
        prompt.append("User Question: ").append(userMessage).append("\n\n");
        
        // Add specific instructions based on context
        if (currentContext != null) {
            switch (currentContext.getContextType()) {
                case ChatContext.CONTEXT_UNIVERSITY:
                    prompt.append("Please provide helpful information about ").append(currentContext.getUniversityName())
                          .append(" university, focusing on the user's question. Include specific details about admission requirements, programs, fees, and application processes when relevant.");
                    break;
                case ChatContext.CONTEXT_PROGRAM:
                    prompt.append("Please provide helpful information about the ").append(currentContext.getProgramName())
                          .append(" program at ").append(currentContext.getUniversityName())
                          .append(", focusing on the user's question. Include specific details about curriculum, admission requirements, career prospects, and application processes when relevant.");
                    break;
                default:
                    prompt.append("Please provide helpful advice for studying abroad based on the user's question.");
                    break;
            }
        } else {
            prompt.append("Please provide helpful advice for studying abroad based on the user's question.");
        }
        
        return prompt.toString();
    }
} 