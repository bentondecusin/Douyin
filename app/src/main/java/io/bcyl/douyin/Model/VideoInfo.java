package io.bcyl.douyin.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class VideoInfo {
    @SerializedName("_id")
    private String Id;
    @SerializedName("student_id")
    private String studentId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("extra_value")
    private String extraValue;
    @SerializedName("video_url")
    private String videoRrl;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("image_w")
    private int imageW;
    @SerializedName("image_h")
    private int imageH;
    @SerializedName("createdAt")
    private Date createdAt;
    @SerializedName("updatedAt")
    private Date updatedAt;
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getId() {
        return Id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getStudentId() {
        return studentId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }
    public String getExtraValue() {
        return extraValue;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoRrl = videoUrl;
    }
    public String getVideoUrl() {
        return videoRrl;
    }
}