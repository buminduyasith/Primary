package com.harini.primary.parent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.Models.Teacher;
import com.harini.primary.Models.VideoLesson;
import com.harini.primary.R;
import com.harini.primary.adapters.AddVideoLessonsAdapter;
import com.harini.primary.adapters.ViewVideoLessonsAdapter;
import com.harini.primary.config.DeveloperKey;

public class ViewVideoLessons extends YouTubeBaseActivity {


    private YouTubePlayerView ytplayerview;

    private YouTubePlayer.OnInitializedListener mOnInitializedListener;
    private  YouTubePlayer mYouTubePlayer;

    private RecyclerView rv_yt_video_list;

    private TextView txtytplayingtitle;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    private ViewVideoLessonsAdapter adapter;

    private CardView card_ytplaying;

    private LinearLayout videolessons_noresults_container;
    private static final String TAG="viewvideod";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video_lessons);

        setupUi();

        youTubePlayerSetup();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("VideoLessons");

        setupRecycleView();


    }

    private void setupUi() {
        ytplayerview = findViewById(R.id.ytplayerview);
        rv_yt_video_list = findViewById(R.id.rv_yt_video_list);
        videolessons_noresults_container = findViewById(R.id.videolessons_noresults_container);
        txtytplayingtitle = findViewById(R.id.txtytplayingtitle);

        card_ytplaying = findViewById(R.id.card_ytplaying);
    }

    private void youTubePlayerSetup(){


        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!b){
                    mYouTubePlayer =youTubePlayer;
                    // mYouTubePlayer.cueVideo(VIDEO_CODE);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        ytplayerview.initialize(DeveloperKey.DEVELOPER_KEY,mOnInitializedListener);
    }




    private void setupRecycleView() {

        // FirebaseUser firebaseUser = mAuth.getCurrentUser();

        SharedPreferences prf = getSharedPreferences("Parent_DATA", MODE_PRIVATE);

       String grade = prf.getString("GRADE", null);

       if(grade==null){
           throw new RuntimeException("grade should not be null");
       }

        Log.d(TAG, "setupRecycleView: grade"+grade);

        Query query = collectionReference.whereEqualTo("class", grade).orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<VideoLesson> options = new FirestoreRecyclerOptions.Builder<VideoLesson>()
                .setQuery(query, VideoLesson.class).build();


        adapter = new ViewVideoLessonsAdapter(options);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if (value != null) {


                    if (value.size() > 0) {


                        videolessons_noresults_container.setVisibility(View.GONE);
                        rv_yt_video_list.setVisibility(View.VISIBLE);
                        card_ytplaying.setVisibility(View.VISIBLE);



                    } else {

                        card_ytplaying.setVisibility(View.GONE);
                        rv_yt_video_list.setVisibility(View.GONE);
                        videolessons_noresults_container.setVisibility(View.VISIBLE);

                    }

                }


            }
        });


        rv_yt_video_list.setHasFixedSize(true);

        rv_yt_video_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rv_yt_video_list.setAdapter(adapter);


        adapter.setOnItemClickListner(new ViewVideoLessonsAdapter.onItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                VideoLesson videoLesson = documentSnapshot.toObject(VideoLesson.class);

                mYouTubePlayer.cueVideo(videoLesson.getVideoid());
                txtytplayingtitle.setText(videoLesson.getTitle());
                Toast.makeText(getApplicationContext(),"wow"+videoLesson.getVideoid(),Toast.LENGTH_SHORT).show();

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