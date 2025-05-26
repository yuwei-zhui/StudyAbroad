package com.example.studyabroad.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategorySelectionAdapter extends RecyclerView.Adapter<CategorySelectionAdapter.ViewHolder> {

    private List<String> allCategories = new ArrayList<>();
    private List<String> filteredCategories = new ArrayList<>();
    private Set<String> selectedCategories = new HashSet<>();
    private OnCategorySelectionListener listener;

    public interface OnCategorySelectionListener {
        void onCategorySelectionChanged(Set<String> selectedCategories);
    }

    public CategorySelectionAdapter(List<String> categories, OnCategorySelectionListener listener) {
        this.allCategories = new ArrayList<>(categories);
        this.filteredCategories = new ArrayList<>(categories);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = filteredCategories.get(position);
        holder.textViewCategoryName.setText(category);
        holder.checkboxCategory.setChecked(selectedCategories.contains(category));

        // Set click listeners
        View.OnClickListener clickListener = v -> {
            if (selectedCategories.contains(category)) {
                selectedCategories.remove(category);
            } else {
                selectedCategories.add(category);
            }
            holder.checkboxCategory.setChecked(selectedCategories.contains(category));
            if (listener != null) {
                listener.onCategorySelectionChanged(selectedCategories);
            }
        };

        holder.itemView.setOnClickListener(clickListener);
        holder.checkboxCategory.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return filteredCategories.size();
    }

    public void filterByLetter(String letter) {
        filteredCategories.clear();
        if (letter.equals("All")) {
            filteredCategories.addAll(allCategories);
        } else {
            for (String category : allCategories) {
                if (category.toUpperCase().startsWith(letter.toUpperCase())) {
                    filteredCategories.add(category);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterBySearch(String query) {
        filteredCategories.clear();
        if (query.isEmpty()) {
            filteredCategories.addAll(allCategories);
        } else {
            for (String category : allCategories) {
                if (category.toLowerCase().contains(query.toLowerCase())) {
                    filteredCategories.add(category);
                }
            }
        }
        notifyDataSetChanged();
    }

    public Set<String> getSelectedCategories() {
        return new HashSet<>(selectedCategories);
    }

    public void setSelectedCategories(Set<String> categories) {
        this.selectedCategories = new HashSet<>(categories);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkboxCategory;
        TextView textViewCategoryName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxCategory = itemView.findViewById(R.id.checkboxCategory);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
        }
    }
} 