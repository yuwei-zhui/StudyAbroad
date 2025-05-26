package com.example.studyabroad.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;
import com.example.studyabroad.adapters.ChatAdapter;
import com.example.studyabroad.repositories.ChatRepository;
import com.example.studyabroad.models.AIRequest;
import com.example.studyabroad.models.AIResponse;
import com.example.studyabroad.models.ChatMessage;
import com.example.studyabroad.network.NetworkClient;
import com.example.studyabroad.network.AIService;
import com.example.studyabroad.network.ApiConfig;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.example.studyabroad.utils.UserProfileManager;
import com.example.studyabroad.utils.DebugHelper;
import com.example.studyabroad.utils.TextFormatter;
import com.example.studyabroad.utils.ChatContextManager;
import com.example.studyabroad.utils.SessionManager;
import com.example.studyabroad.models.ChatContext;
import com.example.studyabroad.database.entity.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";
    
    private RecyclerView recyclerViewChat;
    private EditText etMessage;
    private MaterialButton btnSend;
    private CircularProgressIndicator progressIndicator;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messages;
    private NetworkClient networkClient;
    private AIService aiService;
    private SearchView searchView;
    private ChatRepository chatRepository;
    private SessionManager sessionManager;
    private Handler mainHandler;
    private LinearLayout containerQuickReplies;
    private View layoutQuickReplies;
    private ChatContextManager contextManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DebugHelper.logTimestamp("ChatFragment onCreateView started");
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        
        // Initialize main handler for UI updates
        mainHandler = new Handler(Looper.getMainLooper());
        
        // Initialize views
        initializeViews(view);
        
        // Initialize database and network
        initializeServices();
        
        // Initialize messages list
        messages = new ArrayList<>();
        
        // Setup adapter and recycler view first
        setupRecyclerView();
        
        // Then load messages
        loadMessages();
        
        // Setup listeners
        setupListeners();
        
        // Log API configuration
        DebugHelper.logApiConfiguration(ApiConfig.BASE_URL, "chat");
        
        // Check for chat context and show welcome message if needed
        checkChatContext();
        
        Log.d(TAG, "ChatFragment initialized with " + messages.size() + " messages");
        DebugHelper.logTimestamp("ChatFragment onCreateView completed");
        
        return view;
    }

    private void initializeViews(View view) {
        recyclerViewChat = view.findViewById(R.id.recyclerViewChat);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        progressIndicator = view.findViewById(R.id.progressIndicator);
        containerQuickReplies = view.findViewById(R.id.containerQuickReplies);
        layoutQuickReplies = view.findViewById(R.id.layoutQuickReplies);
        
        Log.d(TAG, "Views initialized successfully");
    }

    private void initializeServices() {
        try {
            chatRepository = ChatRepository.getInstance(requireContext());
            sessionManager = SessionManager.getInstance(requireContext());
            networkClient = NetworkClient.getInstance();
            aiService = networkClient.getAIService();
            contextManager = ChatContextManager.getInstance();
            
            Log.d(TAG, "Services initialized successfully");
        } catch (Exception e) {
            DebugHelper.logException("initializeServices", e);
        }
    }

    private void setupRecyclerView() {
        chatAdapter = new ChatAdapter(messages, this::showDeleteDialog);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewChat.setAdapter(chatAdapter);
        
        Log.d(TAG, "RecyclerView setup completed");
    }

    private void setupListeners() {
        // Send message button click
        btnSend.setOnClickListener(v -> {
            DebugHelper.logTimestamp("Send button clicked");
            sendMessage();
        });
    }



    private void loadMessages() {
        try {
            DebugHelper.logTimestamp("Loading messages from database");
            messages.clear();
            
            // Get current user ID
            User currentUser = sessionManager.getUser();
            long userId = currentUser != null ? currentUser.getId() : 0;
            
            // Load messages for current user only
            messages.addAll(chatRepository.getAllMessages(userId));
            
            // Ensure adapter is initialized before updating
            if (chatAdapter != null) {
                chatAdapter.updateMessages(messages);
                if (!messages.isEmpty()) {
                    recyclerViewChat.scrollToPosition(messages.size() - 1);
                }
            }
            DebugHelper.logMessageList(messages, "Database Load for User " + userId);
        } catch (Exception e) {
            DebugHelper.logException("loadMessages", e);
        }
    }

    private void sendMessage() {
        String content = etMessage.getText().toString().trim();
        if (!content.isEmpty()) {
            DebugHelper.logTimestamp("Sending user message: " + content);
            
            // Format user message
            String formattedContent = TextFormatter.formatUserMessage(content);
            
            // Create and add user message
            ChatMessage userMessage = new ChatMessage(formattedContent, System.currentTimeMillis(), true);
            addUserMessage(userMessage);
            
            // Clear input field
            etMessage.setText("");
            
            // Send to AI (use original content for processing)
            sendMessageToAI(content);
        } else {
            Log.w(TAG, "Attempted to send empty message");
        }
    }

    private void sendMessageToAI(String userMessage) {
        DebugHelper.logTimestamp("Starting AI request for: " + userMessage);
        DebugHelper.logThreadInfo("sendMessageToAI");
        
        // Show loading indicator
        showLoading(true);
        
        try {
            // Get user background information
            UserProfileManager profileManager = UserProfileManager.getInstance(requireContext());
            String userBackground = profileManager.getUserBackgroundForAI();
            DebugHelper.logUserProfile(userBackground);
            
            // Generate contextual prompt if we have context
            String enhancedMessage = userMessage;
            if (contextManager.hasContext()) {
                enhancedMessage = contextManager.generateContextualPrompt(userMessage);
                // Hide quick replies after user sends a message
                hideQuickReplies();
            }
            
            // Create request object
            String userId = "user_" + System.currentTimeMillis();
            AIRequest request = new AIRequest(enhancedMessage, userId, userBackground);
            DebugHelper.logAIRequest(request);
            
            // Send request
            aiService.getResponse(request).enqueue(new Callback<AIResponse>() {
                @Override
                public void onResponse(@NonNull Call<AIResponse> call, @NonNull Response<AIResponse> response) {
                    DebugHelper.logTimestamp("AI Response received");
                    DebugHelper.logThreadInfo("onResponse");
                    Log.d(TAG, "AI Response - Status: " + response.code() + ", Success: " + response.isSuccessful());
                    
                    mainHandler.post(() -> {
                        DebugHelper.logThreadInfo("onResponse mainHandler");
                        showLoading(false);
                        
                        if (response.isSuccessful() && response.body() != null) {
                            AIResponse aiResponse = response.body();
                            DebugHelper.logAIResponse(aiResponse);
                            
                            String responseText = aiResponse.getResponse();
                            
                            if (responseText != null && !responseText.trim().isEmpty()) {
                                // Format the AI response to remove markdown and improve display
                                String formattedResponse = TextFormatter.formatAIResponse(responseText);
                                ChatMessage aiMessage = new ChatMessage(formattedResponse, System.currentTimeMillis(), false);
                                addAIMessage(aiMessage);
                                DebugHelper.showDebugToast(getContext(), "AI response added successfully");
                            } else {
                                String errorMsg = "AI response was empty";
                                Log.e(TAG, errorMsg);
                                showError(errorMsg);
                                DebugHelper.showDebugToast(getContext(), "Empty AI response");
                            }
                        } else {
                            String errorMsg = "AI Response failed - Code: " + response.code() + ", Message: " + response.message();
                            DebugHelper.logNetworkError("chat", response.code(), response.message());
                            showError("Failed to get response from AI assistant. Status: " + response.code());
                        }
                    });
                }
                
                @Override
                public void onFailure(@NonNull Call<AIResponse> call, @NonNull Throwable t) {
                    DebugHelper.logTimestamp("AI Request failed");
                    DebugHelper.logException("AI Request", new Exception(t));
                    
                    mainHandler.post(() -> {
                        showLoading(false);
                        String errorMsg = "Network error: " + t.getMessage();
                        showError(errorMsg);
                        DebugHelper.showDebugToast(getContext(), "Network failure: " + t.getClass().getSimpleName());
                    });
                }
            });
            
        } catch (Exception e) {
            DebugHelper.logException("sendMessageToAI", e);
            showLoading(false);
            showError("Error creating request: " + e.getMessage());
        }
    }

    private void showLoading(boolean show) {
        progressIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSend.setEnabled(!show);
        Log.d(TAG, "Loading indicator: " + (show ? "shown" : "hidden"));
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
        Log.e(TAG, "Error shown to user: " + message);
    }

    private void addUserMessage(ChatMessage userMessage) {
        try {
            DebugHelper.logChatMessage(userMessage, "ADDING_USER");
            
            User currentUser = sessionManager.getUser();
            long userId = currentUser != null ? currentUser.getId() : 0;
            
            chatRepository.saveMessage(userMessage, userId);
            DebugHelper.logDatabaseOperation("addMessage", true, "User message saved for user " + userId);
            
            messages.add(userMessage);
            chatAdapter.notifyItemInserted(messages.size() - 1);
            recyclerViewChat.scrollToPosition(messages.size() - 1);
            
            DebugHelper.logChatMessage(userMessage, "ADDED_USER");
        } catch (Exception e) {
            DebugHelper.logException("addUserMessage", e);
            DebugHelper.logDatabaseOperation("addMessage", false, e.getMessage());
        }
    }

    private void addAIMessage(ChatMessage aiMessage) {
        try {
            DebugHelper.logChatMessage(aiMessage, "ADDING_AI");
            DebugHelper.logThreadInfo("addAIMessage");
            
            User currentUser = sessionManager.getUser();
            long userId = currentUser != null ? currentUser.getId() : 0;
            
            chatRepository.saveMessage(aiMessage, userId);
            DebugHelper.logDatabaseOperation("addMessage", true, "AI message saved for user " + userId);
            
            mainHandler.post(() -> {
                DebugHelper.logThreadInfo("addAIMessage mainHandler");
                messages.add(aiMessage);
                chatAdapter.notifyItemInserted(messages.size() - 1);
                recyclerViewChat.scrollToPosition(messages.size() - 1);
                
                DebugHelper.logChatMessage(aiMessage, "ADDED_AI");
                DebugHelper.logMessageList(messages, "After AI Message Added");
            });
        } catch (Exception e) {
            DebugHelper.logException("addAIMessage", e);
            DebugHelper.logDatabaseOperation("addMessage", false, e.getMessage());
        }
    }

    private void showDeleteDialog(ChatMessage message) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Message")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    try {
                        chatRepository.deleteMessage(message.getId());
                        messages.remove(message);
                        chatAdapter.notifyDataSetChanged();
                        DebugHelper.logChatMessage(message, "DELETED");
                    } catch (Exception e) {
                        DebugHelper.logException("deleteMessage", e);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showClearAllDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Clear All Messages")
                .setMessage("Are you sure you want to delete all messages?")
                .setPositiveButton("Clear", (dialog, which) -> {
                    try {
                        User currentUser = sessionManager.getUser();
                        long userId = currentUser != null ? currentUser.getId() : 0;
                        
                        chatRepository.deleteAllMessagesForUser(userId);
                        messages.clear();
                        chatAdapter.notifyDataSetChanged();
                        DebugHelper.logTimestamp("All messages cleared for user " + userId);
                    } catch (Exception e) {
                        DebugHelper.logException("clearAllMessages", e);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void checkChatContext() {
        if (contextManager.hasNewContext()) {
            String welcomeMessage = contextManager.getWelcomeMessage();
            ChatMessage welcomeMsg = new ChatMessage(welcomeMessage, System.currentTimeMillis(), false);
            
            mainHandler.post(() -> {
                messages.add(welcomeMsg);
                if (chatAdapter != null) {
                    chatAdapter.notifyItemInserted(messages.size() - 1);
                    recyclerViewChat.scrollToPosition(messages.size() - 1);
                }
                
                try {
                    User currentUser = sessionManager.getUser();
                    long userId = currentUser != null ? currentUser.getId() : 0;
                    chatRepository.saveMessage(welcomeMsg, userId);
                } catch (Exception e) {
                    DebugHelper.logException("addWelcomeMessage", e);
                }
                
                showQuickReplies();
                
                contextManager.markContextAsProcessed();
            });
        } else if (contextManager.hasContext()) {
            showQuickReplies();
        }
    }
    
    private void showQuickReplies() {
        List<String> quickReplies = contextManager.getQuickReplyOptions();
        
        if (quickReplies.isEmpty()) {
            layoutQuickReplies.setVisibility(View.GONE);
            return;
        }
        
        // Clear existing quick replies
        containerQuickReplies.removeAllViews();
        
        // Add quick reply buttons
        for (String reply : quickReplies) {
            MaterialButton quickReplyBtn = new MaterialButton(requireContext());
            quickReplyBtn.setText(reply);
            quickReplyBtn.setTextSize(12);
            quickReplyBtn.setAllCaps(false);
            
            // Set button style
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            quickReplyBtn.setLayoutParams(params);
            
            // Set click listener
            quickReplyBtn.setOnClickListener(v -> {
                // Send the quick reply as a message
                etMessage.setText(reply);
                sendMessage();
                // Hide quick replies after selection
                layoutQuickReplies.setVisibility(View.GONE);
            });
            
            containerQuickReplies.addView(quickReplyBtn);
        }
        
        layoutQuickReplies.setVisibility(View.VISIBLE);
    }
    
    private void hideQuickReplies() {
        if (layoutQuickReplies != null) {
            layoutQuickReplies.setVisibility(View.GONE);
        }
    }
} 