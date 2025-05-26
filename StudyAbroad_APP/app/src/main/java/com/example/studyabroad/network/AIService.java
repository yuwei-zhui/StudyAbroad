package com.example.studyabroad.network;

import com.example.studyabroad.models.AIRequest;
import com.example.studyabroad.models.AIResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AIService {
    @POST("chat")
    Call<AIResponse> getResponse(@Body AIRequest request);
} 