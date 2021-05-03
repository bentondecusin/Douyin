package io.bcyl.douyin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    List<String[]> srcList;
    Context context;
    public VideoAdapter(ArrayList<String[]> list, Context context) {
        this.srcList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_video_view, viewGroup, false);
        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, int i) {
        videoViewHolder.url = srcList.get(i)[0];
        videoViewHolder.usrName.setText(srcList.get(i)[1]);
        videoViewHolder.vidTitle.setText(srcList.get(i)[2]);
        videoViewHolder.initializePlayer();;
    }

    @Override
    public int getItemCount() {
        return srcList.size();
    }



}
