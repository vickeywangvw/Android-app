package com.example.txy.tm.Adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.txy.tm.HelpClass.TomatoClk;
import com.example.txy.tm.R;


import java.util.List;

/**
 * Created by TXY on 2019/3/4.
 */

public class ClockAdapter extends RecyclerView.Adapter<ClockAdapter.MainContentViewHolder> {

    /**
     * Item是否被选中监听
     */
    private OnButtonClickListener mOnButtonClickListener;
    /**
     * Item点击监听
     */
    private OnItemClickListener mItemOnClickListener;

    /**
     * 数据
     */
    private List<TomatoClk> TomatoClks = null;

    /**
     * Item拖拽滑动帮助
     */
    private ItemTouchHelper itemTouchHelper;

    public ClockAdapter() {
    }

    public ClockAdapter(List<TomatoClk> clks) {
        this.TomatoClks = clks;
    }

    public void notifyDataSetChanged(List<TomatoClk> clks) {
        this.TomatoClks = clks;
        super.notifyDataSetChanged();
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemOnClickListener = onItemClickListener;
    }

    public void setOnButtonClickListener(OnButtonClickListener mOnButtonClickListener) {
        this.mOnButtonClickListener = mOnButtonClickListener;
    }

    @Override
    public ClockAdapter.MainContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.clock_adpt, parent, false);
        view.getLayoutParams().height = (int)dip2px(70);
        return new MainContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainContentViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return TomatoClks == null ? 0 : TomatoClks.size();
    }

    public TomatoClk getData(int position) {
        return TomatoClks.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnButtonClickListener {
        void onItemClick(View view, int position);
    }


    class MainContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        /**
         * 事件和重要性
         */
        private TextView clock;
        private TextView start;
        private ImageView back;
        private TextView infor;
        /**
         * 触摸就可以拖拽
         */
        private ImageView mIvTouch;




        public MainContentViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            back=(ImageView) itemView.findViewById(R.id.imageView_clock);
            clock = (TextView) itemView.findViewById(R.id.tv_clk);
            start = (TextView) itemView.findViewById(R.id.btn_start_clk);
            infor= (TextView) itemView.findViewById(R.id.tv_clk_mr);
            mIvTouch = (ImageView) itemView.findViewById(R.id.imageView_rv);

            //mIvTouch.setOnTouchListener(this);
            start.setOnClickListener(this);
        }

        /**
         * 给这个Item设置数据
         */
        public void setData() {
            TomatoClk Info = getData(getAdapterPosition());
            clock.setText(Info.getName());
            infor.setText(Info.getTime()+"分钟 "+Info.getNum()+"时段");
            int n=Info.getback();
            Log.v("back","back"+n);
            Bitmap image=null;
            switch (n){
                case 1:image = BitmapFactory.decodeResource(itemView.getResources(), R.mipmap.art_card1);break;
                case 2:image = BitmapFactory.decodeResource(itemView.getResources(), R.mipmap.art_card2);break;
                case 3:image = BitmapFactory.decodeResource(itemView.getResources(), R.mipmap.art_card3);break;
                case 4:image = BitmapFactory.decodeResource(itemView.getResources(), R.mipmap.art_card4);break;
                case 5:image = BitmapFactory.decodeResource(itemView.getResources(), R.mipmap.art_card5);break;
                case 6:image = BitmapFactory.decodeResource(itemView.getResources(), R.mipmap.art_card6);break;
                case 7:image = BitmapFactory.decodeResource(itemView.getResources(), R.mipmap.art_card7);break;
                case 8:image = BitmapFactory.decodeResource(itemView.getResources(), R.mipmap.art_card8);break;

            }
            back.setImageDrawable( rectRoundBitmap(image));
        }


        private RoundedBitmapDrawable rectRoundBitmap(Bitmap bitmap){
            //创建RoundedBitmapDrawable对象
            RoundedBitmapDrawable roundImg = RoundedBitmapDrawableFactory.create(itemView.getResources(), bitmap);
            //抗锯齿
            roundImg.setAntiAlias(true);
            //设置圆角半径
            roundImg.setCornerRadius((int)dip2px(5));
            return roundImg;
        }

        @Override
        public void onClick(View view) {
            if (view == itemView && itemTouchHelper != null) {
                mItemOnClickListener.onItemClick(view, getAdapterPosition());
            } else if (view == start && itemTouchHelper != null) {
                mOnButtonClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (view == mIvTouch)
                itemTouchHelper.startDrag(this);
            return false;
        }

    }
    public static float dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }
}
