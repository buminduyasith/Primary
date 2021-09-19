package com.harini.primary.admin;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.harini.primary.R;
import com.harini.primary.utill.PasswordGenerator;
import com.harini.primary.utill.UserRoles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CreateTeacherAccounts extends AppCompatActivity {

    private TextInputLayout txtinput_fname, txtinput_lname, txtinput_phone, txtinput_email, txtinput_pwd;

    private Button btn_signup;

    private Spinner spinner_grade,spinner_school;

    private ProgressBar progressbar_teacherSignup;

    private final static String TAG = "createTeacherAccounts";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public static final Pattern email_pattern = Pattern.compile("^\\w*[@+][A-za-z]+\\.+com$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teacher_accounts);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setupUI();
        GenerateSecurePassword();
      //  getSchools();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


    }

    private void setupUI() {

        txtinput_fname = findViewById(R.id.txtinput_teacher_fname);
        txtinput_lname = findViewById(R.id.txtinput_teacher_lname);
        txtinput_phone = findViewById(R.id.txtinput_phonenumber);
        txtinput_email = findViewById(R.id.txtinput_email);
        txtinput_pwd = findViewById(R.id.txtinput_password);

        spinner_grade = findViewById(R.id.spinner_grade);
       // spinner_school = findViewById(R.id.spinner_school);

        progressbar_teacherSignup = findViewById(R.id.progressbar_teacherSignup);

        btn_signup = findViewById(R.id.btn_teacherreg_signup);

    }

    private void GenerateSecurePassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        String password = passwordGenerator.generate(6);
        Log.d(TAG, "GenerateSecurePassword: " + password);

        txtinput_pwd.getEditText().setText(password);


    }

    private void signup() {

        progressbar_teacherSignup.setVisibility(View.VISIBLE);

        String email = txtinput_email.getEditText().getText().toString();
        String pwd = txtinput_pwd.getEditText().getText().toString();
        String fname = txtinput_fname.getEditText().getText().toString();
        String lname = txtinput_lname.getEditText().getText().toString();
        String phonenumber = txtinput_phone.getEditText().getText().toString();
        String grade = spinner_grade.getSelectedItem().toString();


        boolean result = validations(email,fname,lname,phonenumber,grade);
        if (result==false){
            progressbar_teacherSignup.setVisibility(View.GONE);
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                            builder.setDisplayName(fname);

                            UserProfileChangeRequest profileUpdates = builder.build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                Map<String, Object> userdata = new HashMap<>();
                                                userdata.put("firstName", fname);
                                                userdata.put("lastName", lname);
                                                userdata.put("phoneNumber", phonenumber);
                                                userdata.put("grade", grade);

                                                UserRoles userole = new UserRoles("TEACHER",user.getUid());
                                                userole.setUserRole();

                                                db.collection("teachers")
                                                       .document(user.getUid())
                                                        .set(userdata)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                mAuth.signOut();
                                                                progressbar_teacherSignup.setVisibility(View.GONE);
                                                                Toast.makeText(getApplicationContext(),"Your account has been successfully created",Toast.LENGTH_SHORT).show();
                                                                Log.d(TAG, "onComplete: dp updated ");
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressbar_teacherSignup.setVisibility(View.GONE);
                                                        Log.d(TAG, "onComplete: dp fail " + e.getLocalizedMessage());
                                                    }
                                                });

//
//
                                            } else {

                                                progressbar_teacherSignup.setVisibility(View.GONE);
                                                Log.d(TAG, "onComplete: update dp name " + task.getException().getLocalizedMessage());
                                            }
                                        }
                                    });



                        } else {

                            progressbar_teacherSignup.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {
                Log.e(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });


    }

    private void getSchools(){


        progressbar_teacherSignup.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();


        List<String> schoollist = new ArrayList<>();

        ArrayAdapter<String> schoolAdapteer = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                schoollist

        );

        spinner_school.setAdapter(schoolAdapteer);


        db.collection("schools")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot snapshot:task.getResult()){


                                progressbar_teacherSignup.setVisibility(View.GONE);

                                schoollist.add(snapshot.getId());


                            }


                            schoolAdapteer.notifyDataSetChanged();



                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });

    }


    private boolean validations(String email, String fname,String lname ,String pnum,String grade){

        if(email.isEmpty() || fname.isEmpty() || lname.isEmpty() || pnum.isEmpty() ||grade.isEmpty()){

            Toast.makeText(getApplicationContext(),"a required field is missing",Toast.LENGTH_SHORT).show();
            return false;


        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(),"email is badly formatted",Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;

    }

}