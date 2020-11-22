package com.example.txy.tm.DataBase;

import com.example.txy.tm.HelpClass.RecordItem;
import com.example.txy.tm.HelpClass.RecordItem;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TXY on 2019/3/13.
 */

public class DB_Record {
    
        public static long StaticId = 0;
        public static List<RecordItem> tempList = new ArrayList<>();
        private static final String TAG = "DB_Clock";

        //连接
        public static void connect() {
            Connector.getDatabase();
        }

        //添加
        public static boolean add(RecordItem RecordItem){
            RecordItem.setId(StaticId++);
            return RecordItem.save();
        }

        //更新操作
        public static boolean update(long id){
            return true;
        }

        //删除
        public static boolean deleteone(long id){
            DataSupport.delete(RecordItem.class, id);
            return true;
        }

        public static boolean deleteall(){
            DataSupport.deleteAll(RecordItem.class);
            return true;
        }

        //查询
        public static RecordItem find(long id){
            return DataSupport.find(RecordItem.class,id);
        }

        //查询全部
        public static List<RecordItem> findAll(){
            return DataSupport.findAll(RecordItem.class);
        }
}
