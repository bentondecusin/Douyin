package io.bcyl.douyin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class UploadVideoActivity extends AppCompatActivity {

    public static UploadVideoActivity uploadactivity;

    private String mp4Path;
    private VideoView videoView;
    private Button btConfirm;
    private Button btCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        uploadactivity = this;
        Intent intent = getIntent();
        mp4Path = intent.getStringExtra("mp4Path");
        videoView = findViewById(R.id.Edit_view);
        btConfirm = findViewById(R.id.bt_confirm);
        btCancel = findViewById(R.id.bt_cancel);

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(UploadVideoActivity.this, EditVideoActivity.class);
                newIntent.putExtra("videoPath", mp4Path);
                startActivity(newIntent);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cancel();
            }
        });

        videoView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        videoView.setVisibility(View.VISIBLE);
        videoView.setZOrderOnTop(true);
        videoView.requestFocus();
        Log.e("Update", mp4Path+"\n");
        videoView.setVideoPath(mp4Path);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
    }

    private void Cancel() {
        UploadVideoActivity.this.finish();
    }
}