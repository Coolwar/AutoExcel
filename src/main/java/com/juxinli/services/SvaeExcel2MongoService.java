package com.juxinli.services;

import com.juxinli.response.Msg;
import com.juxinli.tools.MongoConn;
import com.juxinli.tools.Utils;
import com.mongodb.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/7.
 */
@Service
public class SvaeExcel2MongoService {

    private Logger logger = Logger.getLogger(this.getClass());

    public String saveExcel2Mongo(List<DBObject> list) {

        logger.info("开始存储数据：" + new Date());

        DB conn = MongoConn.conn("localhost", 27017, "capture_std");
        if (conn == null)
            return new Msg("msg", "mongo连接异常").toJson();
        DBCollection dbcol = conn.getCollection("others_std");

        int inserCount = 0;
        int updateCount = 0;

        for (DBObject dbObject : list) {

            Object name = dbObject.get("name");
            Object id_card = dbObject.get("id_card");
            DBObject one = dbcol.findOne(new BasicDBObject("name", name).append("id_card", id_card));
            if (one == null) {
                dbObject.put("process_status", 0);
                dbObject.put("update_time", System.currentTimeMillis());
                dbObject.put("create_time", System.currentTimeMillis());
                dbcol.insert(dbObject);
                inserCount++;
            } else {
                update(one, dbObject, dbcol);
                updateCount++;
            }
        }

        return new Msg("msg", "成功导入数据").append("插入数据", inserCount).append("更新数据", updateCount).toJson();
    }

    private void update(DBObject cur, DBObject fresh, DBCollection dbcol) {
        fresh.removeField("_id");
        String curID = (String) cur.get("id_card");
        String freshID = (String) fresh.get("id_card");
        if (Utils.idCardSimilarity(curID, freshID)) {
            fresh.put("id_card", Utils.starMerge(curID, freshID));
            fresh.put("sources",
                    Utils.listMerge((BasicDBList) cur.get("sources"),
                            (BasicDBList) fresh.get("sources"))
            );
            fresh.put("categories",
                    Utils.listMerge((BasicDBList) cur.get("categories"),
                            (BasicDBList) fresh.get("categories"))
            );
            fresh.put("others",
                    Utils.mapMerge((BasicDBObject) cur.get("others"),
                            (BasicDBObject) fresh.get("others"))
            );
            fresh.put("mobiles",
                    Utils.mobileMerge((BasicDBList) cur.get("mobiles"),
                            (BasicDBList) fresh.get("mobiles"))
            );
            fresh.put("update_time", System.currentTimeMillis());
            fresh.put("process_status", 0);
            dbcol.update(new BasicDBObject("_id", cur.get("_id")),
                    new BasicDBObject("$set", fresh));
        }
    }

}
