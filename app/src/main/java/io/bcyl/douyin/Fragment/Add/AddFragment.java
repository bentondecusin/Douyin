package io.bcyl.douyin.Fragment.Add;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.bcyl.douyin.R;
import io.bcyl.douyin.UploadVideoActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment implements SurfaceHolder.Callback{

    private static final String TAG = "Douyin";
    private static final int REQUEST_CODE_VIDEO = 66;
    private static final String VIDEO_TYPE = "video/*";

    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private SurfaceHolder mHolder;
    private VideoView mVideoView;
    private Button mRecordButton;
    private Button mDCIMButton;
    private Button mChangeCameraButton;
    private boolean isRecording = false;
    private int cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;

    private String mp4Path = "";
    private Uri videoUri = null;
    private String param1;
    private static final String ARG_PARAM = "param";

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static AddFragment newInstance(String param) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        mSurfaceView = view.findViewById(R.id.surfaceview);

        mRecordButton = view.findViewById(R.id.bt_record);
        mDCIMButton = view.findViewById(R.id.bt_DCIM);
        mChangeCameraButton = view.findViewById(R.id.bt_change);

        mChangeCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });
        mDCIMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                getFile(REQUEST_CODE_VIDEO, VIDEO_TYPE, "选择视频");
            }
        });

        mHolder = mSurfaceView.getHolder();

        requestPermission();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_VIDEO== requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                videoUri = data.getData();

                if (videoUri != null) {
                    Log.d(TAG, "pick cover image " + videoUri.toString());
                } else {
                    Log.d(TAG, "uri2File fail " + data.getData());
                }
                mp4Path = getRealPathFromURI(videoUri);
                if (mp4Path != null) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), UploadVideoActivity.class);
                    intent.putExtra("mp4Path", mp4Path);
                    startActivity(intent);
                }
            } else {
                Log.d(TAG, "file pick fail");
            }
        }
    }


    private void getFile(int requestCode, String type, String title) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record(mVideoView);
            }
        });
    }


    private void requestPermission() {
        if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        initCamera(cameraID);
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCamera != null) {
                    mCamera.autoFocus(new Camera.AutoFocusCallback(){
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                Log.e("My", "Focus Success!");
                            } else {
                                mCamera.autoFocus(this);
                            }
                        }
                    });
                }
            }
        });
        mHolder.addCallback(this);
    }

    private void initCamera(int cameraID) {
        if (mCamera != null) {
            mHolder.removeCallback(this);
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
        mCamera = Camera.open(cameraID);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.set("orientation", "portrait");
        if (cameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
            parameters.set("rotation", 90);
        }
        else {
            parameters.set("rotation", 270);
        }
        parameters.setRecordingHint(true);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
    }

    private void switchCamera(){
        if (cameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
            cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        try {
            releaseMediaRecorder();
            initCamera(cameraID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean prepareVideoRecorder() {
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        mp4Path = getOutputMediaPath();
        mMediaRecorder.setOutputFile(mp4Path);

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
        mMediaRecorder.setOrientationHint(90);

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    public void record(View view) {
        if (isRecording) {
            mRecordButton.setText("+");

            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();

//            mVideoView.setVisibility(View.VISIBLE);
//            mVideoView.setVideoPath(mp4Path);
//            mVideoView.start();
            Intent intent = new Intent(getActivity().getApplicationContext(), UploadVideoActivity.class);
            intent.putExtra("mp4Path", mp4Path);
            startActivity(intent);
        } else {
            if(prepareVideoRecorder()) {
                mRecordButton.setText("完成");
                mMediaRecorder.start();
            }
        }
        isRecording = !isRecording;

    }

    private String getOutputMediaPath() {
        File mediaStorageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        }
        else {
            mediaStorageDir = this.getActivity().getFilesDir();
        }
        // mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "IMG_" + timeStamp + ".mp4");
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        Log.d(TAG, "mp4Path: " + mediaFile.getAbsolutePath());
        return mediaFile.getAbsolutePath();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        //停止预览效果
        mCamera.stopPreview();
        //重新设置预览效果
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            initCamera(cameraID);
        }
        mCamera.startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    private String getRealPathFromURI(Uri fileUrl) {
        String fileName = null;
        List<String> pathSeg = fileUrl.getPathSegments();
        fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + '/' + pathSeg.get(2) + '/' + pathSeg.get(3);
        return fileName;
    }
}