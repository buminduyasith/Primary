package com.harini.primary.parent.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class AnnouncementFragment extends Fragment {


    private RecyclerView recyleview_announcement;
    private LinearLayout announcemtns_noresults_container;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;


    private AnnouncementRecycleViewAdapter adapter;

    //private FirestoreRecyclerOptions<Announcement> response;


    private static final String TAG = "annfragmentD";
    private String grade;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_dashboard_announcement, container, false);

        setupUi(view);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("announcements");

        setupRecycleView();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupUi(View view) {

        recyleview_announcement = view.findViewById(R.id.recyleview_announcement);
        announcemtns_noresults_container = view.findViewById(R.id.announcemtns_noresults_container);
    }


    private void setupRecycleView() {

        // Query query;

        FirebaseUser firebaseUser = mAuth.getCurrentUser();


        db.collection("Parents")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        grade = documentSnapshot.getString("grade");

                        Query query = collectionReference.whereEqualTo("grade", grade).orderBy("timestamp", Query.Direction.DESCENDING);


                        FirestoreRecyclerOptions<Announcement> response = new FirestoreRecyclerOptions.Builder<Announcement>()
                                .setQuery(query, Announcement.class).build();


                        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                //Log.d(TAG, "onEvent: snapshort" + value.size());

                                if(value!=null){

                                    if (value.size() > 0) {

                                        //Log.d(TAG, "onEvent: recy view");
                                        announcemtns_noresults_container.setVisibility(View.GONE);
                                        recyleview_announcement.setVisibility(View.VISIBLE);
                                    } else {

                                        //Log.d(TAG, "onEvent: container view");
                                        recyleview_announcement.setVisibility(View.GONE);
                                        announcemtns_noresults_container.setVisibility(View.VISIBLE);
                                    }
                                }



                            }
                        });


                        adapter = new AnnouncementRecycleViewAdapter(response,"PARENT");


                        recyleview_announcement.setHasFixedSize(true);

                        recyleview_announcement.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

                        recyleview_announcement.setAdapter(adapter);


                        adapter.startListening();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null) {
            adapter.startListening();
        } else {
            //Log.d(TAG, "onStart: adapter is null");
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
