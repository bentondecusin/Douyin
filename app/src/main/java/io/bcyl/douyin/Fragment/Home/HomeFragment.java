package io.bcyl.douyin.Fragment.Home;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import io.bcyl.douyin.R;
import io.bcyl.douyin.VideoAdapter;
import io.bcyl.douyin.VideoViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getName();
    private LinearLayoutManager layoutManager;
    private SnapHelper snapHelper;
    private static final String ARG_PARAM = "param";
    private View view;
    private View v;
    private int pos;


    /**
     * For now we are using sentinel data
     */
    ArrayList<String[]> srcList = new ArrayList<String[]>();
    RecyclerView mRecyclerView;
    VideoAdapter mAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * For now we are using sentinel data
         */
        String src1[]={getString(R.string.meme1),"Hugh Jaz 1","Shiba"};
        String src2[]={getString(R.string.meme2),"Hugh Jaz 2","Birdie"};
        String src3[]={getString(R.string.meme3),"Hugh Jaz 3","Horses in Walmart"};
        String src4[]={getString(R.string.meme4),"Hugh Jaz 4","Sad Dog"};
        String src5[]={getString(R.string.meme5),"Hugh Jaz 5","Omegle"};
        String src6[]={getString(R.string.meme6),"Hugh Jaz 6","Dank cat"};
        String src7[]={getString(R.string.meme7),"Hugh Jaz 7","Watch it!"};
        String src8[]={getString(R.string.meme8),"Hugh Jaz 8","War face"};

        srcList.add(src1);
        srcList.add(src8);
        srcList.add(src7);
        srcList.add(src4);
        srcList.add(src5);
        srcList.add(src2);
        srcList.add(src3);
        srcList.add(src6);
        srcList.add(src1);
        srcList.add(src2);
        srcList.add(src3);
        srcList.add(src4);
        srcList.add(src5);
        srcList.add(src6);
        srcList.add(src7);
    }

    public void onStart(){
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView = view.findViewById(R.id.rvVid);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new VideoAdapter(srcList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRecyclerView(mRecyclerView);


        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(0);
                if (viewHolder != null && viewHolder instanceof VideoViewHolder)
                    ((VideoViewHolder) viewHolder).getPlayer().play();
            }
        },200);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                v = snapHelper.findSnapView(layoutManager);
                pos = layoutManager.getPosition(v);
                RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(pos);
                if (viewHolder != null && viewHolder instanceof VideoViewHolder)
                    ((VideoViewHolder) viewHolder).getPlayer().play();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx/10, dy/10);
            }
        });
        return view;
    }

    public void pauseCurrentVideo(){
        v = snapHelper.findSnapView(layoutManager);
        pos = layoutManager.getPosition(v);
        RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(pos);
        ((VideoViewHolder) viewHolder).getPlayer().pause();
    }

    public void resumeCurrentVideo(){
        v = snapHelper.findSnapView(layoutManager);
        if (v != null) {
            pos = layoutManager.getPosition(v);
            RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(pos);
            ((VideoViewHolder) viewHolder).getPlayer().play();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"On resume");
        resumeCurrentVideo();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"On pause");
        pauseCurrentVideo();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"On stop");
        pauseCurrentVideo();
    }



}