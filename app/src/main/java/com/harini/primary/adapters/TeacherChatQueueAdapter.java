package com.harini.primary.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.harini.primary.Models.TeacherChatQueue;
import com.harini.primary.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TeacherChatQueueAdapter extends FirestoreRecyclerAdapter<TeacherChatQueue,TeacherChatQueueAdapter.TeacherChatQueueAdapterViewholder> {

    private onItemClickListner listner;

    public TeacherChatQueueAdapter( FirestoreRecyclerOptions<TeacherChatQueue> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(TeacherChatQueueAdapter.TeacherChatQueueAdapterViewholder holder, int position, TeacherChatQueue model) {

        Timestamp ts=model.getTimestamp();
        Date date=ts.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        String time =sdf.format(date);


        holder.txtUserName.setText(model.getName());
        holder.txtRecentMsg.setText(model.getRecentMsg());
        holder.txttime.setText(time);
    }


    @Override
    public TeacherChatQueueAdapter.TeacherChatQueueAdapterViewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chat_view,parent,false);

        return new TeacherChatQueueAdapter.TeacherChatQueueAdapterViewholder(v);
    }

    public class TeacherChatQueueAdapterViewholder extends RecyclerView.ViewHolder {

        TextView txtUserName;
        TextView txttime;
        TextView txtRecentMsg;

        public TeacherChatQueueAdapterViewholder( View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtRecentMsg = itemView.findViewById(R.id.txtRecentMsg);
            txttime = itemView.findViewById(R.id.txttime);
            itemView.setOnClickListener(new View.OnClickListener() {
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
}
