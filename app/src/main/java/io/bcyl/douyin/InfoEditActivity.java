package io.bcyl.douyin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import io.bcyl.douyin.Fragment.User.UserFragment;

public class InfoEditActivity extends AppCompatActivity {
    private ImageView headerView;
    private EditText userEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_edit);

        headerView = (ImageView) findViewById(R.id.head_edit_view);
        userEdit = (EditText) findViewById(R.id.user_edit);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 修改数据库
                //TODO 调用系统相册修改头像
            }
        });

        Button confirmEditButton = (Button) findViewById(R.id.confirm_edit_info_button);
        confirmEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 修改数据库
                // TODO 发送头像id
                String curUserName=userEdit.getText().toString();
                Intent intent =new Intent();
                intent.putExtra("userName",curUserName);
                //intent.putExtra("headerUrl",)
                setResult(UserFragment.REQUEST_UPDATE_USER,intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String curUserName = data.getStringExtra("userName");
        //String headerUri=savedInstanceState.getString("headerUrl");


        userEdit.setText(curUserName);

        // TODO 设置当前图片
        //headerView.setImage(headerUri);
    }
}