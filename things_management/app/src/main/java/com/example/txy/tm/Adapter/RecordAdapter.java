package com.example.txy.tm.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.txy.tm.CircleImageView;
import com.example.txy.tm.HelpClass.RecordItem;
import com.example.txy.tm.R;

import java.util.List;

import static com.example.txy.tm.SecondActivity.AddRecordActivity.isNumeric;

/**
 * Created by TXY on 2019/3/4.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MainContentViewHolder> {

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
    private List<RecordItem> RecordItems = null;

    /**
     * Item拖拽滑动帮助
     */
    private ItemTouchHelper itemTouchHelper;

    public RecordAdapter() {
    }

    public RecordAdapter(List<RecordItem> rds) {
        this.RecordItems = rds;
    }

    public void notifyDataSetChanged(List<RecordItem> rds) {
        this.RecordItems = rds;
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
    public RecordAdapter.MainContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.record_adpt, null, false));
    }

    @Override
    public void onBindViewHolder(MainContentViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return RecordItems == null ? 0 : RecordItems.size();
    }

    public RecordItem getData(int position) {
        return RecordItems.get(position);
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
        private TextView record;
        private CircleImageView play;
        /**
         * 触摸就可以拖拽
         */
        private ImageView mIvTouch;


        public MainContentViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            record = (TextView) itemView.findViewById(R.id.tv_rd);
            play = (CircleImageView) itemView.findViewById(R.id.btn_play);
            //mIvTouch = (ImageView) itemView.findViewById(R.id.imageView_rv);
            //mIvTouch.setOnTouchListener(this);
            play.setOnClickListener(this);

        }

        /**
         * 给这个Item设置数据
         */
        public void setData() {
            RecordItem Info = getData(getAdapterPosition());
            record.setText(Info.getName());
            int n=1;
            if(isNumeric(Info.getBack())){
                n=Integer.parseInt(Info.getBack());;
                switch (n){
                    case 1:play.setImageResource(R.mipmap.headcard1);break;
                    case 2:play.setImageResource(R.mipmap.headcard2);break;
                    case 3:play.setImageResource(R.mipmap.headcard3);break;
                    case 4:play.setImageResource(R.mipmap.headcard4);break;
                    case 5:play.setImageResource(R.mipmap.headcard5);break;
                    case 6:play.setImageResource(R.mipmap.headcard6);break;
                }
            }else {
                Bitmap bitmap= BitmapFactory.decodeFile(Info.getBack());
                play.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onClick(View view) {
            if (view == itemView && itemTouchHelper != null) {
                mItemOnClickListener.onItemClick(view, getAdapterPosition());
            } else if (view == play && itemTouchHelper != null) {
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
}
