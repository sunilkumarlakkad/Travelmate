package com.example.sunillakkad.travelmate.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.example.sunillakkad.travelmate.R;
import com.example.sunillakkad.travelmate.utils.Constants;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player_view);
        playerView.initialize(Constants.GOOGLE_SERVER_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult
            youTubeInitializationResult) {
        Toast.makeText(this, "Initialization Failed", Toast.LENGTH_LONG).show();
    }
}