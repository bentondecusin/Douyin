package io.bcyl.douyin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import org.w3c.dom.Text;

import io.bcyl.douyin.Fragment.Home.VideoViewHolder;

import static android.os.Looper.getMainLooper;

public class MyVideoActivity extends AppCompatActivity {
    private String title,comment,url,userName;
    private PlayerView playerView;
    private TextView userNameView,videoTitleView;
    private SimpleExoPlayer player;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    public MyVideoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video_view);

        Log.d("MyVideoAc","ok");
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        comment=intent.getStringExtra("comment");
        url=intent.getStringExtra("url");
        userName=intent.getStringExtra("userName");

        playerView = (PlayerView)findViewById(R.id.video_view);

        userNameView=(TextView)findViewById(R.id.usrName);
        userNameView.setText(userName);

        videoTitleView=(TextView)findViewById(R.id.vidTitle);
        videoTitleView.setText(title);
        initializePlayer();

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getPlayer().play();
            }
        },200);

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
        player.seekTo(currentWindow, playbackPosition);
        playerView.setShowPreviousButton(false);
        playerView.setShowNextButton(false);
        playerView.setShowFastForwardButton(false);
        playerView.setShowRewindButton(false);
        playerView.setControllerAutoShow(false);
        playerView.hideController();
        player.prepare();
    }
    public SimpleExoPlayer getPlayer(){
        return this.player;
    }
}