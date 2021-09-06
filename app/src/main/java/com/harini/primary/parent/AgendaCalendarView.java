package com.harini.primary.parent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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
import java.util.List;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class AgendaCalendarView extends AppCompatActivity {

    private RecyclerView rec_event_list;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    private AgendaCalenderAdapter adapter;

    private TextView txtdateshow;

    private LinearLayout events_nodata_container;

    private static final String TAG="events_ac";

    private HorizontalCalendar calendarView;
    private Button textButton_selmonth;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_calendar_view);





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

               //Log.d(TAG, "onDateSelected: "+date);


                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                String formatdate =sdf.format(date.getTime());

                String selectedData =date.get(Calendar.YEAR)+"-"+date.get(Calendar.WEEK_OF_MONTH)+"-"+date.get(Calendar.DAY_OF_MONTH);

                txtdateshow.setText(formatdate);

                getdatawhendatachange(formatdate,1);



                Log.d(TAG, "onDateSelected: pos"+formatdate);
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });






    }


    private void setupUi() {
        textButton_selmonth = findViewById(R.id.textButton_selmonth);
        rec_event_list = findViewById(R.id.rec_event_list);
        txtdateshow = findViewById(R.id.txtdateshow);
        events_nodata_container = findViewById(R.id.events_nodata_container);

    }


    private void setupRecycleView() {

        // FirebaseUser firebaseUser = mAuth.getCurrentUser();

        SharedPreferences prf = getSharedPreferences("Parent_DATA", MODE_PRIVATE);

        String stdclass = prf.getString("GRADE", null);

        if(stdclass==null){
            throw new RuntimeException("grade should not be null");
        }

        char stdgrade = stdclass.charAt(0);

        Log.d(TAG, "setupRecycleView: grade"+stdgrade);


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String formatdate =sdf.format(calendar.getTime());

        Log.d(TAG, "setupRecycleView: format date recview"+formatdate);


        query = collectionReference.whereEqualTo("grade", Integer.valueOf(String.valueOf(stdgrade))).whereEqualTo("date",formatdate).orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<CustomeEvent> options = new FirestoreRecyclerOptions.Builder<CustomeEvent>()
                .setQuery(query, CustomeEvent.class).build();


        adapter = new AgendaCalenderAdapter(options);


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                Log.d(TAG, "onEvent: value size "+value.size());

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

    private void getdatawhendatachange(String formatdate,int grade){

        SharedPreferences prf = getSharedPreferences("Parent_DATA", MODE_PRIVATE);

        String stdclass = prf.getString("GRADE", null);

        if(stdclass==null){
            throw new RuntimeException("grade should not be null");
        }

        char stdgrade = stdclass.charAt(0);

        Log.d(TAG, "setupRecycleView: grade"+stdgrade);

        Log.d(TAG, "getdatawhendatachange: value function int"+Integer.valueOf(String.valueOf(stdgrade)));

        adapter.stopListening();
        query = collectionReference.whereEqualTo("grade", Integer.valueOf(String.valueOf(stdgrade))).whereEqualTo("date",formatdate).orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<CustomeEvent> options = new FirestoreRecyclerOptions.Builder<CustomeEvent>()
                .setQuery(query, CustomeEvent.class).build();


        adapter = new AgendaCalenderAdapter(options);

        rec_event_list.setAdapter(adapter);

        adapter.startListening();

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                Log.d(TAG, "onEvent: value size "+value.size());

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