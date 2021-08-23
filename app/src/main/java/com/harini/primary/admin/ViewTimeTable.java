package com.harini.primary.admin;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.R;
import com.harini.primary.adapters.TimeTableAdapter;
import com.harini.primary.models.ClassRoom;
import com.harini.primary.models.Period;
import com.harini.primary.models.TimeTable;
import com.harini.primary.utill.HintSpinnerAdapter;
import com.harini.primary.utill.TextFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewTimeTable extends AppCompatActivity {

    private Spinner spnClass;
    private TextView tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday;
    private RecyclerView rvPeriods;

    private ArrayList<String> listClasses;
    private String userRole, selectedClass;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    private TimeTableAdapter adapter;
    private ClassRoom classRoom;
    private List<ClassRoom> classRoomList;
    private ArrayList<Period> listPeriods;

    private static final String TAG = "timetable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_time_table);

        userRole = getIntent().getStringExtra("userRole");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        setEventListeners();
        loadClasses();
    }

    private void initViews() {
        spnClass = findViewById(R.id.spnClass);
        tvMonday = findViewById(R.id.tvMonday);
        tvTuesday = findViewById(R.id.tvTuesday);
        tvWednesday = findViewById(R.id.tvWednesday);
        tvThursday = findViewById(R.id.tvThursday);
        tvFriday = findViewById(R.id.tvFriday);
        rvPeriods = findViewById(R.id.rvPeriods);
    }

    private void setEventListeners() {
        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnClass.getSelectedItem().equals(getString(R.string.select_class))) {
                    selectedClass = spnClass.getSelectedItem().toString();
                    getTimetable();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    selectDay(1);
                    showPeriods("monday");
                }
            }
        });

        tvTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    selectDay(2);
                    showPeriods("tuesday");
                }
            }
        });

        tvWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    selectDay(3);
                    showPeriods("wednesday");
                }
            }
        });

        tvThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    selectDay(4);
                    showPeriods("thursday");
                }
            }
        });

        tvFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    selectDay(5);
                    showPeriods("friday");
                }
            }
        });
    }

    private void selectDay(int index) {
        tvMonday.setTextColor(index == 1 ? getColor(R.color.white) : getColor(R.color.heading_color));
        tvTuesday.setTextColor(index == 2 ? getColor(R.color.white) : getColor(R.color.heading_color));
        tvWednesday.setTextColor(index == 3 ? getColor(R.color.white) : getColor(R.color.heading_color));
        tvThursday.setTextColor(index == 4 ? getColor(R.color.white) : getColor(R.color.heading_color));
        tvFriday.setTextColor(index == 5 ? getColor(R.color.white) : getColor(R.color.heading_color));

        tvMonday.setBackground(index == 1 ?
                ContextCompat.getDrawable(ViewTimeTable.this, R.drawable.bg_day_selected) : null);
        tvTuesday.setBackground(index == 2 ?
                ContextCompat.getDrawable(ViewTimeTable.this, R.drawable.bg_day_selected) : null);
        tvWednesday.setBackground(index == 3 ?
                ContextCompat.getDrawable(ViewTimeTable.this, R.drawable.bg_day_selected) : null);
        tvThursday.setBackground(index == 4 ?
                ContextCompat.getDrawable(ViewTimeTable.this, R.drawable.bg_day_selected) : null);
        tvFriday.setBackground(index == 5 ?
                ContextCompat.getDrawable(ViewTimeTable.this, R.drawable.bg_day_selected) : null);
    }

    private void loadClasses() {
        listClasses = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.grade_array)));
        listClasses.add(getString(R.string.select_class));
        HintSpinnerAdapter classesAdapter = new HintSpinnerAdapter(this, R.layout.spinner_item, listClasses);
        spnClass.setAdapter(classesAdapter);
        spnClass.setSelection(classesAdapter.getCount());
    }

    private void getTimetable() {
        collectionReference = db.collection("timetable");
        Query query = collectionReference.whereEqualTo("className", selectedClass).limit(1);

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess: ");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            classRoomList = queryDocumentSnapshots.toObjects(ClassRoom.class);
                            classRoom = classRoomList.get(0);
                            tvMonday.performClick();
                        } else {
                            classRoom = null;
                            classRoomList = null;
                            rvPeriods.setAdapter(null);
                            Toast.makeText(ViewTimeTable.this, "Timetable is unavailable for class " + selectedClass, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    private void showPeriods(String day) {
        if (classRoom != null && classRoom.getTimeTable() != null) {
            switch (day) {
                case "monday":
                    listPeriods = classRoom.getTimeTable().getMonday();
                    break;
                case "tuesday":
                    listPeriods = classRoom.getTimeTable().getTuesday();
                    break;
                case "wednesday":
                    listPeriods = classRoom.getTimeTable().getWednesday();
                    break;
                case "thursday":
                    listPeriods = classRoom.getTimeTable().getThursday();
                    break;
                case "friday":
                    listPeriods = classRoom.getTimeTable().getFriday();
                    break;
            }

            if (listPeriods != null && listPeriods.size() > 0) {
                adapter = new TimeTableAdapter(listPeriods, userRole, true, day);
            } else {
                adapter = null;
            }

            if (adapter != null) {
                adapter.setOnItemClickListener(new TimeTableAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(String day, String title, int position) {
                        showAlertBox(day, title, position);
                    }
                });

                rvPeriods.setHasFixedSize(true);
                rvPeriods.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvPeriods.setAdapter(adapter);
            } else {
                rvPeriods.setAdapter(null);
                Toast.makeText(ViewTimeTable.this, "Periods of " + TextFormatter.capitalizeFirstLetter(day) + " are unavailable.", Toast.LENGTH_SHORT).show();
            }
        } else {
            rvPeriods.setAdapter(null);
            Toast.makeText(ViewTimeTable.this, "Timetable is unavailable for class " + selectedClass, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields() {
        if (spnClass.getSelectedItem().toString().equals(getString(R.string.select_class))) {
            Toast.makeText(this, "Please select class", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showAlertBox(String day, String currentTitle, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_update_title, null, false);

        EditText etTitle = viewInflated.findViewById(R.id.etTitle);
        etTitle.setText(currentTitle);
        etTitle.setSelection(currentTitle.length());
        etTitle.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setTitle("Change Subject Name")
                .setView(viewInflated)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newTitle = etTitle.getText().toString();
                        updateTitle(day, newTitle, position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void updateTitle(String day, String newTitle, int position) {
        Period p;
        TimeTable timeTable = classRoomList.get(0).getTimeTable();

        switch (day) {
            case "monday":
                p = timeTable.getMonday().get(position);
                p.setTitle(newTitle);
                timeTable.getMonday().set(position, p);
                break;
            case "tuesday":
                p = timeTable.getTuesday().get(position);
                p.setTitle(newTitle);
                timeTable.getTuesday().set(position, p);
                break;
            case "wednesday":
                p = timeTable.getWednesday().get(position);
                p.setTitle(newTitle);
                timeTable.getWednesday().set(position, p);
                break;
            case "thursday":
                p = timeTable.getThursday().get(position);
                p.setTitle(newTitle);
                timeTable.getThursday().set(position, p);
                break;
            case "friday":
                p = timeTable.getFriday().get(position);
                p.setTitle(newTitle);
                timeTable.getFriday().set(position, p);
                break;
        }

        classRoomList.get(0).setTimeTable(timeTable);

        db.collection("timetable").document(classRoomList.get(0).getClassName()).set(classRoomList.get(0))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                        Toast.makeText(ViewTimeTable.this, "Subject name successfully updated.", Toast.LENGTH_SHORT).show();
                        adapter.updateSubjectName(position, newTitle);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(ViewTimeTable.this, "Subject name update failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}