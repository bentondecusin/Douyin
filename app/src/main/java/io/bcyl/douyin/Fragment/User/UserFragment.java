package io.bcyl.douyin.Fragment.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.bcyl.douyin.HomeActivity;
import io.bcyl.douyin.InfoEditActivity;
import io.bcyl.douyin.LoginActivity;
import io.bcyl.douyin.R;
import io.bcyl.douyin.Utils.VideoItem;

public class UserFragment extends Fragment {
    public static final int INFO_EDIT_CODE = 0;
    public static final int LOGIN_CODE = 1;
    private final List<VideoItem> itemList = new ArrayList<>();
    private ImageView headImageView;
    private RecyclerView myVideoView;
    private TextView userNameView;
    private Button loginButton;
    private View.OnClickListener loggedListener, unLoggedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View userFragmentView = inflater.inflate(R.layout.fragment_user, container, false);
        headImageView = userFragmentView.findViewById(R.id.head_image);
        userNameView = userFragmentView.findViewById(R.id.user_name_view);
        myVideoView = (RecyclerView) userFragmentView.findViewById(R.id.my_video_view);

        unLoggedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, LOGIN_CODE);
            }
        };

        loggedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.logged = false;
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                reset();
            }
        };

        Button infoEditButton = (Button) userFragmentView.findViewById(R.id.edit_info_button);
        loginButton = (Button) userFragmentView.findViewById(R.id.begin_login_button);

        initView(userFragmentView);
        infoEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InfoEditActivity.class);
                // TODO 传入图片
                //intent.putExtra("headerImage","headerUrl");
                intent.putExtra("userName", userNameView.getText().toString());
                startActivityForResult(intent, INFO_EDIT_CODE);
            }
        });

        return userFragmentView;
    }

    private void initView(View view) {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        boolean logged = preferences.getBoolean("logged", false);
        setLoginButton(logged);

        if (logged) {
            String curUserName=preferences.getString("userName", getString(R.string.not_login));
            userNameView.setText(curUserName);
            // TODO headImage form preferences
            //headImageView.setImageResource(R.mipmap.tiktok_logo);
            initData(curUserName);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        myVideoView.setLayoutManager(layoutManager);
    }

    private void initData(String userName) {
        for (int i = 0; i < 20; i++) {
            VideoItem item = new VideoItem("Url" + i, "myText" + i, "user" + i);
            itemList.add(item);
        }
        myVideoView.setAdapter(new UserVideoAdapter(itemList));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String curUserName = data.getStringExtra("userName");
        userNameView.setText(curUserName);

        // TODO add header
        if (resultCode == LOGIN_CODE) {
            setLoginButton(true);
            initData(curUserName);
        }
        //String curHeader=data.getStringExtra("headerUrl");
        //headImageView.setImageBitmap();
    }

    private void setLoginButton(boolean logged) {
        if (logged) {
            loginButton.setText("退出登录");
            loginButton.setOnClickListener(loggedListener);
        } else {
            loginButton.setText(R.string.login);
            loginButton.setOnClickListener(unLoggedListener);
        }
    }

    private void reset(){
        setLoginButton(false);
        userNameView.setText(getString(R.string.not_login));
        headImageView.setImageResource(R.mipmap.tiktok_logo);
        itemList.clear();
        myVideoView.setAdapter(new UserVideoAdapter(itemList));
    }
}