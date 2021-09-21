package com.harini.primary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.harini.primary.models.Parent;
import com.harini.primary.R;


public class StudentListAdapter extends FirestoreRecyclerAdapter<Parent,StudentListAdapter.StudentListAdapterViewholder> {

    private onItemClickListner listner;

    public StudentListAdapter( FirestoreRecyclerOptions<Parent> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(StudentListAdapter.StudentListAdapterViewholder holder, int position, Parent model) {



//        holder.txtname.setText(model.getFirstName()+" "+model.getLastName());
          holder.txtname.setText(model.getStudentName());

    }


    @Override
    public StudentListAdapter.StudentListAdapterViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_list_chat_select,parent,false);

        return new StudentListAdapter.StudentListAdapterViewholder(v);
    }

    public class StudentListAdapterViewholder extends RecyclerView.ViewHolder {

        TextView txtname;


        public StudentListAdapterViewholder( View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtname);

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
