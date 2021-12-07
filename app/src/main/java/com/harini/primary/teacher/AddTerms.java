package com.harini.primary.teacher;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


@SuppressWarnings("deprecation")
public class AddTerms extends AppCompatActivity {

    private String Firstname, StdName;
    String Id,Value1,Value2,Value3;
    Button b1,b2;
    LineChart barChart;
    LineData barData;
    LineDataSet barDataSet;
    ArrayList barEnt;
    FirebaseFirestore db;
    float L1,L2,L3;
    ListenerRegistration listenerRegistrationChart;
    DocumentReference docRefChart;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_terms_tecacherside);



        StdName = getIntent().getStringExtra("StudentName");

        Id   =getIntent().getStringExtra("RegID");

        if(Id == null)
        {
            Id ="Empty";

        }






        L1=0.0f;
        L2=0.0f;
        L3=0.0f;





        TextView StudnetsName = (TextView)findViewById(R.id.StudntsName);
        StudnetsName.setText("Student Name :"+StdName);

        EditText Sem1=(EditText) findViewById(R.id.firstsem);


        EditText Sem2=(EditText)findViewById(R.id.secndsem);
         ;

        EditText Sem3=(EditText)findViewById(R.id.thriedrdSem);


        Value1="0";Value2="0";Value3="0";





        b1 =(Button) findViewById(R.id.btnsubmit);

        b2 =(Button) findViewById(R.id.linechart);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText Sem1=(EditText) findViewById(R.id.firstsem);
                Value1=Sem1.getText().toString();

                EditText Sem2=(EditText)findViewById(R.id.secndsem);
                Value2 =Sem2.getText().toString();

                EditText Sem3=(EditText)findViewById(R.id.thriedrdSem);
                Value3 =Sem3.getText().toString();

                if( TextUtils.isEmpty(Value1) && TextUtils.isEmpty(Value2) && TextUtils.isEmpty(Value3) ){


                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                }

                if(TextUtils.isEmpty(Value1) ||  TextUtils.isEmpty(Value2) ||  TextUtils.isEmpty(Value3))
                {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                }

                else {

                    Toast.makeText(getApplicationContext(), "ADD DATA", Toast.LENGTH_SHORT).show();

                    Update();

                }





            }
        });

        docRefChart = FirebaseFirestore.getInstance().collection("SemesterResult").document("C"+Id);

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
                            .collection("SemesterResult").document("C"+Id).set(map);


                }







            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                getEntryData(L1,L2,L3);

                barChart = findViewById(R.id.chartTearms);


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
        barEnt.add(new BarEntry(0f, 0,"seme"));
        barEnt.add(new BarEntry(1f, l1,"seme"));
        barEnt.add(new BarEntry(2f, l2,"seme"));
        barEnt.add(new BarEntry(3f, l3,"seme"));


    }


    public void Update() {



        Map<String, String> map = new HashMap<>();
        map.put("teram1",Value1);
        map.put("teram2",Value2);
        map.put("teram3",Value3);




        Task<Void> docRef = FirebaseFirestore.getInstance()
                .collection("SemesterResult").document("C"+Id).set(map);

    }







}