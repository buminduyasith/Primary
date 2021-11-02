package com.harini.primary.admin;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.harini.primary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_Semester_Reslts extends AppCompatActivity {

    LineChart barChart;
    LineData barData;
    LineDataSet barDataSet;
    ArrayList barEnt;
    FirebaseFirestore db;
    float L1,L2,L3;
    ListenerRegistration listenerRegistrationChart;
    DocumentReference docRefChart;
    ProgressDialog progressDialog;
    Button b1,b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term_reslts);


        SharedPreferences prf = getSharedPreferences("Parent_DATA", MODE_PRIVATE);

        String RGID = prf.getString("REGISTER_ID", null);

        String FIXID ="C"+RGID;

        L1=0.0f;
        L2=0.0f;
        L3=0.0f;

        b2 =(Button) findViewById(R.id.linechart2);


        docRefChart = FirebaseFirestore.getInstance().collection("SemesterResult").document(FIXID);

        listenerRegistrationChart = docRefChart.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists()){

                    if (error != null){
                        L1=0.0F;
                        L2=0.0F;
                        L3=0.0F;
                    }
                    else {

                        String teram1 =value.getString("teram1").trim();
                        String teram2 =value.getString("teram2").trim();
                        String teram3 =value.getString("teram3").trim();




                        if(teram1 == null && teram1 == ""){
                            L1=0.0f;
                        }

                        if(teram2 == null && teram2 == ""){
                            L2=0.0f;
                        }
                        if(teram2 == null && teram3 == ""){
                            L2=0.0f;
                        }


                        L1 = Float.parseFloat(teram1);
                        L2 = Float.parseFloat(teram2);
                        L3 = Float.parseFloat(teram3);



                    }

                }else {

                    Map<String, String> map = new HashMap<>();
                    map.put("teram1","0");
                    map.put("teram2","0");
                    map.put("teram3","0");

                    Task<Void> docRef = FirebaseFirestore.getInstance()
                            .collection("SemesterResult").document(FIXID).set(map);


                }







            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                getEntryData(L1,L2,L3);

                barChart = findViewById(R.id.chartTearms2);

                barDataSet = new LineDataSet(barEnt,"Data Set");
                barData = new LineData(barDataSet);
                barChart.setData(barData);

                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
                barDataSet.setLineWidth(16f);

            }
        });





    }



    private  void getEntryData(float l1,float l2,float l3) {

        barEnt = new ArrayList<>();
        barEnt.add(new BarEntry(0f, 0));
        barEnt.add(new BarEntry(1f, l1));
        barEnt.add(new BarEntry(2f, l2));
        barEnt.add(new BarEntry(3f, l3));


    }

}