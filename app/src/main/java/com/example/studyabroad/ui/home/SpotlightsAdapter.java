package com.example.studyabroad.ui.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;

import java.util.ArrayList;
import java.util.List;

public class SpotlightsAdapter extends RecyclerView.Adapter<SpotlightsAdapter.ViewHolder> {
    
    private List<SpotlightItem> spotlights = new ArrayList<>();
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
        SpotlightItem spotlightItem = spotlights.get(position);
        
        // Set spotlight details
        holder.textViewTitle.setText(spotlightItem.getTitle());
        holder.imageViewSpotlight.setImageResource(spotlightItem.getImageResId());
        
        // Set background color
        try {
            if (spotlightItem.getBackgroundColor() != null) {
                holder.cardView.setCardBackgroundColor(Color.parseColor(spotlightItem.getBackgroundColor()));
            }
        } catch (Exception e) {
            // If color parsing fails, use default color
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.success_green, null));
        }
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSpotlightClick(spotlightItem, position);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return spotlights.size();
    }
    
    public void setSpotlights(List<SpotlightItem> spotlights) {
        this.spotlights = spotlights;
        notifyDataSetChanged();
    }
    
    public void setOnSpotlightClickListener(OnSpotlightClickListener listener) {
        this.listener = listener;
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView textViewTitle;
        private final ImageView imageViewSpotlight;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            textViewTitle = itemView.findViewById(R.id.textViewSpotlightTitle);
            imageViewSpotlight = itemView.findViewById(R.id.imageViewSpotlight);
        }
    }
    
    public interface OnSpotlightClickListener {
        void onSpotlightClick(SpotlightItem spotlightItem, int position);
    }
} 