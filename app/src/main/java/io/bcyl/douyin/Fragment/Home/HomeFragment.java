package io.bcyl.douyin.Fragment.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.bcyl.douyin.R;


public class HomeFragment extends Fragment {

    private PagerSnapHelper pagerSnapHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView main_view=view.findViewById(R.id.main_view);
        pagerSnapHelper=new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(main_view);



        return view;
    }
}