package com.harini.primary.admin;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.R;
import com.harini.primary.adapters.AgendaCalenderAdapter;
import com.harini.primary.models.CustomeEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class AddEvents extends AppCompatActivity {

    private FloatingActionButton Fab_addnewevent;
    private RecyclerView rec_event_list;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    private AgendaCalenderAdapter adapter;

    private LinearLayout events_nodata_container;

    private static final String TAG="events_ac";

    private Query query;

    private String eventDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        setupUi();



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("Events");

        setupRecycleView();


        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .addEvents(new CalendarEventsPredicate() {
                    Random rnd = new Random();
                    @Override
                    public List<CalendarEvent> events(Calendar date) {

                        List<CalendarEvent> events = new ArrayList<>();
                        int count = rnd.nextInt(6);

                      /*  for (int i = 0; i <= count; i++){
                            events.add(new CalendarEvent(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)), "event"+i));
                        }

                        return events;*/
                        return null;

                    }
                })
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                Log.d(TAG, "onDateSelected: "+date);


                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                String formatdate =sdf.format(date.getTime());

                String selectedData =date.get(Calendar.YEAR)+"-"+date.get(Calendar.WEEK_OF_MONTH)+"-"+date.get(Calendar.DAY_OF_MONTH);


                getdatawhendatachange(formatdate);


                Log.d(TAG, "onDateSelected: pos"+selectedData);
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });


        Fab_addnewevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addEvent();
            }
        });

    }

    private void setupUi() {

        rec_event_list = findViewById(R.id.rec_event_list);
        Fab_addnewevent = findViewById(R.id.Fab_addnewevent);
        events_nodata_container = findViewById(R.id.events_nodata_container);

    }


    private void setupRecycleView() {

        // FirebaseUser firebaseUser = mAuth.getCurrentUser();

        /*SharedPreferences prf = getSharedPreferences("Parent_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);

        if(grade==null){
            throw new RuntimeException("grade should not be null");
        }

        Log.d(TAG, "setupRecycleView: grade"+grade);*/

        String  grade ="5A";

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String formatdate =sdf.format(calendar.getTime());

        Log.d(TAG, "setupRecycleView: format date recview"+formatdate);


        query = collectionReference.whereEqualTo("date",formatdate).orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<CustomeEvent> options = new FirestoreRecyclerOptions.Builder<CustomeEvent>()
                .setQuery(query, CustomeEvent.class).build();


        adapter = new AgendaCalenderAdapter(options);


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {



                if (value != null) {


                    if (value.size() > 0) {


                        events_nodata_container.setVisibility(View.GONE);
                        rec_event_list.setVisibility(View.VISIBLE);




                    } else {

                        rec_event_list.setVisibility(View.GONE);
                        events_nodata_container.setVisibility(View.VISIBLE);

                    }

                }


            }
        });


        rec_event_list.setHasFixedSize(true);

        rec_event_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rec_event_list.setAdapter(adapter);




    }

    private void getdatawhendatachange(String formatdate){

        adapter.stopListening();
        query = collectionReference.whereEqualTo("date",formatdate).orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<CustomeEvent> options = new FirestoreRecyclerOptions.Builder<CustomeEvent>()
                .setQuery(query, CustomeEvent.class).build();


        adapter = new AgendaCalenderAdapter(options);

        rec_event_list.setAdapter(adapter);

        adapter.startListening();

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {



                if (value != null) {


                    if (value.size() > 0) {


                        events_nodata_container.setVisibility(View.GONE);
                        rec_event_list.setVisibility(View.VISIBLE);




                    } else {

                        rec_event_list.setVisibility(View.GONE);
                        events_nodata_container.setVisibility(View.VISIBLE);

                    }

                }


            }
        });


    }

    private void addEvent(){


        AlertDialog.Builder builder = new AlertDialog.Builder(AddEvents.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_create_add_event, null);

        final TextInputLayout TIL_event_title = view.findViewById(R.id.TIL_event_title);
        final TextInputLayout TIL_grade = view.findViewById(R.id.TIL_grade);
        Button TIL_event_date = view.findViewById(R.id.TIL_event_date);


        builder.setView(view)
                .setTitle("Add an Event")
                .setPositiveButton("done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        final DialogInterface d = dialog;

                        String discription = TIL_event_title.getEditText().getText().toString();
                        String grade = TIL_grade.getEditText().getText().toString();


                        if(discription.isEmpty() || grade.isEmpty() || eventDate.isEmpty()){
                            Toast.makeText(AddEvents.this,"required fields are empty",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ////************************************************************


                        Timestamp timestamp = new Timestamp(new Date());


                        Map<String, Object> event = new HashMap<>();
                        event.put("discription", discription);
                        event.put("date", eventDate);
                        event.put("timestamp", timestamp);
                        event.put("grade", Integer.valueOf(grade));

                        db.collection("Events").add(event)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "onSuccess: ");

                                        Toast.makeText(AddEvents.this,"Event added successfully",Toast.LENGTH_SHORT).show();
                                        d.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG, "onFailure: ");

                                d.dismiss();
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });







                        ////************************************************************


                    }
                })

                .setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();

        dialog.show();


        TIL_event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: clicked til_event_date");
                showDatedialog(TIL_event_date);
            }
        });



    }

    private void showDatedialog( Button btnselectdate){

        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {


                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                String formatdate =sdf.format(calendar.getTime());


                eventDate = formatdate;
                btnselectdate.setText("selected date "+formatdate);
            }
        };

        new DatePickerDialog(AddEvents.this, dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.WEEK_OF_MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }
}