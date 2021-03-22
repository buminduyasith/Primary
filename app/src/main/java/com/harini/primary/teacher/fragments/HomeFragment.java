package com.harini.primary.teacher.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harini.primary.R;
import com.harini.primary.Signin;
import com.harini.primary.teacher.AdHomeWork;
import com.harini.primary.teacher.AddVideoLessons;
import com.harini.primary.teacher.CreateAnnouncement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment implements View.OnClickListener {


   // onCreateView onViewCreated
   private CardView card_ImgMakeAnnouncement,card_addVideoLessons,card_addhomeworks;

    private TextView displayname,day,month;
    private ImageView dpandlogout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher_dashboard_home,container,false);

        setupUI(view);
        setupActions();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        init();



        return view;


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void setupUI(View view){

        displayname = view.findViewById(R.id.displayname);
        day = view.findViewById(R.id.day);
        month = view.findViewById(R.id.month);


        dpandlogout = view.findViewById(R.id.dpandlogout);

        card_ImgMakeAnnouncement = view.findViewById(R.id.card_ImgMakeAnnouncement);
        card_addVideoLessons = view.findViewById(R.id.card_addVideoLessons);
        card_addhomeworks = view.findViewById(R.id.card_addhomeworks);

    }


    private void setupActions(){

        dpandlogout.setOnClickListener(this);
        card_ImgMakeAnnouncement.setOnClickListener(this);
        card_addVideoLessons.setOnClickListener(this);
        card_addhomeworks.setOnClickListener(this);
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

            case R.id.card_ImgMakeAnnouncement:
                Intent CreateAnnouncementIntent = new Intent(getActivity(), CreateAnnouncement.class);
                startActivity(CreateAnnouncementIntent);
                break;
            case R.id.card_addVideoLessons:
                Intent VideoLessonsIntent = new Intent(getActivity(), AddVideoLessons.class);
                startActivity(VideoLessonsIntent);
                break;
            case R.id.card_addhomeworks:
                Intent AdHomeWorkIntent = new Intent(getActivity(), AdHomeWork.class);
                startActivity(AdHomeWorkIntent);
                break;

            default:
                return;

        }
    }


    private void signout(){

        FirebaseUser currentUser = mAuth.getCurrentUser();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());



        builder
                .setTitle("Logout ?")
                .setMessage("Are you sure, you want to logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(currentUser != null){

                            //mAuth.getInstance().signOut();
                            mAuth.signOut();

                            SharedPreferences prf = getActivity().getSharedPreferences("TEACHERS_DATA", Context.MODE_PRIVATE);

                            prf.edit().clear().apply();

                            Intent mainpage = new Intent(getContext(), Signin.class);
                            startActivity(mainpage);
                            getActivity().getFragmentManager().popBackStack();



                        }
                    }
                })
                .setNegativeButton("Cancel",null);


        final AlertDialog dialog = builder.create();

        dialog.show();

    }
}
