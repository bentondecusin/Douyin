package io.bcyl.douyin.Fragment.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.bcyl.douyin.R;
import io.bcyl.douyin.Utils.VideoItem;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    List<VideoItem> videoItemList;
    Context context;
    private RecyclerView recyclerView;
    public VideoAdapter(ArrayList<VideoItem> list, Context context) {
        this.videoItemList = list;
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
        videoViewHolder.url = videoItemList.get(i).getVideoUrl();
        videoViewHolder.usrName.setText(videoItemList.get(i).getUserName());
        videoViewHolder.vidTitle.setText(videoItemList.get(i).getTitle());
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
        return videoItemList.size();
    }



}
