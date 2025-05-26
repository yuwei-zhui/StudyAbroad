package com.example.studyabroad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;
import com.example.studyabroad.model.University;
import com.example.studyabroad.database.DreamListDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UniversitySearchAdapter extends RecyclerView.Adapter<UniversitySearchAdapter.ViewHolder> {

    private List<University> universities = new ArrayList<>();
    private Context context;
    private OnUniversityClickListener universityClickListener;
    private OnProgramClickListener programClickListener;
    private OnAddToTimelineListener addToTimelineListener;
    private OnChatClickListener chatClickListener;
    private OnHeartClickListener heartClickListener;

    public interface OnUniversityClickListener {
        void onUniversityClick(University university);
    }

    public interface OnProgramClickListener {
        void onProgramClick(com.example.studyabroad.model.Program program);
    }

    public interface OnAddToTimelineListener {
        void onAddToTimeline(com.example.studyabroad.model.Program program, String universityName);
    }

    public interface OnChatClickListener {
        void onChatClick(com.example.studyabroad.model.Program program, String universityName);
    }

    public interface OnHeartClickListener {
        void onHeartClick(com.example.studyabroad.model.Program program, String universityName, boolean isAdding);
    }

    public UniversitySearchAdapter(Context context, OnUniversityClickListener universityClickListener, OnProgramClickListener programClickListener) {
        this.context = context;
        this.universityClickListener = universityClickListener;
        this.programClickListener = programClickListener;
    }

    public void setOnAddToTimelineListener(OnAddToTimelineListener listener) {
        this.addToTimelineListener = listener;
    }

    public void setOnChatClickListener(OnChatClickListener listener) {
        this.chatClickListener = listener;
    }

    public void setOnHeartClickListener(OnHeartClickListener listener) {
        this.heartClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        University university = universities.get(position);
        
        // Set basic information
        holder.textViewUniversityName.setText(university.getName());
        holder.textViewLocation.setText(university.getCity() + ", " + university.getCountry());
        holder.textViewRanking.setText(String.valueOf(university.getRanking()));
        
        // Set detailed information
        holder.textViewWebsite.setText(university.getWebsiteUrl() != null ? university.getWebsiteUrl() : "N/A");
        holder.textViewQSRanking.setText(String.valueOf(university.getRanking()));
        holder.textViewLocationDetail.setText(university.getCity() + ", " + university.getCountry());
        
        // Check if there are recommended courses
        if (university.getAdditionalInfo() != null && 
            university.getAdditionalInfo().containsKey("programs")) {
            
            @SuppressWarnings("unchecked")
            List<com.example.studyabroad.model.Program> programs = 
                (List<com.example.studyabroad.model.Program>) university.getAdditionalInfo().get("programs");
            
            if (programs != null && !programs.isEmpty()) {
                // Show first recommended course
                com.example.studyabroad.model.Program firstProgram = programs.get(0);
                showCourseRecommendation(holder, firstProgram);
            } else {
                holder.layoutCourseRecommendation.setVisibility(View.GONE);
            }
        } else {
            holder.layoutCourseRecommendation.setVisibility(View.GONE);
        }
        
        // Set click events
        holder.itemView.setOnClickListener(v -> {
            if (universityClickListener != null) {
                universityClickListener.onUniversityClick(university);
            }
        });
    }

    private void showCourseRecommendation(ViewHolder holder, com.example.studyabroad.model.Program program) {
        holder.layoutCourseRecommendation.setVisibility(View.VISIBLE);
        
        // Get current university once at the beginning
        University currentUniversity = universities.get(holder.getAdapterPosition());
        
        holder.textViewCourseName.setText(program.getName());
        
        // Set course detailed information
        if (program.getDurationYears() > 0) {
            String duration = program.getDurationYears() + " years full time";
            holder.textViewDuration.setText(duration);
        } else {
            holder.textViewDuration.setText("Duration varies");
        }
        
        // Set teaching mode
        String mode = program.getStudyFormat() != null ? program.getStudyFormat() : "On campus";
        holder.textViewMode.setText(mode);
        
        // Set intake time and application deadline
        holder.textViewIntake.setText("Start year (February intake) application due");
        holder.textViewDeadline.setText("31 August 2025");
        
        // Set tuition fee information
        if (program.getTuitionFeeInternational() > 0) {
            String currency = program.getCurrency() != null ? program.getCurrency() : "USD";
            String fee = currency + " $" + String.format("%.0f", program.getTuitionFeeInternational()) + 
                        " (Indicative total course fee)";
            holder.textViewFees.setText(fee);
        } else {
            holder.textViewFees.setText("Contact university for fee information");
        }
        
        // Set admission requirements
        if (program.getMinimumGPA() > 0) {
            holder.textViewRequirements.setText("Min GPA: " + program.getMinimumGPA() + " - Admission criteria");
        } else {
            holder.textViewRequirements.setText("Admission criteria");
        }
        
        // Set heart icon based on dream list status
        DreamListDatabaseHelper dreamListDb = DreamListDatabaseHelper.getInstance(context);
        boolean isDreamSchool = dreamListDb.isDreamSchool(currentUniversity.getName(), program.getName());
        holder.imageViewHeart.setImageResource(isDreamSchool ? R.drawable.ic_heart : R.drawable.ic_heart_outline);
        
        // Set course click events
        holder.layoutCourseRecommendation.setOnClickListener(v -> {
            if (programClickListener != null) {
                programClickListener.onProgramClick(program);
            }
        });
        
        // Set "VIEW MORE DETAILS" click event
        holder.textViewMoreDetails.setOnClickListener(v -> {
            if (programClickListener != null) {
                programClickListener.onProgramClick(program);
            }
        });
        
        // Set "Add to timeline" button click event
        holder.imageViewAddToTimeline.setOnClickListener(v -> {
            if (addToTimelineListener != null) {
                addToTimelineListener.onAddToTimeline(program, currentUniversity.getName());
            }
        });
        
        // Set heart button click event
        holder.imageViewHeart.setOnClickListener(v -> {
            if (heartClickListener != null) {
                // Toggle heart state - this will be handled in the fragment
                heartClickListener.onHeartClick(program, currentUniversity.getName(), true);
            }
        });

        // Set chat button click event
        holder.imageViewChat.setOnClickListener(v -> {
            if (chatClickListener != null) {
                chatClickListener.onChatClick(program, currentUniversity.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return universities.size();
    }

    public void setUniversities(List<University> universities) {
        this.universities = universities != null ? universities : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewLogo;
        TextView textViewUniversityName;
        TextView textViewLocation;
        TextView textViewRanking;
        TextView textViewWebsite;
        TextView textViewQSRanking;
        TextView textViewLocationDetail;
        
        // Course recommendation views
        LinearLayout layoutCourseRecommendation;
        TextView textViewCourseName;
        TextView textViewDuration;
        TextView textViewMode;
        TextView textViewIntake;
        TextView textViewDeadline;
        TextView textViewFees;
        TextView textViewRequirements;
        TextView textViewMoreDetails;
        ImageView imageViewAddToTimeline;
        ImageView imageViewHeart;
        ImageView imageViewChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            imageViewLogo = itemView.findViewById(R.id.imageViewLogo);
            textViewUniversityName = itemView.findViewById(R.id.textViewUniversityName);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewRanking = itemView.findViewById(R.id.textViewRanking);
            textViewWebsite = itemView.findViewById(R.id.textViewWebsite);
            textViewQSRanking = itemView.findViewById(R.id.textViewQSRanking);
            textViewLocationDetail = itemView.findViewById(R.id.textViewLocationDetail);
            
            // Course recommendation views
            layoutCourseRecommendation = itemView.findViewById(R.id.layoutCourseRecommendation);
            textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
            textViewMode = itemView.findViewById(R.id.textViewMode);
            textViewIntake = itemView.findViewById(R.id.textViewIntake);
            textViewDeadline = itemView.findViewById(R.id.textViewDeadline);
            textViewFees = itemView.findViewById(R.id.textViewFees);
            textViewRequirements = itemView.findViewById(R.id.textViewRequirements);
            textViewMoreDetails = itemView.findViewById(R.id.textViewMoreDetails);
            imageViewAddToTimeline = itemView.findViewById(R.id.imageViewAddToTimeline);
            imageViewHeart = itemView.findViewById(R.id.imageViewHeart);
            imageViewChat = itemView.findViewById(R.id.imageViewChat);
        }
    }
} 