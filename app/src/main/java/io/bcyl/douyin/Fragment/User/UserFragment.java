package io.bcyl.douyin.Fragment.User;

import android.content.Intent;
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

import io.bcyl.douyin.InfoEditActivity;
import io.bcyl.douyin.R;
import io.bcyl.douyin.Utils.VideoItem;

public class UserFragment extends Fragment {
    public static final int REQUEST_EDIT_CODE=0;
    public static final int REQUEST_UPDATE_USER=1;
    private final List<VideoItem> itemList=new ArrayList<>();
    private ImageView bgImageView,headImageView;
    private TextView userNameView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View userFragmentView=inflater.inflate(R.layout.fragment_user, container, false);
        initData();
        initView(userFragmentView);
        Button infoEditButton = (Button) userFragmentView.findViewById(R.id.edit_info_button);
        Button friendAddButton = (Button) userFragmentView.findViewById(R.id.add_friend_button);

        infoEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), InfoEditActivity.class);
                // TODO 传入图片
                //intent.putExtra("headerImage","headerUrl");
                intent.putExtra("userName",userNameView.getText());
                startActivityForResult(intent,REQUEST_EDIT_CODE);
            }
        });

        friendAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 关注
            }
        });
        return userFragmentView;
    }

    private void initView(View view){
        bgImageView=view.findViewById(R.id.user_bgImage);
        bgImageView.setImageResource(R.drawable.ic_tiktok_logo);

        headImageView=view.findViewById(R.id.head_image);
        headImageView.setImageResource(R.mipmap.ic_launcher);

        userNameView=view.findViewById(R.id.user_name_view);
        userNameView.setText("用户名写在这");

        RecyclerView myVideoView = (RecyclerView) view.findViewById(R.id.my_video_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        myVideoView.setLayoutManager(layoutManager);
        myVideoView.setAdapter(new UserVideoAdapter(itemList));
    }

    private void initData(){
        for (int i=0;i<20;i++){
            VideoItem item = new VideoItem("Url"+i,"myText"+i,"user"+i);
            itemList.add(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String curUserName=data.getStringExtra("userName");
        userNameView.setText(curUserName);
        //String curHeader=data.getStringExtra("headerUrl");
        //headImageView.setImageBitmap();

    }
}