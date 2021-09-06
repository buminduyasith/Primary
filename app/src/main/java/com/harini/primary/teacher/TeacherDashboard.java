package com.harini.primary.teacher;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.harini.primary.R;
import com.harini.primary.teacher.fragments.ChatFragment;
import com.harini.primary.teacher.fragments.HomeFragment;

public class TeacherDashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FrameLayout fragment_container;
    private BottomNavigationView bottomMainNAV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        setupUI();

        bottomMainNAV.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();




    }


    private void setupUI(){

        fragment_container = findViewById(R.id.fragment_container);
        bottomMainNAV = findViewById(R.id.bottomMainNAV);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedfragment = null;

        switch (item.getItemId()) {
            case R.id.menu_home:
                selectedfragment = new HomeFragment();
                break;

            case R.id.menu_chat:
                selectedfragment = new ChatFragment();
                break;



        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedfragment).commit();


        return true;
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }
}