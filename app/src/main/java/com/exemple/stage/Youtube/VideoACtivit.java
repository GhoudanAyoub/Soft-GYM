package com.exemple.stage.Youtube;
/**
 * Created By GHOUADN AYOUB
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.exemple.stage.R;
import com.exemple.stage.Services.Config;
import com.exemple.stage.modele.Videos;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class VideoACtivit extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener {

    //****************************
    private static final int RECOVERY_REQUEST = 1;
    public String key;
    DatabaseReference databaseReference;
    TextView Name, textView37, textView39, textView41, textView43;
    com.exemple.stage.modele.Videos Videos;
    //private VideoView vv;
    String uriPath;
    ImageButton fullScreen;
    private MediaController mediacontroller;
    private Uri uri;
    private boolean isContinuously = false;
    private AdView mAdView;
    private YouTubePlayerView youTubeView;
    private int requestCode;
    private int resultCode;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_activit);
        key = getIntent().getStringExtra("ID");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Name = findViewById(R.id.Name);
        textView37 = findViewById(R.id.textView37);
        textView39 = findViewById(R.id.textView39);
        textView41 = findViewById(R.id.textView41);
        textView43 = findViewById(R.id.textView43);
        youTubeView = findViewById(R.id.youTubeView);

        databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        databaseReference.keepSynced(true);
        try {
            Query myTopPostsQuery = databaseReference.orderByChild("ID").equalTo(key);
            myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Videos = dataSnapshot1.getValue(Videos.class);
                        }
                        uriPath = Videos.getVideo();
                        //vv.setVideoPath(uriPath);
                        Name.setText(Videos.getVideoName());
                        textView37.setText(Videos.getChapter1());
                        textView39.setText(Videos.getChapter2());
                        textView41.setText(Videos.getChapter3());
                        textView43.setText(Videos.getOther());
                    } else {
                        Toast.makeText(getApplicationContext(), "No data Found !!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } catch (Exception p) {
            Toast.makeText(getApplicationContext(), "Check Your Connection !!", Toast.LENGTH_LONG).show();
        }

        //*****************START OF YOUTUBE CALL *********************//
        //youTubeView = (YouTubePlayerView) findViewById(R.id.videoWebView);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
        //*****************END OF YOUTUBE CALL *********************//


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            try {
                youTubePlayer.cueVideo(uriPath);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            } catch (Exception e) {
                onBackPressed();
                Toast.makeText(this, "Check Your Connection And Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPlaying() {
    }

    @Override
    public void onPaused() {
    }

    @Override
    public void onStopped() {
    }

    @Override
    public void onBuffering(boolean b) {
        // Called when buffering starts or ends.
    }

    @Override
    public void onSeekTo(int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
