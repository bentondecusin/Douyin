package io.bcyl.douyin.Utils;

public class UserInfoItem {
    private final String id;
    private final String userName;

    public UserInfoItem(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }
}
