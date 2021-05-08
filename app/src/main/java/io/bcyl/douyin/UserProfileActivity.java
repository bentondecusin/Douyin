package io.bcyl.douyin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.bcyl.douyin.Fragment.User.UserVideoAdapter;
import io.bcyl.douyin.HomeActivity;
import io.bcyl.douyin.Utils.VideoInfo;
import io.bcyl.douyin.R;
import io.bcyl.douyin.Utils.Network;

import static android.os.Looper.getMainLooper;

public class UserProfileActivity extends AppCompatActivity {
    public static final int LOGIN_CODE = 1;
    private List<VideoInfo> itemList = new ArrayList<>();
    private ImageView headImageView;
    private RecyclerView myVideoView;
    private TextView userNameView;
    private Button loginButton;
    private View.OnClickListener loggedListener, unLoggedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        headImageView = findViewById(R.id.head_image);
        userNameView = findViewById(R.id.user_name_view);
        myVideoView = (RecyclerView) findViewById(R.id.my_video_view);
        Intent intent=getIntent();
        String userName = intent.getStringExtra("userName");
        userNameView.setText(userName);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        myVideoView.setLayoutManager(layoutManager);
        initData(userName);
    }

    private void initData(String userName) {
        new Thread(() -> {
            itemList = Network.dataGetFromRemote(null);

            if (itemList != null && !itemList.isEmpty()){
                new Handler(getMainLooper()).post(() -> {
                    itemList.removeIf(item-> !item.getUserName().equals(userName));
                    Log.d("MyList",String.valueOf(itemList.size()));
                    myVideoView.setAdapter(new UserVideoAdapter(itemList,this));
                });
            }
        }).start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String curUserName = data.getStringExtra("userName");
        userNameView.setText(curUserName);
        initData(curUserName);
    }
}