package io.bcyl.douyin.Model;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("result")
    public VideoInfo videoInfo;
    @SerializedName("success")
    public boolean success;
}
