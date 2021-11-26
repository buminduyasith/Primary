package com.harini.primary.admin;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.datatransport.Event;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.R;
import com.harini.primary.adapters.PerantsAdapter;
import com.harini.primary.models.ParentsList;

import java.util.ArrayList;

public class Students_list extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<ParentsList> list;
    String DocID;
    PerantsAdapter perantsAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fectching ");
        progressDialog.show();


        recyclerView =findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db =FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        perantsAdapter = new PerantsAdapter(Students_list.this,list);



        recyclerView.setAdapter(perantsAdapter);

        EventChangeList();



    }

    public  void EventChangeList ()
    {


        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

        String stdgrade = prf.getString("GRADE", null);


        CollectionReference collectionReference = db.collection("Parents");

        collectionReference.whereEqualTo("grade",stdgrade).orderBy("firstName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if (error != null){

                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Not Working",error.getMessage());
                }

                for (DocumentChange dc  : value.getDocumentChanges()){



                    if(dc.getType() == DocumentChange.Type.ADDED){


                        list.add(dc.getDocument().toObject(ParentsList.class));




                    }

                    if(progressDialog.isShowing())
                        progressDialog.dismiss();


                    perantsAdapter.notifyDataSetChanged();
                }


            }


        });

    }


}