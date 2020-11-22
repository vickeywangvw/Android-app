package com.example.txy.tm.HelpClass;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by TXY on 2019/3/5.
 */

public class RecordItem extends DataSupport implements Serializable {
    private long id;
    private String mName;
    private String mFilePath;
    private int mLength;
    private long mTime;
    private String imagePath;
    private String voicePath;

    public RecordItem() {
    }

    public RecordItem(Parcel in) {
        mName = in.readString();
        mFilePath = in.readString();
        id = in.readInt();
        mLength = in.readInt();
        mTime = in.readLong();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getBack() {
        return imagePath;
    }

    public void setBack(String imagePath) {
        this.imagePath = imagePath;
    }

    public static final Parcelable.Creator<RecordItem> CREATOR = new Parcelable.Creator<RecordItem>() {
        public RecordItem createFromParcel(Parcel in) {
            return new RecordItem(in);
        }

        public RecordItem[] newArray(int size) {
            return new RecordItem[size];
        }
    };


}

