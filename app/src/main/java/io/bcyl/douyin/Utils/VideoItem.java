package io.bcyl.douyin.Utils;

public class VideoItem {

    private final String videoUrl;
    private final String userName;
    private final String title;


    public VideoItem(String videoUrl, String title,String userName) {
        this.videoUrl = videoUrl;
        this.title=title;
        this.userName=userName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return userName;
    }
}
