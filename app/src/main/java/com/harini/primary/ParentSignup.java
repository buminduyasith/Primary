package com.harini.primary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.utill.UserRoles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentSignup extends AppCompatActivity {


    private TextInputLayout txtinput_stdname, txtinput_parent_fname, txtinput_parent_lname, txtinput_email, txtinput_password, txtinput_regid, txtinput_phonenum;

    private Button btnSignup;

    private Spinner spinner_grade;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    private final static String TAG = "parentsignup_test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_signup);

        setupUI();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getSchools();


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


    }

    private void setupUI() {

        txtinput_stdname = findViewById(R.id.txtinput_studentName);
        txtinput_parent_fname = findViewById(R.id.txtinput_parent_fname);
        txtinput_parent_lname = findViewById(R.id.txtinput_parent_lname);
        txtinput_email = findViewById(R.id.txtinput_email);
        txtinput_password = findViewById(R.id.txtinput_password);
        txtinput_regid = findViewById(R.id.txtinput_registerid);
        txtinput_phonenum = findViewById(R.id.txtinput_phonenumber);
        btnSignup = findViewById(R.id.btn_parentreg_signup);

        spinner_grade = findViewById(R.id.spinner_grade);

        progressBar = findViewById(R.id.Progress_ParentRegister);
    }

    private void signup() {

        progressBar.setVisibility(View.VISIBLE);


        String email = txtinput_email.getEditText().getText().toString();
        String password = txtinput_password.getEditText().getText().toString();
        String parentFname = txtinput_parent_fname.getEditText().getText().toString();
        String parentLname = txtinput_parent_lname.getEditText().getText().toString();
        String stdname = txtinput_stdname.getEditText().getText().toString();

        String regid = txtinput_regid.getEditText().getText().toString();
        String phonenum = txtinput_phonenum.getEditText().getText().toString();
        String grade = spinner_grade.getSelectedItem().toString();


        //validation
        //password should have at least chars

        boolean result = validations(email,password,parentFname,parentLname,stdname,regid,phonenum);
        if (result==false){
            progressBar.setVisibility(View.GONE);
            return;
        }




        if (email.isEmpty()) {
            progressBar.setVisibility(View.GONE);

            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                       // Log.d(TAG, "onComplete: " + task.getResult());

                        if (task.isSuccessful()) {

                            Log.d(TAG, "onComplete: muath success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                            builder.setDisplayName(parentFname);

                            UserProfileChangeRequest profileUpdates = builder.build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Map<String, Object> userdata = new HashMap<>();
                                                userdata.put("firstName", parentFname);
                                                userdata.put("lastName", parentLname);
                                                userdata.put("studentName", stdname);
                                                userdata.put("registerID", regid);
                                                userdata.put("phoneNumber", phonenum);
                                                userdata.put("grade", grade);

                                                UserRoles userole = new UserRoles("PARENT",user.getUid());
                                                userole.setUserRole();

                                                db.collection("Parents")
                                                        .document(user.getUid())
                                                        .set(userdata)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                mAuth.signOut();
                                                                Toast.makeText(getApplicationContext(),"Your account has been successfully created",Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(getApplicationContext(),Signin.class);
                                                                progressBar.setVisibility(View.GONE);

                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                mAuth.signOut();
                                                                progressBar.setVisibility(View.GONE);

                                                                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
                                                            }
                                                        });




                                            } else {
                                                progressBar.setVisibility(View.GONE);

                                                Log.d(TAG, "onComplete: user profile display name update fail");
                                            }
                                        }
                                    });



                        } else {

                            Log.d(TAG, "onComplete: account fail");
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);


                        }
                    }
                });


    }

    private void getSchools() {


        progressBar.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();


        List<String> gradelist = new ArrayList<>();

        ArrayAdapter<String> gradeAdapteer = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                gradelist

        );

        spinner_grade.setAdapter(gradeAdapteer);


        db.collection("teachers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot snapshot : task.getResult()) {


                                progressBar.setVisibility(View.GONE);

                                gradelist.add(snapshot.getString("grade"));



                            }


                            gradeAdapteer.notifyDataSetChanged();


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });

    }

    private boolean validations(String email,String pwd, String fname,String lname ,String stdname,String regid,String phonenum){

        if(email.isEmpty() || pwd.isEmpty() || fname.isEmpty() || lname.isEmpty() || stdname.isEmpty() || regid.isEmpty() || phonenum.isEmpty()){

            Toast.makeText(getApplicationContext(),"a required field is missing",Toast.LENGTH_SHORT).show();
            return false;


        }

        else if(pwd.length()<6){
            Toast.makeText(getApplicationContext(),"Password should have more than 6 characters",Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;

    }


}