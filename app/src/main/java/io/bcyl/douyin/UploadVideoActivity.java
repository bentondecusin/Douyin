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

    private String mp4Path;
    private VideoView videoView;
    private Button btConfirm;
    private Button btCancel;

    private UploadVideoAPI api;
    private Bitmap bitmap;
    private File video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        Intent intent = getIntent();
        mp4Path = intent.getStringExtra("mp4Path");
        videoView = findViewById(R.id.Edit_view);
        btConfirm = findViewById(R.id.bt_confirm);
        btCancel = findViewById(R.id.bt_cancel);
        initNetwork();

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upload();
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

    private void initNetwork() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(UploadVideoAPI.class);
    }

    private void Upload() {
        bitmap = getVideoThumb();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] coverImageData= out.toByteArray();

        video = new File(mp4Path);

        if (coverImageData == null || coverImageData.length == 0) {
            Toast.makeText(this, "封面不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        MultipartBody.Part cover_image_part = MultipartBody.Part.createFormData(
                "image",
                "cover.png",
                RequestBody.create(MediaType.parse("multipart/form-data"), coverImageData)
        );

        MultipartBody.Part video_part = MultipartBody.Part.createFormData(
                "video",
                "video.mp4",
                RequestBody.create(MediaType.parse("multipart/form-data"), video)
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<UploadResponse> call = api.uploadVideo(
                        IDENTIFIER + STUDENT_ID,
                        USER_NAME,
                        "",
                        cover_image_part,
                        video_part,
                        token
                );
                Log.e("My", "Test");
                try {
                    Response<UploadResponse> response = call.execute();


                    if (response.isSuccessful() && response.body().success) {
                        Log.e("My", "Success!");
                        UploadVideoActivity.this.finish();
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UploadVideoActivity.this, "上传失败!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void Cancel() {
        UploadVideoActivity.this.finish();
    }

    private Bitmap getVideoThumb() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mp4Path);
        return mediaMetadataRetriever.getFrameAtTime();
    }
}