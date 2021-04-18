package com.harini.primary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login,btn_signup_admin,btn_signup_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        setupUi();
        setActions();
    }

    private void setupUi(){
        btn_login =findViewById(R.id.btn_loginlink);
        btn_signup_admin =findViewById(R.id.btn_signup_adminlink);
        btn_signup_parent =findViewById(R.id.btn_signup_parentlink);

    }

    private void setActions(){
        btn_login.setOnClickListener(this);
        btn_signup_admin.setOnClickListener(this);
        btn_signup_parent.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_loginlink:
                login();
                break;
            case R.id.btn_signup_parentlink:
                signupasParent();
                break;
            case R.id.btn_signup_adminlink:
                signupasAdmin();
            default:
                return;


        }
    }

    private void login(){

        Intent newIntent = new Intent(getApplicationContext(),Signin.class);
        startActivity(newIntent);
    }

    private void signupasAdmin(){

        Intent newIntent = new Intent(getApplicationContext(),AdminSignup.class);
        startActivity(newIntent);
    }

    private void signupasParent(){

        Intent newIntent = new Intent(getApplicationContext(),ParentSignup.class);
        startActivity(newIntent);
    }
}