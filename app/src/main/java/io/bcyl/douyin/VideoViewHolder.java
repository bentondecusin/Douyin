package io.bcyl.douyin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

import io.bcyl.douyin.Fragment.Home.HomeFragment;

import static com.google.android.exoplayer2.Player.STATE_ENDED;

public class VideoViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = HomeFragment.class.getName();
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    public TextView usrName;
    public TextView vidTitle;
    public String url;
    Context context;
    View view;
    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        usrName   = itemView.findViewById(R.id.vidTitle);
        vidTitle  = itemView.findViewById(R.id.usrName);
        playerView = itemView.findViewById(R.id.video_view);
    }

    public void initializePlayer() {
        player = new SimpleExoPlayer.Builder(context).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = new MediaItem.Builder().setUri(url)
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build();
        player.setMediaItem(mediaItem);
        player.seekTo(currentWindow, playbackPosition);
        playerView.setShowPreviousButton(false);
        playerView.setShowNextButton(false);
        playerView.setControllerAutoShow(false);
        playerView.hideController();
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    //TODO Next vid
                }
            }
        });
        player.prepare();
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    public SimpleExoPlayer getPlayer(){
        return this.player;
    }

}