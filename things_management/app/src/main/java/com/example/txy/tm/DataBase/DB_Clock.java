package com.example.txy.tm.DataBase;

import com.example.txy.tm.HelpClass.TomatoClk;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TXY on 2019/3/4.
 */

public class DB_Clock {
    public static long StaticId = 0;
    public static List<TomatoClk> tempList = new ArrayList<>();
    private static final String TAG = "DB_Clock";

    //连接
    public static void connect() {
        Connector.getDatabase();
    }

    //添加
    public static boolean add(TomatoClk TomatoClk){
        TomatoClk.setId(StaticId++);
        return TomatoClk.save();
    }

    //更新操作
    public static boolean update(long id){
        return true;
    }

    //删除
    public static boolean deleteone(long id){
        DataSupport.delete(TomatoClk.class, id);
        return true;
    }

    public static boolean deleteall(){
        DataSupport.deleteAll(TomatoClk.class);
        return true;
    }

    //查询
    public static TomatoClk find(long id){
        return DataSupport.find(TomatoClk.class,id);
    }

    //查询全部
    public static List<TomatoClk> findAll(){
        return DataSupport.findAll(TomatoClk.class);
    }

}
