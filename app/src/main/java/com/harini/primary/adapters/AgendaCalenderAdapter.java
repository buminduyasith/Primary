package com.harini.primary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.harini.primary.R;
import com.harini.primary.models.CustomeEvent;

public class AgendaCalenderAdapter extends FirestoreRecyclerAdapter<CustomeEvent,AgendaCalenderAdapter.AgendaCalenderViewHolder> {

    public AgendaCalenderAdapter(@NonNull FirestoreRecyclerOptions<CustomeEvent> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AgendaCalenderAdapter.AgendaCalenderViewHolder holder, int position, @NonNull CustomeEvent model) {

        holder.txt_event_discription.setText(model.getDiscription());
    }

    @NonNull
    @Override
    public AgendaCalenderAdapter.AgendaCalenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_agendacalenderview_list,parent,false);

        return new AgendaCalenderAdapter.AgendaCalenderViewHolder(v);
    }

    public class AgendaCalenderViewHolder extends RecyclerView.ViewHolder {


        TextView txt_event_discription;

        public AgendaCalenderViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_event_discription = itemView.findViewById(R.id.txt_event_discription);
        }
    }
}
