package io.bcyl.douyin.Fragment.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.bcyl.douyin.HomeActivity;
import io.bcyl.douyin.LoginActivity;
import io.bcyl.douyin.Model.VideoInfo;
import io.bcyl.douyin.R;
import io.bcyl.douyin.Utils.Network;

public class UserFragment extends Fragment {
    public static final int LOGIN_CODE = 1;
    private List<VideoInfo> itemList = new ArrayList<>();
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


        loginButton = (Button) userFragmentView.findViewById(R.id.begin_login_button);

        initView(userFragmentView);

        return userFragmentView;
    }

    private void initView(View view) {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        boolean logged = preferences.getBoolean("logged", false);
        setLoginButton(logged);

        if (logged) {
            String curUserName = preferences.getString("userName", getString(R.string.not_login));
            userNameView.setText(curUserName);
            initData(curUserName);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        myVideoView.setLayoutManager(layoutManager);
    }

    private void initData(String userName) {
        itemList = Network.dataGetFromRemote(userName);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String curUserName = data.getStringExtra("userName");
        userNameView.setText(curUserName);
        if (resultCode == LOGIN_CODE) {
            setLoginButton(true);
            initData(curUserName);
        }
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

    private void reset() {
        setLoginButton(false);
        userNameView.setText(getString(R.string.not_login));
        headImageView.setImageResource(R.mipmap.tiktok_logo);
        itemList.clear();
        myVideoView.setAdapter(new UserVideoAdapter(itemList));
    }
}