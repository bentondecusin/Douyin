package io.bcyl.douyin.Fragment.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.bcyl.douyin.Fragment.User.UserFragment;
import io.bcyl.douyin.HomeActivity;
import io.bcyl.douyin.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText userNameEdit = findViewById(R.id.user_login);
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEdit.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("userName", userName);

                SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("logged", true);
                editor.putString("userName", userName);
                editor.apply();

                HomeActivity.logged = true;
                setResult(UserFragment.LOGIN_CODE, intent);
                finish();
            }
        });
    }
}