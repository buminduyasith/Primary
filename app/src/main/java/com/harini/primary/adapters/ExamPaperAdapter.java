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
import com.harini.primary.models.ExamPaper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamPaperAdapter extends FirestoreRecyclerAdapter<ExamPaper,ExamPaperAdapter.ExamPaperViewHolder> {

    private onItemClickListner listner;
    private String role;
    public ExamPaperAdapter(@NonNull  FirestoreRecyclerOptions<ExamPaper> options,String role) {
        super(options);
        this.role =role;
    }

    @Override
    protected void onBindViewHolder(@NonNull  ExamPaperAdapter.ExamPaperViewHolder holder, int position,  ExamPaper model) {
        Timestamp ts=model.getTimestamp();
        Date date=ts.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        String time =sdf.format(date);


        holder.txt_paper.setText(model.getFilename());

        holder.txt_utime.setText(time);

        if(role.equals("teacher")){
            holder.img_btn_download.setVisibility(View.GONE);
        }


    }


    @Override
    public ExamPaperViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_view_exams_papers,parent,false);

        return new ExamPaperAdapter.ExamPaperViewHolder(v);
    }


    public class ExamPaperViewHolder extends RecyclerView.ViewHolder {

        TextView txt_paper,txt_utime;
        ImageButton img_btn_download;

        public ExamPaperViewHolder(@NonNull  View itemView) {
            super(itemView);

            txt_paper = itemView.findViewById(R.id.txt_paper);
            txt_utime = itemView.findViewById(R.id.txt_utime);
            img_btn_download = itemView.findViewById(R.id.img_btn_download);

            img_btn_download.setOnClickListener(new View.OnClickListener() {
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
