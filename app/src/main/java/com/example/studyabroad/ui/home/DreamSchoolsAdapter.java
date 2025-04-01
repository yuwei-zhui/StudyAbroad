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
import com.example.studyabroad.database.entity.University;

import java.util.ArrayList;
import java.util.List;

public class DreamSchoolsAdapter extends RecyclerView.Adapter<DreamSchoolsAdapter.ViewHolder> {
    
    private List<University> universities = new ArrayList<>();
    private OnUniversityClickListener listener;
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dream_school, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        University university = universities.get(position);
        
        // Set university details
        holder.textViewSchoolName.setText(university.getName());
        holder.textViewLocation.setText(university.getCity() + ", " + university.getCountry());
        holder.textViewRanking.setText("QS: " + university.getQsRanking());
        
        // Set background color based on university (for demo purposes)
        int color;
        if (university.getName().contains("Melbourne")) {
            color = holder.itemView.getContext().getResources().getColor(R.color.orange_primary, null);
        } else if (university.getName().contains("Imperial")) {
            color = holder.itemView.getContext().getResources().getColor(R.color.blue_primary, null);
        } else if (university.getName().contains("Hongkong")) {
            color = holder.itemView.getContext().getResources().getColor(R.color.light_orange, null);
        } else if (university.getName().contains("Sydney")) {
            color = holder.itemView.getContext().getResources().getColor(R.color.colorPrimary, null);
        } else {
            color = holder.itemView.getContext().getResources().getColor(R.color.colorPrimary, null);
        }
        holder.viewBackground.setBackgroundColor(color);
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUniversityClick(university);
            }
        });
        
        // Set options click listener
        holder.imageViewMore.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOptionsClick(university, v);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return universities.size();
    }
    
    public void setUniversities(List<University> universities) {
        this.universities = universities;
        notifyDataSetChanged();
    }
    
    public void setOnUniversityClickListener(OnUniversityClickListener listener) {
        this.listener = listener;
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View viewBackground;
        private final TextView textViewSchoolName;
        private final TextView textViewLocation;
        private final TextView textViewRanking;
        private final ImageView imageViewSchoolIcon;
        private final ImageView imageViewMore;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            textViewSchoolName = itemView.findViewById(R.id.textViewSchoolName);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewRanking = itemView.findViewById(R.id.textViewRanking);
            imageViewSchoolIcon = itemView.findViewById(R.id.imageViewSchoolIcon);
            imageViewMore = itemView.findViewById(R.id.imageViewMore);
        }
    }
    
    public interface OnUniversityClickListener {
        void onUniversityClick(University university);
        void onOptionsClick(University university, View view);
    }
} 