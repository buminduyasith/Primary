package com.harini.primary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harini.primary.R;
import com.harini.primary.models.ParentsList;
import com.harini.primary.teacher.AddTerms;

import java.util.ArrayList;

public class PerantsAdapter extends  RecyclerView.Adapter<PerantsAdapter.MyViewHolder> {

    Context context;

    ArrayList<ParentsList> list;
    String FullName;

    public PerantsAdapter(Context context, ArrayList< ParentsList> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.studtents,parent,false);
        return  new MyViewHolder(v);




    }

    @Override
    public void onBindViewHolder(@NonNull PerantsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ParentsList perantslist = list.get(position);


                FullName = perantslist.getFirstName()+"\b"+ perantslist.getLastName();
                holder.stdName.setText(perantslist.getStudentName());
                holder.perantsName.setText(FullName);




        holder. RecyclerV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(context, AddTerms.class);
                myIntent.putExtra("StudentName", perantslist.getStudentName()); //Optional parameters
                myIntent.putExtra("RegID",perantslist.getregisterID() );




                context.startActivity(myIntent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView   perantsName,stdName;

        View RecyclerV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            stdName = itemView.findViewById(R.id.studntsName);
            perantsName = itemView.findViewById(R.id.percentName);
            RecyclerV = itemView.findViewById(R.id.studntlist);

        }
    }

}
