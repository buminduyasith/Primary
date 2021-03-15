package com.harini.primary.teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.Models.Announcement;
import com.harini.primary.R;
import com.harini.primary.adapters.AnnouncementRecycleViewAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateAnnouncement extends AppCompatActivity implements AnnouncementDialog.AnnouncementDialogListner {

    private FloatingActionButton Fab_addannouncment;

    private RecyclerView recyleview_announcement;


    private String announcement;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;


    private AnnouncementRecycleViewAdapter adapter;

    private LinearLayout chat_noresults_container;


    private static final String TAG = "createanndebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

        setupUi();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("announcements");



        setupRecycleView();



        Fab_addannouncment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openDialog();
            }
        });



    }

    private void setupUi(){

        Fab_addannouncment = findViewById(R.id.Fab_addannouncment);
        recyleview_announcement = findViewById(R.id.recyleview_announcement);
        chat_noresults_container = findViewById(R.id.chat_noresults_container);
    }


    private void setupRecycleView(){

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        Query query = collectionReference.whereEqualTo("userid",firebaseUser.getUid()).orderBy("timestamp",Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Announcement> options = new FirestoreRecyclerOptions.Builder<Announcement>()
                .setQuery(query,Announcement.class).build();



        adapter = new AnnouncementRecycleViewAdapter(options);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Log.d(TAG, "onStart:b "+value.size());


                if(value !=null){

                    if(value.size()>0){

                        chat_noresults_container.setVisibility(View.GONE);
                        recyleview_announcement.setVisibility(View.VISIBLE);


                    }
                    else{

                        recyleview_announcement.setVisibility(View.GONE);
                        chat_noresults_container.setVisibility(View.VISIBLE);

                    }

                }




            }
        });



       /* adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                Log.d(TAG, "onItemRangeInserted: "+itemCount);
                Log.d(TAG, "setupRecycleView: bumi count"+options.getSnapshots().size());
            }

            @Override
            public void onChanged() {
                Log.d(TAG, "onChanged: "+options.getSnapshots().size());
                super.onChanged();
            }
        });*/


        recyleview_announcement.setHasFixedSize(true);

        recyleview_announcement.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyleview_announcement.setAdapter(adapter);










    }



    private void openDialog(){

        AnnouncementDialog announcementDialog = new AnnouncementDialog();

        announcementDialog.show(getSupportFragmentManager(),"Make an Announcement");

    }

    @Override
    public void addText(String announcement) {
        this.announcement = announcement;
        //Toast.makeText(getApplicationContext(),"value "+announcement,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addannouncement(String announcement) {

        if(announcement.isEmpty()){

            Toast.makeText(getApplicationContext(),"A required field is missing",Toast.LENGTH_SHORT).show();

            return;
        }

        FirebaseUser firebaseUser = mAuth.getCurrentUser();


        //test



        db.collection("teachers")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        Timestamp timestamp = new Timestamp(new Date());
                        Map<String, Object> newannouncement = new HashMap<>();
                        newannouncement.put("message",announcement);
                        newannouncement.put("userid",firebaseUser.getUid());
                        newannouncement.put("grade",documentSnapshot.getString("grade"));
                        newannouncement.put("timestamp",timestamp);

                        db.collection("announcements")
                                .add(newannouncement)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getApplicationContext(),"announcement added to the system successfully",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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
}