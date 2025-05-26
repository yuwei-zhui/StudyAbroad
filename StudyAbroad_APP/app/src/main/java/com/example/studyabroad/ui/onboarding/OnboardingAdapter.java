package com.example.studyabroad.ui.onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {
    
    private List<OnboardingItem> onboardingItems;
    
    public OnboardingAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }
    
    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_onboarding,
                        parent,
                        false
                )
        );
    }
    
    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.bind(onboardingItems.get(position));
    }
    
    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }
    
    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        
        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageView;
        
        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            
            textTitle = itemView.findViewById(R.id.textViewTitle);
            textDescription = itemView.findViewById(R.id.textViewDescription);
            imageView = itemView.findViewById(R.id.imageViewOnboarding);
        }
        
        void bind(OnboardingItem onboardingItem) {
            textTitle.setText(onboardingItem.getTitle());
            textDescription.setText(onboardingItem.getDescription());
            imageView.setImageResource(onboardingItem.getImageResId());
        }
    }
} 