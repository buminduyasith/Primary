package com.harini.primary.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harini.primary.R;
import com.harini.primary.Signin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdminDashboard extends AppCompatActivity implements View.OnClickListener {

    private TextView displayname, day, month;
    private ImageView dpandlogout;
    private CardView card_createteacherAcocunts, card_createEvents, card_addTimetables, card_viewTimetables,card_addexammarks;

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

    private void setupUI() {
        displayname = findViewById(R.id.displayname);
        day = findViewById(R.id.day);
        month = findViewById(R.id.month);

        dpandlogout = findViewById(R.id.dpandlogout);

        card_createteacherAcocunts = findViewById(R.id.card_createteacherAcocunts);
        card_createEvents = findViewById(R.id.card_createEvents);
        card_addTimetables = findViewById(R.id.card_addTimetables);
        card_viewTimetables = findViewById(R.id.card_viewTimetables);
        card_addexammarks = findViewById(R.id.card_addexammarks);
    }

    private void setActions() {
        dpandlogout.setOnClickListener(this);

        card_createteacherAcocunts.setOnClickListener(this);
        card_createEvents.setOnClickListener(this);
        card_addTimetables.setOnClickListener(this);
        card_viewTimetables.setOnClickListener(this);
        card_addexammarks.setOnClickListener(this);
    }

    private void init() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
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
        switch (view.getId()) {
            case R.id.dpandlogout:
                signout();
                break;

            case R.id.card_createteacherAcocunts:
                Intent CreateTeacherAccountsIntent = new Intent(getApplicationContext(), CreateTeacherAccounts.class);
                startActivity(CreateTeacherAccountsIntent);
                break;

            case R.id.card_createEvents:
                Intent AddEventsIntent = new Intent(getApplicationContext(), AddEvents.class);
                startActivity(AddEventsIntent);
                break;

            case R.id.card_addTimetables:
                Intent intentAddTimetable = new Intent(getApplicationContext(), AddTimeTable.class);
                startActivity(intentAddTimetable);
                break;

            case R.id.card_viewTimetables:
                Intent intentViewTimetable = new Intent(getApplicationContext(), ViewTimeTable.class);
                intentViewTimetable.putExtra("userRole","ADMIN");
                startActivity(intentViewTimetable);
                break;
            case R.id.card_addexammarks:
                Intent addexamintent= new Intent(getApplicationContext(), AddStudentMarks.class);
               // addexamintent.putExtra("userRole","ADMIN");
                startActivity(addexamintent);
                break;
        }
    }

    private void signout() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);

        builder.setTitle("Logout ?")
                .setMessage("Are you sure, you want to logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (currentUser != null) {
                            FirebaseAuth.getInstance().signOut();
                            Intent mainpage = new Intent(getApplicationContext(), Signin.class);
                            startActivity(mainpage);
                            finish();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}