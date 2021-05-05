package io.bcyl.douyin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import io.bcyl.douyin.Fragment.Home.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler =new Handler(Looper.getMainLooper());
    private Boolean logged =false;
    private static final int CHECK_LOGGED_CODE=1;
//    private static UseDataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // jump to HomeActivity after delay
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(MainActivity.this,HomeActivity.class);
                intent.putExtra("logged",logged);
                startActivityForResult(intent,CHECK_LOGGED_CODE);
            }
        },3000);

    }
}