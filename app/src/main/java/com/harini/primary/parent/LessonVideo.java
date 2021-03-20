package com.harini.primary.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.harini.primary.R;
import com.harini.primary.config.DeveloperKey;

// implements YouTubePlayer.OnInitializedListener
public class LessonVideo extends YouTubeBaseActivity{

    private YouTubePlayerView ytplayerview;

    private static final String TAG="yotubed";
    private final String VIDEO_CODE= "nRUvr0KPJDA";
    private static final int RECOVERY_REQUEST = 1;


    private YouTubePlayer.OnInitializedListener mOnInitializedListener;
    private  YouTubePlayer mYouTubePlayer;
    private Button btnplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_video);

        ytplayerview = findViewById(R.id.ytplayerview);

        youTubePlayerSetup();



        Log.d(TAG, "onCreate: key "+DeveloperKey.DEVELOPER_KEY);

       // ytplayerview.initialize(DeveloperKey.DEVELOPER_KEY, this);

        btnplay = findViewById(R.id.btnplay);

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYouTubePlayer.cueVideo("icUFDf37Ls4");
            }
        });


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


    /*@Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.loadVideo("icUFDf37Ls4");
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            Toast.makeText(this, "Error Intializing Youtube Player", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            getYouTubePlayerProvider().initialize(DeveloperKey.DEVELOPER_KEY, this);
        }
    }
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return ytplayerview;
    }*/
}