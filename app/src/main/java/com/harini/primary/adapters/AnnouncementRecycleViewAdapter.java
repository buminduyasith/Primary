package com.harini.primary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.harini.primary.R;
import com.harini.primary.models.Announcement;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnnouncementRecycleViewAdapter extends FirestoreRecyclerAdapter<Announcement, AnnouncementRecycleViewAdapter.AnnouncementRecycleViewAdapterViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private String role;
    private onItemClickListner listner;

    public AnnouncementRecycleViewAdapter(@NonNull FirestoreRecyclerOptions<Announcement> options,String role) {
        super(options);
        this.role = role;
    }

    @Override
    protected void onBindViewHolder(@NonNull AnnouncementRecycleViewAdapterViewHolder holder, int position, @NonNull Announcement model) {

        Timestamp ts=model.getTimestamp();
        Date date=ts.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        String time =sdf.format(date);

        if(role=="TEACHER"){

            holder.txtmessage.setText(model.getMessage());
            holder.txttime.setText(time);

        }else{


            holder.img_del.setVisibility(View.GONE);
            holder.txtmessage.setText(model.getMessage());
            holder.txttime.setText(time);
        }



    }

    @NonNull
    @Override
    public AnnouncementRecycleViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_announcment_list,parent,false);

        return new AnnouncementRecycleViewAdapter.AnnouncementRecycleViewAdapterViewHolder(v);
    }

    public class AnnouncementRecycleViewAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView txtmessage;
        TextView txttime;
        ImageView img_del;

        public AnnouncementRecycleViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            txtmessage = itemView.findViewById(R.id.txt_message);
            txttime = itemView.findViewById(R.id.txt_time);
            img_del = itemView.findViewById(R.id.img_del_announcement);

            img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION && listner !=null){
                        listner.onItemClick(getSnapshots().getSnapshot(pos),pos);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public interface onItemClickListner{

        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListner(onItemClickListner listner){


        this.listner = listner;
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }
}
