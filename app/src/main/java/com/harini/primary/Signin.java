package com.harini.primary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harini.primary.Models.Parent;
import com.harini.primary.Models.Teacher;
import com.harini.primary.admin.AdminDashboard;
import com.harini.primary.parent.ParentDashboard;
import com.harini.primary.teacher.TeacherDashboard;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.harini.primary.utill.Utill.hideSoftKeyboard;

public class Signin extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG ="signindebug" ;
    private TextInputLayout txtinput_email,txtinput_password;
    private Button btnsignin;

    private TextView txtforgetpwd,txtregister;

    private ProgressBar logprogressBar;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private static final String TEACHER_ROLE = "TEACHER";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String PARENT_ROLE = "PARENT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        setupUI(findViewById(R.id.log_top_interface));
        setActions();





    }

    private void setupUI(View view) {

        txtinput_email = findViewById(R.id.txtinput_email);
        txtinput_password = findViewById(R.id.txtinput_password);
        btnsignin = findViewById(R.id.btnsignin);
        txtforgetpwd = findViewById(R.id.forgetpasswordlink);
        txtregister = findViewById(R.id.registerlink);
        logprogressBar = findViewById(R.id.logprogressBar);



        if (!(view instanceof TextInputEditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                private View v;
                private MotionEvent event;

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Signin.this);
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

    private void setActions(){
        btnsignin.setOnClickListener(this);
        txtforgetpwd.setOnClickListener(this);
        txtregister.setOnClickListener(this);
    }

    private void signin(){

        logprogressBar.setVisibility(View.VISIBLE);
        String email = txtinput_email.getEditText().getText().toString();
        String pwd = txtinput_password.getEditText().getText().toString();

        if(email.isEmpty() || pwd.isEmpty()){
            logprogressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"A required field is missing",Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser user = task.getResult().getUser();
                            getUserRole(user.getUid());

                        }
                        else{
                            logprogressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void getUserRole(String uid){

        db.collection("userRole")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String role = documentSnapshot.getString("role");

                        if(role.equals(TEACHER_ROLE)){
                            getclassforTeacher(uid);


                        }

                        else if(role.equals(PARENT_ROLE)){

                            getclassforStudent(uid);

                        }

                        else{
                            Intent newIntent = new Intent(getApplicationContext(), AdminDashboard.class);

                            logprogressBar.setVisibility(View.GONE);
                            startActivity(newIntent);
                            finish();
                        }





                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnsignin:

                signin();

                break;

            case R.id.forgetpasswordlink:

                forgetPassword();

                break;

            case R.id.registerlink:

                Intent newIntent = new Intent(getApplicationContext(),MainScreen.class);
                startActivity(newIntent);
                //signout();

                break;
            default:
                return;

        }

    }


    private void forgetPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.forgetpassword_dialog,null);

        final TextInputLayout emailforgetpassword= view.findViewById(R.id.forgetpassword_xml_textinput);

        builder.setView(view)
                .setTitle("Reset Password")
                .setPositiveButton("done", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String emailAddress = emailforgetpassword.getEditText().getText().toString();

                        if(emailAddress.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Email address is required",Toast.LENGTH_SHORT);
                            return;
                        }

                        auth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Log.d(TAG, "Email sent.");
                                            final SweetAlertDialog pDialog = new SweetAlertDialog(Signin.this, SweetAlertDialog.SUCCESS_TYPE);
                                            pDialog.setCancelable(false);
                                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                            pDialog.setTitleText("Reset Password");
                                            pDialog.setContentText("Please check your mails to change your password");
                                            pDialog.setConfirmButton("Done\n", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    pDialog.dismissWithAnimation();




                                                }
                                            }).show();
                                        }
                                        else{

                                            Toast.makeText(getApplicationContext(),"Something happend wrong please tray again later",Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });




                    }
                })
                .setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();

        dialog.show();
    }


    private void getclassforTeacher(String uid) {

        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);




        db.collection("teachers")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {

                        Teacher teacher = ds.toObject(Teacher.class);

                        if(grade==null){

                            SharedPreferences.Editor editor = prf.edit();

                            editor.putString("FIRST_NAME",teacher.getFirstName());

                            editor.putString("LAST_NAME",teacher.getLastName());

                            editor.putString("PHONE_NUMBER",teacher.getPhoneNumber());

                            editor.putString("GRADE",teacher.getGrade());

                            editor.commit();

                        }

                        Intent newIntent = new Intent(getApplicationContext(), TeacherDashboard.class);


                        logprogressBar.setVisibility(View.GONE);
                        startActivity(newIntent);

                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void getclassforStudent(String uid) {

        SharedPreferences prf = getSharedPreferences("Parent_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);




        db.collection("Parents")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {

                        Parent parent = ds.toObject(Parent.class);

                        if(grade==null){

                            SharedPreferences.Editor editor = prf.edit();

                            editor.putString("FIRST_NAME",parent.getFirstName());

                            editor.putString("LAST_NAME",parent.getLastName());

                            editor.putString("PHONE_NUMBER",parent.getPhoneNumber());

                            editor.putString("GRADE",parent.getGrade());

                            editor.putString("REGISTER_ID",parent.getRegisterID());

                            editor.putString("STUDENT_NAME",parent.getStudentName());




                            editor.commit();

                        }

                        Intent newIntent = new Intent(getApplicationContext(), ParentDashboard.class);

                        logprogressBar.setVisibility(View.GONE);
                        startActivity(newIntent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}