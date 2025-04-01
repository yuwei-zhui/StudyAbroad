package com.example.studyabroad.adapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;
import com.example.studyabroad.models.School;

import java.util.List;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolViewHolder> {

    private List<School> schools;

    public SchoolAdapter(List<School> schools) {
        this.schools = schools;
    }

    @NonNull
    @Override
    public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dream_school, parent, false);
        return new SchoolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolViewHolder holder, int position) {
        School school = schools.get(position);
        
        // 设置学校名称
        holder.textViewSchoolName.setText(school.getName());
        
        // 设置位置信息
        holder.textViewLocation.setText(school.getLocation());
        
        // 设置排名
        holder.textViewRanking.setText("QS: " + school.getRanking());
        
        // 设置背景颜色
        int color = ContextCompat.getColor(holder.itemView.getContext(), school.getBackgroundColor());
        holder.viewBackground.setBackgroundColor(color);
        
        // 设置图标
        holder.imageViewSchoolIcon.setImageResource(school.getIconResId());
        holder.imageViewSchoolIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), R.color.white)));
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    public static class SchoolViewHolder extends RecyclerView.ViewHolder {
        View viewBackground;
        TextView textViewSchoolName;
        TextView textViewRanking;
        TextView textViewLocation;
        ImageView imageViewSchoolIcon;
        ImageView imageViewMore;

        public SchoolViewHolder(@NonNull View itemView) {
            super(itemView);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            textViewSchoolName = itemView.findViewById(R.id.textViewSchoolName);
            textViewRanking = itemView.findViewById(R.id.textViewRanking);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            imageViewSchoolIcon = itemView.findViewById(R.id.imageViewSchoolIcon);
            imageViewMore = itemView.findViewById(R.id.imageViewMore);
        }
    }
} 