package com.harini.primary.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harini.primary.R;
import com.harini.primary.Signin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdminDashboard extends AppCompatActivity implements View.OnClickListener {

    private TextView displayname,day,month;
    private ImageView dpandlogout;
    private CardView card_createteacherAcocunts;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        setupUI();


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        init();

        setActions();
    }

    private void setupUI(){

        displayname = findViewById(R.id.displayname);
        day = findViewById(R.id.day);
        month = findViewById(R.id.month);


        dpandlogout = findViewById(R.id.dpandlogout);

        card_createteacherAcocunts = findViewById(R.id.card_createteacherAcocunts);

    }

    private void setActions(){

        dpandlogout.setOnClickListener(this);

        card_createteacherAcocunts.setOnClickListener(this);
    }

    private void init(){

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null){
            displayname.setText("Hi " + user.getDisplayName());
        }

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String monthof = new SimpleDateFormat("MMM").format(cal.getTime());
        String dateof = new SimpleDateFormat("dd ").format(new Date());
        month.setText(monthof.toUpperCase());
        day.setText(dateof);





    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.dpandlogout:

                signout();

                break;

            case R.id.card_createteacherAcocunts:
                Intent newIntent = new Intent(getApplicationContext(),CreateTeacherAccounts.class);
                startActivity(newIntent);
                break;
            default:
                return;

        }
    }


    private void signout(){

        FirebaseUser currentUser = mAuth.getCurrentUser();

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);



        builder
                .setTitle("Logout ?")
                .setMessage("Are you sure, you want to logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(currentUser != null){

                            mAuth.getInstance().signOut();
                            Intent mainpage = new Intent(getApplicationContext(), Signin.class);
                            startActivity(mainpage);
                            finish();



                        }
                    }
                })
                .setNegativeButton("Cancel",null);


        AlertDialog dialog = builder.create();

        dialog.show();

    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);


    }
}