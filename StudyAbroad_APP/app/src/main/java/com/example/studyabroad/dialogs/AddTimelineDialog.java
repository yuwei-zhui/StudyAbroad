package com.example.studyabroad.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.studyabroad.R;
import com.example.studyabroad.models.TimelineItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddTimelineDialog extends DialogFragment {

    public interface OnTimelineItemAddedListener {
        void onTimelineItemAdded(TimelineItem item);
    }

    private OnTimelineItemAddedListener listener;
    private TextInputEditText etTitle, etDescription, etDate, etStatus;
    private MaterialButton btnSave, btnCancel;
    private Calendar selectedDate;
    private int selectedStatus = TimelineItem.STATUS_PENDING;

    public void setOnTimelineItemAddedListener(OnTimelineItemAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_timeline, null);

        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        etDate = view.findViewById(R.id.etDate);
        etStatus = view.findViewById(R.id.etStatus);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);

        selectedDate = Calendar.getInstance();
        updateDateText();
        updateStatusText();

        setupListeners();

        builder.setView(view);
        return builder.create();
    }

    private void setupListeners() {
        etDate.setOnClickListener(v -> showDatePicker());
        etStatus.setOnClickListener(v -> showStatusPicker());
        
        btnSave.setOnClickListener(v -> saveTimelineItem());
        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            requireContext(),
            (view, year, month, dayOfMonth) -> {
                selectedDate.set(year, month, dayOfMonth);
                updateDateText();
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showStatusPicker() {
        String[] statuses = {"Pending", "Completed", "Overdue"};
        new AlertDialog.Builder(requireContext())
            .setTitle("Select Status")
            .setItems(statuses, (dialog, which) -> {
                selectedStatus = which;
                updateStatusText();
            })
            .show();
    }

    private void updateDateText() {
        String dateText = String.format("%02d/%02d/%04d",
            selectedDate.get(Calendar.DAY_OF_MONTH),
            selectedDate.get(Calendar.MONTH) + 1,
            selectedDate.get(Calendar.YEAR));
        etDate.setText(dateText);
    }

    private void updateStatusText() {
        String[] statuses = {"Pending", "Completed", "Overdue"};
        etStatus.setText(statuses[selectedStatus]);
    }

    private void saveTimelineItem() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Title is required");
            return;
        }

        TimelineItem item = new TimelineItem(
            title,
            description,
            selectedDate.getTime(),
            selectedStatus,
            "university1" // TODO: Get actual university ID
        );

        if (listener != null) {
            listener.onTimelineItemAdded(item);
        }
        dismiss();
    }
} 