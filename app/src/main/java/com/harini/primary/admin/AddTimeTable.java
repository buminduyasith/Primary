package com.harini.primary.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.R;
import com.harini.primary.adapters.TimeTableAdapter;
import com.harini.primary.models.ClassRoom;
import com.harini.primary.models.Period;
import com.harini.primary.models.TimeTable;
import com.harini.primary.utill.HintSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTimeTable extends AppCompatActivity {

    private static final String TAG = "timetable";
    private Spinner spnClass, spnDay;
    private RecyclerView rvPeriods;
    private Button btnSave;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private ArrayList<String> listClasses, listDays;
    private ArrayList<Period> listPeriods;
    private String selectedClass, selectedDay;

    private TimeTableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_table);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("timetable");

        initViews();
        setEventListeners();
        resetFields();
    }

    private void initViews() {
        spnClass = findViewById(R.id.spnClass);
        spnDay = findViewById(R.id.spnDay);
        rvPeriods = findViewById(R.id.rvPeriods);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setEventListeners() {
        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnClass.getSelectedItem().toString().equals(getString(R.string.select_class))) {
                    selectedClass = listClasses.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnDay.getSelectedItem().toString().equals(getString(R.string.select_day))) {
                    selectedDay = listDays.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    addTimetable();
                }
            }
        });
    }

    private void loadClasses() {
        selectedClass = null;
        listClasses = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.grade_array)));
        listClasses.add(getString(R.string.select_class));
        HintSpinnerAdapter classesAdapter = new HintSpinnerAdapter(this, R.layout.spinner_item, listClasses);
        spnClass.setAdapter(classesAdapter);
        spnClass.setSelection(classesAdapter.getCount());
    }

    private void loadDays() {
        selectedDay = null;
        listDays = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.weekdays_array)));
        listDays.add(getString(R.string.select_day));
        HintSpinnerAdapter daysAdapter = new HintSpinnerAdapter(this, R.layout.spinner_item, listDays);
        spnDay.setAdapter(daysAdapter);
        spnDay.setSelection(daysAdapter.getCount());
    }

    private void loadPeriods() {
        listPeriods = new ArrayList<>();
        ArrayList<String> listPeriodsStr = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.periods_time_array)));

        for (String str : listPeriodsStr) {
            Period period = new Period();
            period.setStartTime(str.split("-")[0].trim());
            period.setEndTime(str.split("-")[1].trim());
            period.setTitle("");
            listPeriods.add(period);
        }

        adapter = new TimeTableAdapter(listPeriods, "ADMIN", false, null);
        rvPeriods.setHasFixedSize(true);
        rvPeriods.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvPeriods.setAdapter(adapter);
    }

    private boolean validateFields() {
        if (spnClass.getSelectedItem().toString().equals(getString(R.string.select_class))) {
            Toast.makeText(this, "Please select class", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spnDay.getSelectedItem().toString().equals(getString(R.string.select_day))) {
            Toast.makeText(this, "Please select day", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addTimetable() {
        TimeTable tTable = new TimeTable();

        ClassRoom classRoom = new ClassRoom();
        classRoom.setClassName(selectedClass);
        classRoom.setTimeTable(setPeriods(tTable));

        collectionReference.whereEqualTo("className", selectedClass).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            addTimetableToFireStore(classRoom);

                        } else {
                            List<ClassRoom> classRoomDocList = queryDocumentSnapshots.toObjects(ClassRoom.class);
                            ClassRoom classRoomObj = classRoomDocList.get(0);
                            classRoomObj.setTimeTable(setPeriods(classRoomObj.getTimeTable()));

                            Log.d(TAG, "Firestore Document : "+ classRoomObj.getTimeTable());

                            addTimetableToFireStore(classRoomObj);
                        }
                    }
                });
    }

    private TimeTable setPeriods(TimeTable timeTable) {
        if (selectedDay != null && !selectedDay.isEmpty()) {
            switch (selectedDay.toLowerCase().trim()) {
                case "monday":
                    timeTable.setMonday(adapter.getPeriodsList());
                    break;

                case "tuesday":
                    timeTable.setTuesday(adapter.getPeriodsList());
                    break;

                case "wednesday":
                    timeTable.setWednesday(adapter.getPeriodsList());
                    break;

                case "thursday":
                    timeTable.setThursday(adapter.getPeriodsList());
                    break;

                case "friday":
                    timeTable.setFriday(adapter.getPeriodsList());
                    break;
            }
        }
        return timeTable;
    }

    private void addTimetableToFireStore(ClassRoom classRoom) {
        db.collection("timetable").document(classRoom.getClassName()).set(classRoom)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                        Toast.makeText(AddTimeTable.this, "Successfully added periods of " + selectedDay + " to " + selectedClass + " timetable", Toast.LENGTH_SHORT).show();
                        resetFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(AddTimeTable.this, "Failed to add periods of " + selectedDay + " to " + selectedClass + " timetable", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetFields() {
        loadClasses();
        loadDays();
        loadPeriods();
    }
}