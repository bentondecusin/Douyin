package io.bcyl.douyin.Utils;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("result")
    public VideoInfo videoInfo;
    @SerializedName("success")
    public boolean success;
}
