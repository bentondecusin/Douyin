package io.bcyl.douyin.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import io.bcyl.douyin.R;
import io.bcyl.douyin.Model.VideoInfo;
import io.bcyl.douyin.Model.VideoInfoList;

public class Network {

    public static List<VideoInfo> dataGetFromRemote(String usrId){

        final String BASE_URL = "https://api-sjtu-camp-2021.bytedance.com/homework/invoke/video?student_id=t1";
        String urlStr = BASE_URL;
        if (usrId != null) urlStr = BASE_URL + usrId;
        List<VideoInfo> vidList = null;
        try{
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("GET");
            int status = conn.getResponseCode();
            if (status == 200){
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                VideoInfoList vl = new Gson().fromJson(reader, new TypeToken<VideoInfoList>(){
                }.getType());
                vidList = vl.feeds;
                reader.close();
                in.close();
            }
            else throw new Exception(Integer.toString(status));
        } catch (Exception e){
            e.printStackTrace();
        }
        return vidList;
    }
}
