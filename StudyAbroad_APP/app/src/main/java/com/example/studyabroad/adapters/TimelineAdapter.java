package com.example.studyabroad.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;
import com.example.studyabroad.models.TimelineItem;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {

    private List<TimelineItem> timelineItems;
    private final SimpleDateFormat dateFormat;
    private OnTimelineItemActionListener listener;
    private Context context;

    public interface OnTimelineItemActionListener {
        void onEditItem(TimelineItem item);
        void onDeleteItem(TimelineItem item);
    }

    public TimelineAdapter(List<TimelineItem> timelineItems, Context context) {
        this.timelineItems = timelineItems;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        this.context = context;
        android.util.Log.d("TimelineAdapter", "Creating TimelineAdapter, initial item count: " + (timelineItems != null ? timelineItems.size() : 0));
    }

    public void setOnTimelineItemActionListener(OnTimelineItemActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline, parent, false);
        return new TimelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        TimelineItem item = timelineItems.get(position);
        
        android.util.Log.d("TimelineAdapter", "Binding timeline item: " + item.getTitle() + " (position: " + position + ")");
        
        // Set university name and program name
        holder.tvUniversityName.setText(item.getUniversityName() != null ? item.getUniversityName() : "Unknown University");
        holder.tvProgramName.setText(item.getProgramName() != null ? item.getProgramName() : item.getTitle());
        
        // Calculate and set days info
        setDaysInfo(holder, item.getDate());
        
        // Set card background color based on deadline status
        setCardBackgroundColor(holder, item.getDate());

        // Setup menu click
        holder.ivMenu.setOnClickListener(v -> showPopupMenu(v, item));
    }

    private void setDaysInfo(TimelineViewHolder holder, Date targetDate) {
        Date currentDate = new Date();
        long diffInMillis = targetDate.getTime() - currentDate.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
        
        if (diffInMillis <= 0) {
            // Expired
            holder.tvOpensInLabel.setText("Expired");
            holder.tvOpensInDays.setText("-" + Math.abs(days));
            holder.tvClosesInLabel.setText("Closed");
            holder.tvClosesInDays.setText("-" + Math.abs(days));
        } else {
            Calendar openDate = Calendar.getInstance();
            openDate.setTime(targetDate);
            openDate.add(Calendar.DAY_OF_YEAR, -90);
            
            long openDiffInMillis = openDate.getTimeInMillis() - currentDate.getTime();
            long openDays = TimeUnit.MILLISECONDS.toDays(openDiffInMillis);
            
            if (openDiffInMillis > 0) {
                // Not yet open
                holder.tvOpensInLabel.setText("Opens in");
                holder.tvOpensInDays.setText(String.valueOf(openDays));
                holder.tvClosesInLabel.setText("Closes in");
                holder.tvClosesInDays.setText(String.valueOf(days));
            } else {
                // Already open
                holder.tvOpensInLabel.setText("Opened");
                holder.tvOpensInDays.setText("0");
                holder.tvClosesInLabel.setText("Closes in");
                holder.tvClosesInDays.setText(String.valueOf(days));
            }
        }
    }

    private void setCardBackgroundColor(TimelineViewHolder holder, Date targetDate) {
        Date currentDate = new Date();
        long diffInMillis = targetDate.getTime() - currentDate.getTime();
        
        int backgroundResource;
        if (diffInMillis <= 0) {
            // Expired - gray solid color
            backgroundResource = R.drawable.solid_timeline_card_4;
        } else {
            // Not expired - use different solid colors based on position
            int position = timelineItems.indexOf(getItemByDate(targetDate));
            switch (position % 4) {
                case 0:
                    backgroundResource = R.drawable.solid_timeline_card_1;
                    break;
                case 1:
                    backgroundResource = R.drawable.solid_timeline_card_2;
                    break;
                case 2:
                    backgroundResource = R.drawable.solid_timeline_card_3;
                    break;
                default:
                    backgroundResource = R.drawable.solid_timeline_card_4;
                    break;
            }
        }
        
        // Set solid color background
        holder.cardTimeline.setBackgroundResource(backgroundResource);
    }

    private TimelineItem getItemByDate(Date date) {
        for (TimelineItem item : timelineItems) {
            if (item.getDate().equals(date)) {
                return item;
            }
        }
        return timelineItems.get(0); // fallback
    }

    private void showPopupMenu(View view, TimelineItem item) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_timeline_item);
        
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.action_edit) {
                if (listener != null) {
                    listener.onEditItem(item);
                }
                return true;
            } else if (itemId == R.id.action_delete) {
                if (listener != null) {
                    listener.onDeleteItem(item);
                }
                return true;
            }
            return false;
        });
        
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        int count = timelineItems != null ? timelineItems.size() : 0;
        android.util.Log.d("TimelineAdapter", "getItemCount: " + count);
        return count;
    }

    public void updateTimelineItems(List<TimelineItem> newItems) {
        android.util.Log.d("TimelineAdapter", "Updating timeline items, new item count: " + (newItems != null ? newItems.size() : 0));
        this.timelineItems = newItems;
        notifyDataSetChanged();
    }

    static class TimelineViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardTimeline;
        TextView tvUniversityName, tvProgramName;
        TextView tvOpensInLabel, tvOpensInDays;
        TextView tvClosesInLabel, tvClosesInDays;
        ImageView ivMenu;

        TimelineViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTimeline = itemView.findViewById(R.id.cardTimeline);
            tvUniversityName = itemView.findViewById(R.id.tvUniversityName);
            tvProgramName = itemView.findViewById(R.id.tvProgramName);
            tvOpensInLabel = itemView.findViewById(R.id.tvOpensInLabel);
            tvOpensInDays = itemView.findViewById(R.id.tvOpensInDays);
            tvClosesInLabel = itemView.findViewById(R.id.tvClosesInLabel);
            tvClosesInDays = itemView.findViewById(R.id.tvClosesInDays);
            ivMenu = itemView.findViewById(R.id.ivMenu);
        }
    }
} 