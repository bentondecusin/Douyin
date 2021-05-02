package io.bcyl.douyin.Fragment.User;

public class VideoItem {

    private final String videoUrl;
    private final String text;

    public VideoItem(String videoUrl, String text) {
        this.videoUrl = videoUrl;
        this.text=text;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getText() {
        return text;
    }
}
