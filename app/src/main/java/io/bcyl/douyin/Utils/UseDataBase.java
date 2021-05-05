package io.bcyl.douyin.Utils;

import android.util.Log;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.function.Consumer;


public class UseDataBase {
    private MongoDatabase database;
    private boolean connectFlag;

    public boolean isConnected() {
        return connectFlag;
    }


    public UseDataBase() {

        String connectionString= "mongodb+srv://dev:CS175@cluster0.wwqrs.mongodb.net/test";
        try {
            MongoClient mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase("CS175");
            connectFlag=true;
        } catch (Exception e) {
            database=null;
            Log.e("MyDB", "Connect Error");
            connectFlag=false;
        }
    }


    public FindIterable<Document> query(String collectionName, Bson filters){
        MongoCollection<Document> collection=database.getCollection(collectionName);
        System.out.println(connectFlag);
        if(connectFlag){
            return collection.find(filters);
        }
        return null;
    }

}
