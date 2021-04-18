package com.harini.primary.parent.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.harini.primary.parent.AgendaCalendarView;
import com.harini.primary.parent.ViewHomeWork;
import com.harini.primary.parent.ViewVideoLessons;
import com.harini.primary.teacher.CreateAnnouncement;
import com.harini.primary.utill.LocaleHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private TextView displayname,day,month;
    private ImageView dpandlogout;
    private CardView card_viewHomeworks,card_viewVideoLessons,card_viewevents;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_parent_dashboard_home,container,false);

        setupUI(view);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        init();

        setActions();

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

        card_viewHomeworks = view.findViewById(R.id.card_viewHomeworks);
        card_viewVideoLessons = view.findViewById(R.id.card_viewVideoLessons);
        card_viewevents = view.findViewById(R.id.card_viewevents);
//
//        Context context =   LocaleHelper.setLocale(getContext(), "si");
//
//        Resources resource = context.getResources();
//
//        TextView txthome = view.findViewById(R.id.txthomework); // setText(resource.getString(R.string.homeworkActivtiyTitle));
//
//        txthome.setText();
    }

    private void setActions(){

        dpandlogout.setOnClickListener(this);
        card_viewHomeworks.setOnClickListener(this);
        card_viewVideoLessons.setOnClickListener(this);
        card_viewevents.setOnClickListener(this);
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

            case R.id.card_viewHomeworks:
                Intent ViewHomeWorkIntent = new Intent(getActivity(), ViewHomeWork.class);
                startActivity(ViewHomeWorkIntent);
                break;

            case R.id.card_viewVideoLessons:
                Intent ViewVideoLessonsIntent = new Intent(getActivity(), ViewVideoLessons.class);
                startActivity(ViewVideoLessonsIntent);
                break;

            case R.id.card_viewevents:

                Intent ViewEventsLessonsIntent = new Intent(getActivity(), AgendaCalendarView.class);
                startActivity(ViewEventsLessonsIntent);
                break;

            case R.id.dpandlogout:

                signout();

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

                            mAuth.getInstance().signOut();

                            SharedPreferences prf = getActivity().getSharedPreferences("Parent_DATA", Context.MODE_PRIVATE);

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
