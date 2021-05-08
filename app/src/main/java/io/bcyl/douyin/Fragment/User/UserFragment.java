package io.bcyl.douyin.Fragment.User;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
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

import io.bcyl.douyin.HomeActivity;
import io.bcyl.douyin.Utils.VideoInfo;
import io.bcyl.douyin.R;
import io.bcyl.douyin.Utils.Network;

import static android.os.Looper.getMainLooper;

public class UserFragment extends Fragment {
    public static final int LOGIN_CODE = 1;
    private List<VideoInfo> itemList = new ArrayList<>();
    private ImageView headImageView;
    private RecyclerView myVideoView;
    private TextView userNameView;
    private Button loginButton;
    private String curUserName;
    private boolean logged;
    private View.OnClickListener loggedListener, unLoggedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View userFragmentView = inflater.inflate(R.layout.fragment_user, container, false);
        headImageView = userFragmentView.findViewById(R.id.head_image);
        userNameView = userFragmentView.findViewById(R.id.user_name_view);
        myVideoView = (RecyclerView) userFragmentView.findViewById(R.id.my_video_view);

        unLoggedListener = v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_CODE);
        };

        loggedListener = v -> {
            HomeActivity.logged = false;
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            reset();
        };

        loginButton = (Button) userFragmentView.findViewById(R.id.begin_login_button);
        initView();

        return userFragmentView;
    }

    private void initView() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        logged = preferences.getBoolean("logged", false);
        setLoginButton(logged);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        myVideoView.setLayoutManager(layoutManager);

        curUserName = preferences.getString("userName", getString(R.string.not_login));
        Log.d("MyUserName", curUserName);

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (logged) {
            userNameView.setText(curUserName);
            Log.d("MyGET", String.valueOf(itemList.size()));
            new Thread(() -> {
                itemList = Network.dataGetFromRemote(null);

                if (itemList != null && !itemList.isEmpty()) {
                    new Handler(getMainLooper()).post(() -> {
                        itemList.removeIf(item -> !item.getUserName().equals(curUserName));
                        Log.d("MyList", String.valueOf(itemList.size()));
                        myVideoView.setAdapter(new UserVideoAdapter(itemList, getContext()));
                    });
                }
            }).start();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String curUserName = data.getStringExtra("userName");
        userNameView.setText(curUserName);
        if (resultCode == LOGIN_CODE) {
            logged = true;
            setLoginButton(true);
            initData();
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("确定要退出登录吗？");
        dialog.setCancelable(true);

        //确定按钮的点击事件
        dialog.setPositiveButton("OK", (dialog12, which) -> {
            setLoginButton(false);
            logged=false;
            userNameView.setText(getString(R.string.not_login));
            headImageView.setImageResource(R.mipmap.tiktok_logo);
            itemList.clear();
            myVideoView.setAdapter(new UserVideoAdapter(itemList, getContext()));
        });
        //取消按钮的点击事件
        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
        });
        dialog.show();

    }
}