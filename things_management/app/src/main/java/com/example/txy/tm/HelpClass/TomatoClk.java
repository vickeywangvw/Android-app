package com.example.txy.tm.HelpClass;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by TXY on 2019/3/3.
 */

public class TomatoClk extends DataSupport implements Serializable {
    private long id;
    private String name;
    private int num;
    private int time;
    private int interval;
    private int n;

    public TomatoClk(){

    }

    public TomatoClk(String name,int num,int time,int interval,int n){
        this.name=name;
        this.num=num;
        this.time=time;
        this.interval=interval;
        this.n=n;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getback() {
        return n;
    }

    public void setback(int n) {
        this.n = n;
    }


}
