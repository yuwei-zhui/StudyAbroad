package com.example.studyabroad.adapters;

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

import java.util.List;

public class SpotlightAdapter extends RecyclerView.Adapter<SpotlightAdapter.SpotlightViewHolder> {

    private List<Spotlight> spotlights;

    public SpotlightAdapter(List<Spotlight> spotlights) {
        this.spotlights = spotlights;
    }

    @NonNull
    @Override
    public SpotlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spotlight, parent, false);
        return new SpotlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpotlightViewHolder holder, int position) {
        Spotlight spotlight = spotlights.get(position);
        
        // Set title
        holder.textViewSpotlightTitle.setText(spotlight.getTitle());
        
        // Set background color
        int color = ContextCompat.getColor(holder.itemView.getContext(), spotlight.getBackgroundColor());
        holder.constraintLayoutBackground.setBackgroundColor(color);
        
        // Set icon
        holder.imageViewSpotlightIcon.setImageResource(spotlight.getIconResId());
    }

    @Override
    public int getItemCount() {
        return spotlights.size();
    }

    public static class SpotlightViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayoutBackground;
        TextView textViewSpotlightTitle;
        ImageView imageViewSpotlightIcon;

        public SpotlightViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutBackground = itemView.findViewById(R.id.constraintLayoutBackground);
            textViewSpotlightTitle = itemView.findViewById(R.id.textViewSpotlightTitle);
            imageViewSpotlightIcon = itemView.findViewById(R.id.imageViewSpotlightIcon);
        }
    }
} 