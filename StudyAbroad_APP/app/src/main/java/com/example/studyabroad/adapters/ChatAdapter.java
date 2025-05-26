package com.example.studyabroad.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;
import com.example.studyabroad.models.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> messages;
    private final SimpleDateFormat timeFormat;
    private OnMessageLongClickListener longClickListener;

    public interface OnMessageLongClickListener {
        void onMessageLongClick(ChatMessage message);
    }

    public ChatAdapter(List<ChatMessage> messages, OnMessageLongClickListener longClickListener) {
        this.messages = messages;
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        String time = timeFormat.format(new Date(message.getTimestamp()));
        
        if (message.getType() == ChatMessage.TYPE_USER) {
            holder.layoutUserMessage.setVisibility(View.VISIBLE);
            holder.layoutAIMessage.setVisibility(View.GONE);
            holder.tvUserMessage.setText(message.getMessage());
            holder.tvUserTime.setText(time);
        } else {
            holder.layoutUserMessage.setVisibility(View.GONE);
            holder.layoutAIMessage.setVisibility(View.VISIBLE);
            holder.tvAIMessage.setText(message.getMessage());
            holder.tvAITime.setText(time);
        }

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onMessageLongClick(message);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateMessages(List<ChatMessage> newMessages) {
        this.messages = newMessages;
        notifyDataSetChanged();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutUserMessage, layoutAIMessage;
        TextView tvUserMessage, tvAIMessage, tvUserTime, tvAITime;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutUserMessage = itemView.findViewById(R.id.layoutUserMessage);
            layoutAIMessage = itemView.findViewById(R.id.layoutAIMessage);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);
            tvAIMessage = itemView.findViewById(R.id.tvAIMessage);
            tvUserTime = itemView.findViewById(R.id.tvUserTime);
            tvAITime = itemView.findViewById(R.id.tvAITime);
        }
    }
} 