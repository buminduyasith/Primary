package com.harini.primary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harini.primary.utill.UserRoles;

import java.util.HashMap;
import java.util.Map;

import static com.harini.primary.utill.Utill.hideSoftKeyboard;

public class AdminSignup extends AppCompatActivity {

    private static final String TAG ="adminsignup" ;
    private TextInputLayout txt_fname, txt_lname,txt_schoolname,txt_email,txt_password;

    private Button btn_adminreg_signup;

    private ProgressBar signupProgressbar;

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        setupUI(findViewById(R.id.admin_regform));

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btn_adminreg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    private void setupUI(View view){

        txt_fname = findViewById(R.id.txtinput_admin_fname);
        txt_lname = findViewById(R.id.txtinput_admin_lname);
        txt_schoolname = findViewById(R.id.txtinput_sclname);
        txt_email = findViewById(R.id.txtinput_email);
        txt_password = findViewById(R.id.txtinput_password);


        signupProgressbar = findViewById(R.id.signupProgressbar);

        btn_adminreg_signup = findViewById(R.id.btn_adminreg_signup);

        if (!(view instanceof TextInputEditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                private View v;
                private MotionEvent event;

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(AdminSignup.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }

    }



    private void signup() {

        signupProgressbar.setVisibility(View.VISIBLE);



        String email = txt_email.getEditText().getText().toString();
        String password = txt_password.getEditText().getText().toString();
        String Fname = txt_fname.getEditText().getText().toString();
        String Lname = txt_lname.getEditText().getText().toString();
        String schoolname = txt_schoolname.getEditText().getText().toString();



        boolean result = validations(email,password,Fname,Lname,schoolname);
        if (result==false){
            signupProgressbar.setVisibility(View.GONE);
            return;
        }

        //validation
        //password should have at least chars
        Log.d(TAG, "signup: "+email+" "+password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                       // Log.d(TAG, "onComplete: " + task.getResult());


                        if (task.isSuccessful()) {

                            Log.d(TAG, "onComplete: muath success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                            builder.setDisplayName(Fname);

                            UserProfileChangeRequest profileUpdates = builder.build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Map<String, Object> userdata = new HashMap<>();
                                                userdata.put("firstName", Fname);
                                                userdata.put("lastName", Lname);
                                                userdata.put("school", schoolname);

                                                UserRoles userole = new UserRoles("ADMIN",user.getUid());
                                                userole.setUserRole();

                                                db.collection("admin")
                                                        .document(user.getUid())
                                                        .set(userdata)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                mAuth.signOut();
                                                                Toast.makeText(getApplicationContext(),"Your account has been successfully created",Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(getApplicationContext(),Signin.class);
                                                                signupProgressbar.setVisibility(View.GONE);
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                mAuth.signOut();
                                                                signupProgressbar.setVisibility(View.GONE);
                                                                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
                                                            }
                                                        });

                                            } else {
                                                signupProgressbar.setVisibility(View.GONE);
                                                Log.d(TAG, "onComplete: user profile display name update fail");
                                            }
                                        }
                                    });



                        } else {

                            Log.d(TAG, "onComplete: account fail");
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            signupProgressbar.setVisibility(View.GONE);

                        }
                    }
                });


    }

    private boolean validations(String email,String pwd, String fname,String lname ,String sclname){

        if(email.isEmpty() || pwd.isEmpty() || fname.isEmpty() || lname.isEmpty() || sclname.isEmpty()){

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