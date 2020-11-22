package com.example.txy.tm.FirstActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.icu.text.AlphabeticIndex;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txy.tm.Adapter.ClockAdapter;
import com.example.txy.tm.Adapter.RecordAdapter;
import com.example.txy.tm.DataBase.DB_Record;
import com.example.txy.tm.HelpClass.RecordItem;
import com.example.txy.tm.HelpClass.ThingInfo;
import com.example.txy.tm.R;
import com.example.txy.tm.Recorder.PlaybackDialogFragment;
import com.example.txy.tm.Recorder.RecordAudioDialogFragment;
import com.example.txy.tm.SecondActivity.AddRecordActivity;
import com.example.txy.tm.SecondActivity.TmtClockShowActivity;
import com.example.txy.tm.TouchHelper.MyItemTochHelper;
import com.example.txy.tm.TouchHelper.MyItemTouchHelpCallback;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FightActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //全局变量
    private GoogleApiClient client;
    private RecyclerView recyclerView;
    public List<RecordItem> RecordItemList = new ArrayList<>();
    private int updatePosition;
    private RecordAdapter mainAdapter;
    private MyItemTochHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView= (TextView) findViewById(R.id.cut);
        textView.setVisibility(View.GONE);
        RecyclerView rv= (RecyclerView) findViewById(R.id.recyclerview_main2);
        rv.setVisibility(View.GONE);

        //监听器
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //添加事件按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FightActivity.this, AddRecordActivity.class);
                RecordItem temp = null;
                intent.putExtra("Record", temp);
                startActivityForResult(intent, 1);
            }
        });

        //获得数据库
        DB_Record.connect();

        //recyclerView 相关初始化
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new RecordAdapter(RecordItemList);
        mainAdapter.setOnItemClickListener(onItemClickListener);
        mainAdapter.setOnButtonClickListener(onButtonClickListener);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 10, 0, 10);//设置itemView中内容相对边框左，上，右，下距离
            }
        });

        // 把ItemTouchHelper和itemTouchHelper绑定
        itemTouchHelper = new MyItemTochHelper(onItemTouchCallbackListener);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mainAdapter.setItemTouchHelper(itemTouchHelper);

        itemTouchHelper.setDragEnable(true);
        itemTouchHelper.setSwipeEnable(true);

        //初始显示今日任务
        RecordItemList.clear();
        RecordItemList = DB_Record.findAll();
        mainAdapter.notifyDataSetChanged(RecordItemList);
    }

    /**
     * 显示列表点击监听操作
     * onSwiped 删除操作
     * onMove 交换操作
     * */
    private MyItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener
            = new MyItemTouchHelpCallback.OnItemTouchCallbackListener(){
        //删除
        @Override
        public void onSwiped(int adapterPosition) {
            if(RecordItemList != null){
                DB_Record.deleteone(RecordItemList.get(adapterPosition).getId());
                RecordItemList.remove(adapterPosition);
                mainAdapter.notifyItemRemoved(adapterPosition);
            }
        }

        //交换
        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            if(RecordItemList != null){
                // 更换数据源中的数据Item的位置
                Collections.swap(RecordItemList, srcPosition,targetPosition);

                // 更新UI中的Item的位置，主要是给用户看到交互效果
                mainAdapter.notifyItemMoved(srcPosition, targetPosition);
                return true;
            }
            return false;
        }
    };

    //单击Item事件
    private RecordAdapter.OnItemClickListener onItemClickListener = new RecordAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            RecordItem temp = RecordItemList.get(position);
            updatePosition = position;
            Intent intent = new Intent(FightActivity.this, AddRecordActivity.class);
            intent.putExtra("Record",temp);
            startActivityForResult(intent,2);

        }
    };

    //单击Button事件
    private RecordAdapter.OnButtonClickListener onButtonClickListener = new RecordAdapter.OnButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            RecordItem rd_play = RecordItemList.get(position);
            final PlaybackDialogFragment fragmentPlay = PlaybackDialogFragment.newInstance(rd_play);
            fragmentPlay.show(getSupportFragmentManager(), PlaybackDialogFragment.class.getSimpleName());
            fragmentPlay.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                @Override
                public void onCancel() {
                    fragmentPlay.dismiss();
                }
            });
        }
    };


    //左侧边栏菜单点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_todo) {
            Intent intent_todo = new Intent(FightActivity.this, MainActivity.class);
            startActivity(intent_todo);

        } else if (id == R.id.nav_methods) {
            Intent intent_tomato = new Intent(FightActivity.this, TomatoActivity.class);
            startActivity(intent_tomato);

        } else if (id == R.id.nav_fight) {
            Intent intent = new Intent(FightActivity.this, FightActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //添加番茄钟信息传递
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(data==null)
            {
                Toast.makeText(FightActivity.this,"暂无添加",Toast.LENGTH_SHORT).show();
                return ;
            }
            RecordItem newRecordItem = (RecordItem) data.getSerializableExtra("addRecord");
            DB_Record.add(newRecordItem);

            RecordItemList.add(newRecordItem);
            mainAdapter.notifyDataSetChanged(RecordItemList);
        }
        else if(requestCode==2){
            if(data==null) {
                Toast.makeText(FightActivity.this,"暂无添加",Toast.LENGTH_SHORT).show();
                return ;
            }
            RecordItem newRecordItem = (RecordItem) data.getSerializableExtra("addRecord");
            if(newRecordItem==null) {
                Toast.makeText(FightActivity.this,"暂无添加",Toast.LENGTH_SHORT).show();
                return ;
            }
            RecordItem updateinfo = DB_Record.find(newRecordItem.getId());

            updateinfo.setName(newRecordItem.getName());
            Log.v("你的名字在哪里","null2"+newRecordItem.getName());
            updateinfo.setFilePath(newRecordItem.getFilePath());
            updateinfo.setTime(newRecordItem.getTime());
            updateinfo.setLength(newRecordItem.getLength());
            updateinfo.setBack(newRecordItem.getBack());
            updateinfo.save();

            RecordItemList.set(updatePosition,updateinfo);
            mainAdapter.notifyDataSetChanged(RecordItemList);
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }



}
