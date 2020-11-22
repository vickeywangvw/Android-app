package com.example.txy.tm.HelpClass;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;
import org.litepal.crud.DataSupport;

/**
 * Created by TXY on 2019/3/1.
 */

public class ThingInfo extends DataSupport implements Serializable{

    private long id;
    private int repeat;
    private String whatthing;
    private String note;
    private boolean isdone;
    public String date;
    private String time;
    private Bitmap background;



    //构造函数
    public ThingInfo(){

    }

    public ThingInfo(String whatthing,String note,int repeat,boolean ischeck) {
        this.whatthing = whatthing;
        this.note = note;
        this.repeat = repeat;
        this.isdone = ischeck;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getWhatthing() {
        return whatthing;
    }

    public void setWhatthing(String whatthing) {
        this.whatthing = whatthing;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isDone() {
        return isdone;
    }

    public void setDone(boolean done) {
        this.isdone = done;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

}
