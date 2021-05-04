package io.bcyl.douyin.Fragment.User;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.bcyl.douyin.R;
import io.bcyl.douyin.Utils.VideoItem;

public class UserVideoAdapter extends RecyclerView.Adapter<UserVideoAdapter.UserVideoHolder> {

    @NonNull
    private final List<VideoItem> myVideoItems;

    public UserVideoAdapter(@NonNull List<VideoItem> myVideoItems) {
        this.myVideoItems = myVideoItems;
    }

    static class UserVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        VideoView videoView;
        TextView videoTitleView;

        public UserVideoHolder(@NonNull View itemView) {
            super(itemView);
            videoView = (VideoView) itemView.findViewById(R.id.item_video);
            videoTitleView = (TextView) itemView.findViewById(R.id.item_title);
        }

        @Override
        public void onClick(View v) {
            //  TODO 点击播放自己的视频
            Log.d("MyVideo", "Item Clicked");
        }
    }

    @NonNull
    @Override
    public UserVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
        return new UserVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserVideoHolder holder, int position) {
        VideoItem item =myVideoItems.get(position);
        holder.videoTitleView.setText(item.getTitle());

        //holder.videoView.setVideoPath(item.getVideoUrl()); 当前没有video
    }

    @Override
    public int getItemCount() {
        return myVideoItems.size();
    }


}
