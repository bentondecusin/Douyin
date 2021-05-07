package io.bcyl.douyin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

public class MyVideoActivity extends AppCompatActivity {
    private String url;
    private PlayerView playerView;
    private SimpleExoPlayer player;

    public MyVideoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video_view);

        Log.d("MyVideoAc","ok");
        Intent intent=getIntent();
        String title = intent.getStringExtra("title");
        url=intent.getStringExtra("url");
        String userName = intent.getStringExtra("userName");

        playerView = (PlayerView)findViewById(R.id.video_view);
        TextView userNameView = (TextView) findViewById(R.id.usrName);
        userNameView.setText(userName);

        TextView videoTitleView = (TextView) findViewById(R.id.vidTitle);
        videoTitleView.setText(title);

        initializePlayer();
        new Handler(getMainLooper()).postDelayed(() -> getPlayer().play(),200);

    }
    public void pauseCurrentVideo(){
        getPlayer().pause();
    }

    public void resumeCurrentVideo(){
        getPlayer().play();
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeCurrentVideo();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseCurrentVideo();
    }

    @Override
    public void onStop() {
        super.onStop();
        pauseCurrentVideo();
    }

    public void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = new MediaItem.Builder().setUri(url)
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build();
        player.setMediaItem(mediaItem);
        int currentWindow = 0;
        long playbackPosition = 0;
        player.seekTo(currentWindow, playbackPosition);
        playerView.setShowPreviousButton(false);
        playerView.setShowNextButton(false);
        playerView.setShowFastForwardButton(false);
        playerView.setShowRewindButton(false);
        playerView.setControllerAutoShow(false);
        playerView.hideController();
        player.prepare();

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_ENDED) {
                    finish();
                }
            }
        });
    }
    public SimpleExoPlayer getPlayer(){
        return this.player;
    }
}