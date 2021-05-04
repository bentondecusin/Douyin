package io.bcyl.douyin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    List<String[]> srcList;
    Context context;
    private RecyclerView recyclerView;
    public VideoAdapter(ArrayList<String[]> list, Context context) {
        this.srcList = list;
        this.context = context;
    }


    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_video_view, viewGroup, false);
        VideoViewHolder videoViewHolder = new VideoViewHolder(v);
        videoViewHolder.setAdapter(this);
        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, int i) {
        videoViewHolder.url = srcList.get(i)[0];
        videoViewHolder.usrName.setText(srcList.get(i)[1]);
        videoViewHolder.vidTitle.setText(srcList.get(i)[2]);
        videoViewHolder.initializePlayer();;
    }

    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.getPlayer().play();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.getPlayer().pause();
        holder.getPlayer().seekTo(0);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    public void setRecyclerView(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }


    @Override
    public int getItemCount() {
        return srcList.size();
    }



}
