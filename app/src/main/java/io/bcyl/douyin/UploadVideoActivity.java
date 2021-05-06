package io.bcyl.douyin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import io.bcyl.douyin.Model.UploadResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.bcyl.douyin.Constants.BASE_URL;
import static io.bcyl.douyin.Constants.IDENTIFIER;
import static io.bcyl.douyin.Constants.STUDENT_ID;
import static io.bcyl.douyin.Constants.USER_NAME;
import static io.bcyl.douyin.Constants.token;

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