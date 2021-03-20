package com.harini.primary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harini.primary.Models.Teacher;
import com.harini.primary.admin.AdminDashboard;
import com.harini.primary.parent.ParentDashboard;
import com.harini.primary.teacher.TeacherDashboard;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "splashscreendebug";
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private static final String TEACHER_ROLE = "TEACHER";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String PARENT_ROLE = "PARENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


       /* Intent ParentSignupIntent = new Intent(this, AddVideoLessons.class);

        startActivity(ParentSignupIntent);*/
    }


    private boolean isFirstTime() {

        SharedPreferences prf = getSharedPreferences("appAllData", MODE_PRIVATE);


        boolean applicationStatus = prf.getBoolean("FIRST_TIME", false);
        Log.d(TAG, "isFirstTime: " + applicationStatus);
        SharedPreferences.Editor editor = prf.edit();

        if (applicationStatus == true) {

            editor.putBoolean("FIRST_TIME", false);

            editor.commit();

            return true;

        } else {

            return false;

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        // throw new RuntimeException("Test Crash");

       if (isFirstTime()) {

            Intent newIntent = new Intent(getApplicationContext(), MainScreen.class);

            startActivity(newIntent);

            finish();

        } else {


            FirebaseUser user = mAuth.getCurrentUser();

            if (user != null) {

                db.collection("userRole")
                        .document(user.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                String role = documentSnapshot.getString("role");

                                if(role.equals(TEACHER_ROLE)){

                                    getclass(user.getUid());


                                }

                                else if(role.equals(PARENT_ROLE)){
                                    Intent newIntent = new Intent(getApplicationContext(), ParentDashboard.class);

                                    startActivity(newIntent);
                                }

                                else{
                                    Intent newIntent = new Intent(getApplicationContext(), AdminDashboard.class);

                                    startActivity(newIntent);
                                }



                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getLocalizedMessage());


                    }
                });


            } else {

                Intent newIntent = new Intent(getApplicationContext(), Signin.class);

                startActivity(newIntent);

                finish();

            }


        }
    }

    private void getclass(String uid) {

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

                        startActivity(newIntent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


}