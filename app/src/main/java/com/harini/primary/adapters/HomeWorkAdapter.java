package com.harini.primary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.harini.primary.Models.Homework;
import com.harini.primary.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeWorkAdapter extends FirestoreRecyclerAdapter<Homework,HomeWorkAdapter.HomeWorkViewHolder> {

    private String role;
    private onItemClickListner listner;

    public HomeWorkAdapter(@NonNull FirestoreRecyclerOptions<Homework> options,String role) {
        super(options);
        this.role = role;
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeWorkAdapter.HomeWorkViewHolder holder, int position, @NonNull Homework model) {

        Timestamp ts=model.getTimestamp();
        Date date=ts.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        String time =sdf.format(date);


        holder.txthwdiscription.setText(model.getDiscription());

        holder.txthwtime.setText(time);
    }

    @NonNull
    @Override
    public HomeWorkAdapter.HomeWorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_add_homework,parent,false);

        return new  HomeWorkAdapter.HomeWorkViewHolder(v);
    }

    public class HomeWorkViewHolder extends RecyclerView.ViewHolder {

        TextView txthwdiscription;
        TextView txthwtime;

        public HomeWorkViewHolder(@NonNull View itemView) {
            super(itemView);

            txthwdiscription = itemView.findViewById(R.id.txthwdiscription);
            txthwtime = itemView.findViewById(R.id.txthwtime);
        }
    }

    public interface onItemClickListner{

        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListner(onItemClickListner listner){


        this.listner = listner;
    }
}
