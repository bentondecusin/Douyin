package io.bcyl.douyin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import io.bcyl.douyin.Fragment.Add.AddFragment;
import io.bcyl.douyin.Fragment.Home.HomeFragment;
import io.bcyl.douyin.Fragment.User.UserFragment;

public class HomeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private Fragment mFragmentHome = new HomeFragment();
    private Fragment mFragmentAdd = new AddFragment();
    private Fragment mFragmentUser = new UserFragment();

    public static boolean logged;
    private BottomNavigationView mBottomNavigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences preferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        logged = preferences.getBoolean("logged", false);
        initView();
    }

    private void initView() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return mFragmentHome;
                    case 1:
                        return mFragmentAdd;
                    case 2:
                        return mFragmentUser;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });


        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                viewPager.setCurrentItem(item.getOrder());
                return true;
            }
        });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}