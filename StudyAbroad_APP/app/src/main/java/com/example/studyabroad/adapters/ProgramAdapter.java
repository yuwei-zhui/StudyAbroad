package com.example.studyabroad.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyabroad.R;
import com.example.studyabroad.model.Program;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {

    private List<Program> programs = new ArrayList<>();
    private OnProgramClickListener listener;

    public interface OnProgramClickListener {
        void onProgramClick(Program program);
    }

    public ProgramAdapter(OnProgramClickListener listener) {
        this.listener = listener;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_program, parent, false);
        return new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        Program program = programs.get(position);
        holder.bind(program);
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    class ProgramViewHolder extends RecyclerView.ViewHolder {
        private TextView textProgramName;
        private TextView textProgramDetails;
        private TextView textTuition;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            textProgramName = itemView.findViewById(R.id.textProgramName);
            textProgramDetails = itemView.findViewById(R.id.textProgramDetails);
            textTuition = itemView.findViewById(R.id.textTuition);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onProgramClick(programs.get(position));
                }
            });
        }

        public void bind(Program program) {
            textProgramName.setText(program.getName());
            
            // Build program details text
            String details = program.getFormattedDuration() + " • " + 
                             program.getFieldOfStudy() + " • " + 
                             program.getDegreeType();
            textProgramDetails.setText(details);
            
            // Format tuition with currency symbol
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
            String tuition = format.format(program.getTuitionFeeInternational()) + "/yr";
            textTuition.setText(tuition);
        }
    }
} 