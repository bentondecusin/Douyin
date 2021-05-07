package io.bcyl.douyin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import io.bcyl.douyin.Utils.Constants;
import io.bcyl.douyin.Utils.UploadResponse;
import io.bcyl.douyin.Utils.Util;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.bcyl.douyin.Utils.Constants.BASE_URL;
import static io.bcyl.douyin.Utils.Constants.IDENTIFIER;
import static io.bcyl.douyin.Utils.Constants.STUDENT_ID;
import static io.bcyl.douyin.Utils.Constants.USER_NAME;
import static io.bcyl.douyin.Utils.Constants.token;

public class EditVideoActivity extends AppCompatActivity {
    private static final String TAG = "Douyin";
    private static final int REQUEST_CODE_COVER_IMAGE = 101;
    private static final String COVER_IMAGE_TYPE = "image/*";
    private ImageButton bt_cover;
    private String mp4Path;
    private EditText comment;
    private Button bt_draft;
    private Button bt_upload;
    private Uri coverImageUri;
    private UploadVideoAPI api;
    private Bitmap bitmap;
    private File video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        initActivity();
        initNetwork();

        bitmap = getVideoThumb();
        bt_cover.setImageBitmap(bitmap);
        bt_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFile(REQUEST_CODE_COVER_IMAGE, COVER_IMAGE_TYPE, "选择图片");

            }
        });

        bt_draft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UploadVideoActivity.uploadactivity != null) {
                    UploadVideoActivity.uploadactivity.finish();
                }
                EditVideoActivity.this.finish();
            }
        });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upload();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_COVER_IMAGE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                coverImageUri = data.getData();
                bt_cover.setImageURI(coverImageUri);

                if (coverImageUri != null) {
                    Log.d(TAG, "pick cover image " + coverImageUri.toString());
                } else {
                    Log.d(TAG, "uri2File fail " + data.getData());
                }

            } else {
                Log.d(TAG, "file pick fail");
            }
        }
    }

    private void initActivity() {
        mp4Path = getIntent().getStringExtra("videoPath");

        bt_cover = findViewById(R.id.cover_img);
        comment = findViewById(R.id.input_comment);
        bt_draft = findViewById(R.id.bt_draft);
        bt_upload = findViewById(R.id.bt_upload);
    }

    private void initNetwork() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(UploadVideoAPI.class);
    }

    private void getFile(int requestCode, String type, String title) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    private void getCoverImage() {
        return;
    }

    private void Upload() {
        byte[] coverImageData = null;
        if (coverImageUri != null) {
             coverImageData = readDataFromUri(coverImageUri);
        }
        else {
            bitmap = getVideoThumb();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            coverImageData = out.toByteArray();
        }

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
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                Call<UploadResponse> call = api.uploadVideo(
                        IDENTIFIER,
                        sharedPreferences.getString( "userName", "Guest"),
                         Constants.DELIM +comment.getText(),
                        cover_image_part,
                        video_part,
                        token
                );
                Log.i("My", STUDENT_ID + ";" + USER_NAME + ";" + comment.toString());
                try {
                    Response<UploadResponse> response = call.execute();


                    if (response.isSuccessful() && response.body().success) {
                        Log.e("My", "Success!");
                        Toast.makeText(EditVideoActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                        if (UploadVideoActivity.uploadactivity != null) {
                            UploadVideoActivity.uploadactivity.finish();
                        }
                        EditVideoActivity.this.finish();
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EditVideoActivity.this, "上传失败!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private Bitmap getVideoThumb() {
        // 获取视频第一帧图片
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mp4Path);
        return mediaMetadataRetriever.getFrameAtTime();
    }

    private byte[] readDataFromUri(Uri uri) {
        byte[] data = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            data = Util.inputStream2bytes(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}