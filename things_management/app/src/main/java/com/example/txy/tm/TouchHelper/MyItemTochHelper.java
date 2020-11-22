package com.example.txy.tm.TouchHelper;

/**
 * Created by TXY on 2019/3/1.
 */

public class MyItemTochHelper extends YolandaItemTouchHelper{
    private MyItemTouchHelpCallback itemTouchHelpCallback;

    public MyItemTochHelper(MyItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener) {
        super(new MyItemTouchHelpCallback(onItemTouchCallbackListener));
        itemTouchHelpCallback = (MyItemTouchHelpCallback)getCallback();
    }

    /**
     * 设置是否可以被拖拽
     *
     * @param canDrag 是true，否false
     */
    public void setDragEnable(boolean canDrag) {
        itemTouchHelpCallback.setDragEnable(canDrag);
    }

    /**
     * 设置是否可以被滑动
     *
     * @param canSwipe 是true，否false
     */
    public void setSwipeEnable(boolean canSwipe) {
        itemTouchHelpCallback.setSwipeEnable(canSwipe);
    }
}
