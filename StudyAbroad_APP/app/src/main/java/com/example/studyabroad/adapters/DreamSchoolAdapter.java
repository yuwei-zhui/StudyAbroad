package com.example.studyabroad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;
import com.example.studyabroad.models.DreamSchool;

import java.util.ArrayList;
import java.util.List;

public class DreamSchoolAdapter extends RecyclerView.Adapter<DreamSchoolAdapter.DreamSchoolViewHolder> {

    private List<DreamSchool> dreamSchools = new ArrayList<>();
    private Context context;
    private OnDreamSchoolClickListener clickListener;

    public interface OnDreamSchoolClickListener {
        void onDreamSchoolClick(DreamSchool dreamSchool);
        void onRemoveClick(DreamSchool dreamSchool);
    }

    public DreamSchoolAdapter(Context context) {
        this.context = context;
    }

    public void setOnDreamSchoolClickListener(OnDreamSchoolClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public DreamSchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dream_school, parent, false);
        return new DreamSchoolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DreamSchoolViewHolder holder, int position) {
        DreamSchool dreamSchool = dreamSchools.get(position);
        
        holder.textUniversityName.setText(dreamSchool.getUniversityName());
        
        // Set card background color based on position or university name
        int backgroundColor = getCardBackgroundColor(position, dreamSchool.getUniversityName());
        holder.cardDreamSchool.setCardBackgroundColor(ContextCompat.getColor(context, backgroundColor));
        
        // Set program name, but hide if it's the general interest
        if ("General Interest".equals(dreamSchool.getProgramName())) {
            holder.textProgramName.setVisibility(View.GONE);
        } else {
            holder.textProgramName.setVisibility(View.VISIBLE);
            holder.textProgramName.setText(dreamSchool.getProgramName());
        }
        
        // Set location if available
        if (dreamSchool.getLocation() != null && !dreamSchool.getLocation().isEmpty()) {
            holder.textLocation.setText(dreamSchool.getLocation());
            holder.textLocation.setVisibility(View.VISIBLE);
        } else {
            holder.textLocation.setVisibility(View.GONE);
        }
        
        // Set ranking if available
        if (dreamSchool.getRanking() != null && !dreamSchool.getRanking().isEmpty()) {
            holder.textRanking.setText("#" + dreamSchool.getRanking());
            holder.textRanking.setVisibility(View.VISIBLE);
        } else {
            holder.textRanking.setVisibility(View.GONE);
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onDreamSchoolClick(dreamSchool);
            }
        });
        
        holder.imageRemove.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onRemoveClick(dreamSchool);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dreamSchools.size();
    }

    public void setDreamSchools(List<DreamSchool> dreamSchools) {
        this.dreamSchools = dreamSchools != null ? dreamSchools : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void removeDreamSchool(DreamSchool dreamSchool) {
        int position = dreamSchools.indexOf(dreamSchool);
        if (position != -1) {
            dreamSchools.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    private int getCardBackgroundColor(int position, String universityName) {
        // Use different colors based on university name or position
        if (universityName.toLowerCase().contains("melbourne")) {
            return R.color.orange_primary;
        } else if (universityName.toLowerCase().contains("imperial")) {
            return R.color.blue_primary;
        } else if (universityName.toLowerCase().contains("stanford")) {
            return R.color.purple_primary;
        } else if (universityName.toLowerCase().contains("cambridge")) {
            return R.color.light_green;
        } else {
            // Cycle through colors based on position
            int[] colors = {R.color.orange_primary, R.color.blue_primary, R.color.purple_primary, R.color.light_green};
            return colors[position % colors.length];
        }
    }

    static class DreamSchoolViewHolder extends RecyclerView.ViewHolder {
        CardView cardDreamSchool;
        TextView textUniversityName;
        TextView textProgramName;
        TextView textLocation;
        TextView textRanking;
        ImageView imageRemove;

        DreamSchoolViewHolder(@NonNull View itemView) {
            super(itemView);
            cardDreamSchool = itemView.findViewById(R.id.cardDreamSchool);
            textUniversityName = itemView.findViewById(R.id.textUniversityName);
            textProgramName = itemView.findViewById(R.id.textProgramName);
            textLocation = itemView.findViewById(R.id.textLocation);
            textRanking = itemView.findViewById(R.id.textRanking);
            imageRemove = itemView.findViewById(R.id.imageRemove);
        }
    }
} 