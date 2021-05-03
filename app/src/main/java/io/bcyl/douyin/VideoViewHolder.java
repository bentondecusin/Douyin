package io.bcyl.douyin;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoViewHolder extends RecyclerView.ViewHolder{
    private TextView usrName;
    private TextView vidTitle;
    private String src;
    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        usrName   = itemView.findViewById(R.id.vidTitle);
        vidTitle  = itemView.findViewById(R.id.usrName);
    }

}
