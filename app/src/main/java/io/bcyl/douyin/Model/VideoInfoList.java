package io.bcyl.douyin.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoInfoList {
    @SerializedName("feeds")
    public List<VideoInfo> feeds;
    @SerializedName("success")
    public boolean success;
}

