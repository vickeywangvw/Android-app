package com.example.txy.tm.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.txy.tm.R;
import com.example.txy.tm.HelpClass.ThingInfo;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.example.txy.tm.CountDownProgressBar.dip2px;

/**
 * Created by TXY on 2019/3/1.
 */

public class ThingAdapter extends RecyclerView.Adapter<ThingAdapter.MainContentViewHolder>  {
    /**
     * Item是否被选中监听
     */
    private OnCheckedChangeListener mOnCheckedChangeListener;
    /**
     * Item点击监听
     */
    private OnItemClickListener mItemOnClickListener;
    /**
     * 数据
     */
    private List<ThingInfo> thinginfos = null;

    /**
     * Item拖拽滑动帮助
     */
    private ItemTouchHelper itemTouchHelper;

    public ThingAdapter() {
    }

    public ThingAdapter(List<ThingInfo> infos) {
        this.thinginfos = infos;
    }

    public void notifyDataSetChanged(List<ThingInfo> Infos) {
        this.thinginfos = Infos;
        super.notifyDataSetChanged();
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemOnClickListener = onItemClickListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    @Override
    public ThingAdapter.MainContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.thing_adpt, parent, false);
        view.getLayoutParams().height = (int)dip2px(45);
        return new MainContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainContentViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return thinginfos == null ? 0 : thinginfos.size();
    }

    public ThingInfo getData(int position) {
        return thinginfos.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnCheckedChangeListener {
        void onItemCheckedChange(CompoundButton view, int position, boolean checked);
    }

    class MainContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        /**
         * 事件和重要性
         */
        private TextView thing, time,date;
        /**
         * 触摸就可以拖拽
         */
        private ImageView mIvTouch;
        /**
         * 是否选中
         */
        private CheckBox mCbCheck;

        public MainContentViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            thing = (TextView) itemView.findViewById(R.id.whatthings);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            mIvTouch = (ImageView) itemView.findViewById(R.id.imageView_rv);
            mCbCheck = (CheckBox) itemView.findViewById(R.id.checkbox_rv);
            mCbCheck.setOnClickListener(this);
            mIvTouch.setOnTouchListener(this);
        }

        /**
         * 给这个Item设置数据
         */
        public void setData() {
            ThingInfo Info = getData(getAdapterPosition());
            thing.setText(Info.getWhatthing());
            time.setText(Info.getTime());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String current = format.format(new java.util.Date());
            if(current.equals(Info.getDate())) {
                date.setText("今日");
            }else{
                date.setText(Info.getDate().substring(5));
            }
            mCbCheck.setChecked(Info.isDone());
        }

        @Override
        public void onClick(View view) {
            if (view == itemView && itemTouchHelper != null) {
                mItemOnClickListener.onItemClick(view, getAdapterPosition());
            } else if (view == mCbCheck && mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onItemCheckedChange(mCbCheck, getAdapterPosition(), mCbCheck.isChecked());
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (view == mIvTouch)
                itemTouchHelper.startDrag(this);
            return false;
        }

    }
}
