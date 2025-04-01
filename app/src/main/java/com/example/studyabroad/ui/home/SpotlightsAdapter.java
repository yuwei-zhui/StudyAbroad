package com.example.studyabroad.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;
import com.example.studyabroad.models.Spotlight;

import java.util.ArrayList;
import java.util.List;

public class SpotlightsAdapter extends RecyclerView.Adapter<SpotlightsAdapter.ViewHolder> {
    
    private List<Spotlight> spotlights = new ArrayList<>();
    private OnSpotlightClickListener listener;
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_spotlight, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Spotlight spotlight = spotlights.get(position);
        
        // Set spotlight details
        holder.textViewTitle.setText(spotlight.getTitle());
        holder.imageViewSpotlightIcon.setImageResource(spotlight.getIconResId());
        
        // Set background color
        int color = ContextCompat.getColor(holder.itemView.getContext(), spotlight.getBackgroundColor());
        holder.constraintLayoutBackground.setBackgroundColor(color);
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSpotlightClick(spotlight, position);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return spotlights.size();
    }
    
    public void setSpotlights(List<Spotlight> spotlights) {
        this.spotlights = spotlights;
        notifyDataSetChanged();
    }
    
    public void setOnSpotlightClickListener(OnSpotlightClickListener listener) {
        this.listener = listener;
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout constraintLayoutBackground;
        private final TextView textViewTitle;
        private final ImageView imageViewSpotlightIcon;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutBackground = itemView.findViewById(R.id.constraintLayoutBackground);
            textViewTitle = itemView.findViewById(R.id.textViewSpotlightTitle);
            imageViewSpotlightIcon = itemView.findViewById(R.id.imageViewSpotlightIcon);
        }
    }
    
    public interface OnSpotlightClickListener {
        void onSpotlightClick(Spotlight spotlight, int position);
    }
} 