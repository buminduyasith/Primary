package com.harini.primary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.harini.primary.R;
import com.harini.primary.models.VideoLesson;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddVideoLessonsAdapter extends FirestoreRecyclerAdapter<VideoLesson,AddVideoLessonsAdapter.AddVideoLessonsViewHolder> {

    private onItemClickListner listner;


    public AddVideoLessonsAdapter(@NonNull FirestoreRecyclerOptions<VideoLesson> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AddVideoLessonsAdapter.AddVideoLessonsViewHolder holder, int position, @NonNull VideoLesson model) {

        Timestamp ts=model.getTimestamp();
        Date date=ts.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        String time =sdf.format(date);


        holder.txttile.setText(model.getTitle());

        holder.txttime.setText(time);

    }

    @NonNull
    @Override
    public AddVideoLessonsAdapter.AddVideoLessonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_lesson_video,parent,false);

        return new AddVideoLessonsAdapter.AddVideoLessonsViewHolder(v);
    }

    public class AddVideoLessonsViewHolder extends RecyclerView.ViewHolder {

        TextView txttile;
        TextView txttime;
        ImageButton imgbtnremove;

        public AddVideoLessonsViewHolder(@NonNull View itemView) {
            super(itemView);

            txttile = itemView.findViewById(R.id.txtvideotitle);
            txttime = itemView.findViewById(R.id.txttime);
            imgbtnremove = itemView.findViewById(R.id.btndelete);

            imgbtnremove.setOnClickListener(new View.OnClickListener() {
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
