package com.example.txy.tm.DataBase;

import com.example.txy.tm.HelpClass.ThingInfo;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TXY on 2019/3/1.
 */

public class DB_Thing {
    public static long StaticId = 0;
    public static List<ThingInfo> tempList = new ArrayList<>();
    private static final String TAG = "DB_Thing";

    //连接
    public static void connect() {
        Connector.getDatabase();
    }

    //添加
    public static boolean add(ThingInfo thingInfo){
        thingInfo.setId(StaticId++);
        return thingInfo.save();
    }

    //更新操作
    public static boolean update(long id){
        return true;
    }

    //删除
    public static boolean deleteone(long id){
        DataSupport.delete(ThingInfo.class, id);
        return true;
    }

    public static boolean deleteall(){
        DataSupport.deleteAll(ThingInfo.class);
        return true;
    }

    //查询
    public static ThingInfo find(long id){
        return DataSupport.find(ThingInfo.class,id);
    }

    //查询全部
    public static List<ThingInfo> findAll(){
        return DataSupport.findAll(ThingInfo.class);
    }

    //查询今天且未完成的记录
    public static List<ThingInfo> findtoday(String today){
        return DataSupport.where("date=? and isdone=?" ,today,"0").find(ThingInfo.class);
    }

    //查询已完成的且为一次性任务记录
    public static List<ThingInfo> findTdone(){
        return DataSupport.where("isdone=? and repeat=?","1","1").find(ThingInfo.class);
    }

    //查询每日任务
    public static List<ThingInfo> findeveryday(){
        return DataSupport.where("repeat=?","2").find(ThingInfo.class);
    }

    //查询已完成的每日任务
    public static List<ThingInfo> findEdone(){
        return DataSupport.where("isdone=? and repeat=?","1","2").find(ThingInfo.class);
    }
}
