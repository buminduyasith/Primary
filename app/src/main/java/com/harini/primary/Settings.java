package com.harini.primary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    private Switch switch_lang_sinhala;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupUI();
        setupActions();
        initSetup();


    }

    private void setupUI() {

        switch_lang_sinhala = findViewById(R.id.switch_lang_sinhala);

    }

    private void initSetup(){
        SharedPreferences prf = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean langStatus = prf.getBoolean("lan_sinhala", false);
        switch_lang_sinhala.setChecked(langStatus);

    }

    private void setupActions(){

        switch_lang_sinhala.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.switch_lang_sinhala:

                if (switch_lang_sinhala.isChecked()) {
                    //Toast.makeText(getApplicationContext(), "run true", Toast.LENGTH_LONG).show();
                    SharedPreferences prf = getSharedPreferences("Settings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prf.edit();
                    editor.putBoolean("lan_sinhala",switch_lang_sinhala.isChecked());
                    editor.commit();
                    Intent splash = new Intent(getApplicationContext(), SplashScreen.class);
                    startActivity(splash);

                } else {
                    //Toast.makeText(getApplicationContext(), "run false", Toast.LENGTH_LONG).show();
                    SharedPreferences prf = getSharedPreferences("Settings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prf.edit();
                    editor.putBoolean("lan_sinhala",switch_lang_sinhala.isChecked());
                    editor.commit();
                    Intent splash = new Intent(getApplicationContext(), SplashScreen.class);
                    startActivity(splash);
                }

                break;


            default:
                return;
        }
    }
}