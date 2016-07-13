package com.juxinli.tools;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/2.
 */
@Component
public class MongoConn {

    /**
     * mongodb数据库连接
     *
     * @param host     ip
     * @param port     端口号
     * @param database 库名
     * @return db
     */
    public static DB conn(String host, int port, String database) {
        DB db;
        try {
            MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), new MongoClientOptions.Builder()
                    .socketTimeout(300000)
                    .connectionsPerHost(500)
                    .threadsAllowedToBlockForConnectionMultiplier(500)
                    .socketKeepAlive(true)
                    .build()
            );
            db = mongoClient.getDB(database);
            return db;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

}
