package io.bcyl.douyin.Fragment.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

import io.bcyl.douyin.MyVideoActivity;
import io.bcyl.douyin.R;

public class VideoViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = HomeFragment.class.getName();
    private final PlayerView playerView;
    private final TextView loadingView;
    private SimpleExoPlayer player;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    public TextView usrName;
    public TextView vidTitle;
    public TextView at;
    public String url;
    public Context context;
    private VideoAdapter videoAdapter;
    private View itemView;
    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        context = itemView.getContext();
        usrName = itemView.findViewById(R.id.usrName);
        vidTitle  = itemView.findViewById(R.id.vidTitle);
        playerView = itemView.findViewById(R.id.video_view);
        at = itemView.findViewById(R.id.at);
        loadingView = itemView.findViewById(R.id.loading);


    }


    public View getItemView() {
        return itemView;
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
        playerView.setShowFastForwardButton(false);
        playerView.setShowRewindButton(false);
        playerView.setControllerAutoShow(true);
        playerView.showController();
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_IDLE){
                    loadingView.setText("视频暂时无法播放");
                }
                else if (playbackState == Player.STATE_BUFFERING){
                    playerView.setControllerAutoShow(true);
                    loadingView.setAlpha(1);
                }
                else if (playbackState == Player.STATE_READY) {
                    playerView.showController();
                    playerView.setControllerShowTimeoutMs(1000);
                    loadingView.setAlpha(0);
                }
                else if (playbackState == Player.STATE_ENDED) {
                    int idx = getAdapterPosition();
                    if (videoAdapter.getItemCount() > idx)
                        videoAdapter.getRecyclerView().smoothScrollToPosition(idx+1);
                }

            }
        });
        player.prepare();

    }

    public void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    public void setAdapter(VideoAdapter videoAdapter){
        this.videoAdapter = videoAdapter;
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

    public PlayerView getPlayerView() {
        return this.playerView;
    }
    public SimpleExoPlayer getPlayer(){
        return this.player;
    }

    public TextView getUsrName() {
        return usrName;
    }

    public TextView getVidTitle(){
        return vidTitle;
    }
}
