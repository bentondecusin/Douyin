package io.bcyl.douyin.Utils;

public class UserInfoItem {
    private String headerUrl;
    private final String id;
    private String userName;

    public UserInfoItem(String headerUrl, String id, String userName) {
        this.headerUrl = headerUrl;
        this.id = id;
        this.userName = userName;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }
}
