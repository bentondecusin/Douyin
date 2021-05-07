package io.bcyl.douyin.Fragment.User;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.bcyl.douyin.Fragment.Home.HomeFragment;
import io.bcyl.douyin.MyVideoActivity;
import io.bcyl.douyin.Utils.VideoInfo;
import io.bcyl.douyin.R;

public class UserVideoAdapter extends RecyclerView.Adapter<UserVideoAdapter.UserVideoHolder> {

    @NonNull
    private final List<VideoInfo> videoInfoList;
    Context context;

    public UserVideoAdapter(@NonNull List<VideoInfo> myVideoInfos,Context context) {
        this.videoInfoList = myVideoInfos;
        this.context=context;
    }

    static class UserVideoHolder extends RecyclerView.ViewHolder {
        private final ImageView videoPreview;
        public VideoInfo videoInfo=null;

        public UserVideoHolder(@NonNull View itemView,Context context) {
            super(itemView);
            videoPreview = (ImageView) itemView.findViewById(R.id.item_preview);
        }
    }

    @NonNull
    @Override
    public UserVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
        return new UserVideoHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserVideoHolder holder, int position) {
        VideoInfo item = videoInfoList.get(position);
        holder.videoInfo=item;
        String userName=item.getUserName();
        String title=item.getExtraValue().split(",")[1];
        String comment=item.getExtraValue().split(",")[2];
        String imgUrl=item.getImageUrl();
        String videoUrl=item.getVideoUrl();

        Log.d("MyImgUrl",imgUrl);
        Glide.with(context)
                .load(imgUrl)
                .into(holder.videoPreview);

        holder.videoPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MyVideoActivity.class);
                intent.putExtra("url",videoUrl);
                intent.putExtra("userName",userName);
                intent.putExtra("title",title);
                intent.putExtra("comment",comment);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoInfoList.size();
    }

}
