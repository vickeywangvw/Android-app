package com.example.txy.tm.TouchHelper;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by TXY on 2019/3/1.
 */

public class YolandaItemTouchHelper extends ItemTouchHelper {
    public ItemTouchHelper.Callback mCallback = null;
    public YolandaItemTouchHelper(ItemTouchHelper.Callback callback) {
        super(callback);
        mCallback = callback;
    }
    public Callback getCallback(){
        return mCallback;

    }
}
