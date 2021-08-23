package com.harini.primary.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harini.primary.R;
import com.harini.primary.models.Period;
import com.harini.primary.utill.TimeFormatter;

import java.text.MessageFormat;
import java.util.ArrayList;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.PeriodViewHolder> {

    private final ArrayList<Period> periodsList;
    private String userRole, showingDay;
    private boolean isView;
    private OnItemClickListener listener;

    public TimeTableAdapter(ArrayList<Period> periodsList, String userRole, boolean isView, String showingDay) {
        this.periodsList = periodsList;
        this.userRole = userRole;
        this.isView = isView;
        this.showingDay = showingDay;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public PeriodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.period_item, parent, false);
        return new PeriodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PeriodViewHolder holder, int position) {
        Period period = periodsList.get(holder.getAdapterPosition());

        if ("ADMIN".equalsIgnoreCase(userRole)) {
            if (isView) {
                holder.etTitle.setEnabled(false);
                holder.btnEdit.setVisibility(View.VISIBLE);
            } else {
                holder.etTitle.setEnabled(true);
                holder.btnEdit.setVisibility(View.GONE);
            }
        } else { // teacher & parent view timetable
            holder.etTitle.setEnabled(false);
            holder.btnEdit.setVisibility(View.GONE);
        }

        holder.tvTime.setText(MessageFormat.format("{0}\n - \n{1}",
                TimeFormatter.convert24hToAmPm(period.getStartTime()), TimeFormatter.convert24hToAmPm(period.getEndTime())));

        if (isView) {
            holder.etTitle.setText(period.getTitle());
        } else { // add timetable - admin
            holder.etTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null) {
                        period.setTitle(s.toString());
                    }
                }
            });
        }

        if (holder.btnEdit.getVisibility() == View.VISIBLE) {
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(showingDay,period.getTitle(), holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return periodsList.size();
    }

    public ArrayList<Period> getPeriodsList() {
        return periodsList;
    }

    public static class PeriodViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final EditText etTitle;
        private final ImageView btnEdit;

        public PeriodViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            etTitle = itemView.findViewById(R.id.etTitle);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(String day, String currentSubjectName, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateSubjectName(int position, String newTitle){
        periodsList.get(position).setTitle(newTitle);
        notifyDataSetChanged();
    }
}