package com.example.txy.tm.FirstActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txy.tm.Adapter.ClockAdapter;
import com.example.txy.tm.DataBase.DB_Clock;
import com.example.txy.tm.DataBase.DB_Thing;
import com.example.txy.tm.HelpClass.ThingInfo;
import com.example.txy.tm.HelpClass.TomatoClk;
import com.example.txy.tm.HelpClass.TomatoClk;
import com.example.txy.tm.R;
import com.example.txy.tm.SecondActivity.AddClockActivity;
import com.example.txy.tm.SecondActivity.AddThingActivity;
import com.example.txy.tm.SecondActivity.TmtClockShowActivity;
import com.example.txy.tm.HelpClass.TomatoClk;
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

public class TomatoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //全局变量
    private GoogleApiClient client;
    private RecyclerView recyclerView;
    public List<TomatoClk> TomatoclkList = new ArrayList<>();
    private int updatePosition;
    private ClockAdapter mainAdapter;
    private MyItemTochHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView= (TextView) findViewById(R.id.cut);
        textView.setVisibility(View.GONE);
        RecyclerView rv= (RecyclerView) findViewById(R.id.recyclerview_main2);
        rv.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //添加番茄钟
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TomatoActivity.this, AddClockActivity.class);
                TomatoClk temp = null;
                intent.putExtra("setTomatoClk",temp);
                startActivityForResult(intent, 1);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

//获得数据库
        DB_Clock.connect();

        //recyclerView 相关初始化
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new ClockAdapter(TomatoclkList);
        mainAdapter.setOnItemClickListener(onItemClickListener);
        mainAdapter.setOnButtonClickListener(onButtonClickListener);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(4, 10, 4, 10);//设置itemView中内容相对边框左，上，右，下距离
            }
        });

        // 把ItemTouchHelper和itemTouchHelper绑定
        itemTouchHelper = new MyItemTochHelper(onItemTouchCallbackListener);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mainAdapter.setItemTouchHelper(itemTouchHelper);

        itemTouchHelper.setDragEnable(true);
        itemTouchHelper.setSwipeEnable(true);




        //初始显示今日任务
        TomatoclkList.clear();
        TomatoclkList = DB_Clock.findAll();
        mainAdapter.notifyDataSetChanged(TomatoclkList);
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
            if(TomatoclkList != null){
                DB_Clock.deleteone(TomatoclkList.get(adapterPosition).getId());
                TomatoclkList.remove(adapterPosition);
                mainAdapter.notifyItemRemoved(adapterPosition);
            }
        }

        //交换
        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            if(TomatoclkList != null){
                // 更换数据源中的数据Item的位置
                Collections.swap(TomatoclkList, srcPosition,targetPosition);

                // 更新UI中的Item的位置，主要是给用户看到交互效果
                mainAdapter.notifyItemMoved(srcPosition, targetPosition);
                return true;
            }
            return false;
        }
    };

    //单击Item事件
    private ClockAdapter.OnItemClickListener onItemClickListener = new ClockAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            TomatoClk temp = TomatoclkList.get(position);
            updatePosition = position;
            Intent intent = new Intent(TomatoActivity.this, AddClockActivity.class);
            intent.putExtra("setTomatoClk",temp);
            startActivityForResult(intent,2);

        }
    };

    //单击Button事件
    private ClockAdapter.OnButtonClickListener onButtonClickListener = new ClockAdapter.OnButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            TomatoClk temp = TomatoclkList.get(position);
            updatePosition = position;
            Intent intent = new Intent(TomatoActivity.this, TmtClockShowActivity.class);
            intent.putExtra("showTomatoClk",temp);
            startActivity(intent);

        }
    };

    //初始化右菜单栏
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_methods, menu);
        return true;
    }*/
    //左侧边栏菜单点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_todo) {
            Intent intent_todo = new Intent(TomatoActivity.this, MainActivity.class);
            startActivity(intent_todo);

        } else if (id == R.id.nav_methods) {
            Intent intent_tomato = new Intent(TomatoActivity.this, TomatoActivity.class);
            startActivity(intent_tomato);

        } else if (id == R.id.nav_fight) {
            Intent intent = new Intent(TomatoActivity.this, FightActivity.class);
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
                Toast.makeText(TomatoActivity.this,"暂无添加",Toast.LENGTH_SHORT).show();
                return ;
            }
            TomatoClk newTomatoClk = (TomatoClk) data.getSerializableExtra("addClock");

            DB_Clock.add(newTomatoClk);

            TomatoclkList.add(newTomatoClk);
            mainAdapter.notifyDataSetChanged(TomatoclkList);
        }
        else if(requestCode==2){
            if(data==null) {
                Toast.makeText(TomatoActivity.this,"暂无添加",Toast.LENGTH_SHORT).show();
                return ;
            }
            TomatoClk newTomatoClk = (TomatoClk) data.getSerializableExtra("addClock");
            if(newTomatoClk==null) {
                Toast.makeText(TomatoActivity.this,"暂无添加",Toast.LENGTH_SHORT).show();
                return ;
            }
            TomatoClk updateinfo = DB_Clock.find(newTomatoClk.getId());

            updateinfo.setName(newTomatoClk.getName());
            updateinfo.setNum(newTomatoClk.getNum());
            updateinfo.setTime(newTomatoClk.getTime());
            updateinfo.setInterval(newTomatoClk.getInterval());
            updateinfo.setback(newTomatoClk.getback());
            updateinfo.save();

            TomatoclkList.set(updatePosition,updateinfo);
            mainAdapter.notifyDataSetChanged(TomatoclkList);
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
