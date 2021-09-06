package com.harini.primary.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.harini.primary.models.Message;
import com.harini.primary.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MessageAdapter extends FirestoreRecyclerAdapter<Message,MessageAdapter.MessageAdapterViewholder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;
    private onItemClickListner listner;
    private String role;
    private String TAG="msgadapter";
    FirestoreRecyclerOptions<Message> optionsi;

    public MessageAdapter( FirestoreRecyclerOptions<Message> options) {
        super(options);
        optionsi = options;


//        this.role = role;
    }

    @Override
    protected void onBindViewHolder( MessageAdapter.MessageAdapterViewholder holder, int position,  Message model) {
        Log.d(TAG, "onBindViewHolder: start");
        Timestamp ts=model.getTimestamp();
        Date date=ts.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat(" hh:mm a");
        String time =sdf.format(date);


        holder.txtmsg.setText(model.getMsg());
        holder.txtTime.setText(time);
        role = model.getSenderRole();
       // holder.cardview_single_chat_view.setForegroundGravity();

    }


    @Override
    public MessageAdapterViewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: "+viewType);
        if (viewType==1) {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
//            return new MessageAdapter.ViewHolder(view);
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_msg_view_out,parent,false);


            return new MessageAdapter.MessageAdapterViewholder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_msg_view_in,parent,false);


            return new MessageAdapter.MessageAdapterViewholder(v);
        }

//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_msg_view_out,parent,false);
//
//
//        return new MessageAdapter.MessageAdapterViewholder(v);
    }

    @Override
    public int getItemViewType(int position) {

//        return super.getItemViewType(position);
        Message message = optionsi.getSnapshots().get(position);
        if(message.getSenderRole().contains("TEACHER")){
            return 0;
        }else{
            return 1;
        }
    }

    public class MessageAdapterViewholder extends RecyclerView.ViewHolder {
        TextView txtmsg;
        TextView txtTime;
        CardView cardview_single_chat_view;

        public MessageAdapterViewholder( View itemView) {
            super(itemView);
            txtmsg = itemView.findViewById(R.id.txtmsg);
            txtTime = itemView.findViewById(R.id.txtTime);
            cardview_single_chat_view = itemView.findViewById(R.id.cardview_single_chat_view);
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
}
