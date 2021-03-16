package com.harini.primary.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.harini.primary.R;
import com.harini.primary.config.DeveloperKey;

public class LessonVideo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView ytplayerview;

    private static final String TAG="yotubed";
    private final String VIDEO_CODE= "nRUvr0KPJDA";
    private static final int RECOVERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_video);

        ytplayerview = findViewById(R.id.ytplayerview);




        Log.d(TAG, "onCreate: key "+DeveloperKey.DEVELOPER_KEY);

        ytplayerview.initialize(DeveloperKey.DEVELOPER_KEY, this);


    }


    @Override
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
    }
}