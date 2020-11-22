package com.example.txy.tm.FirstActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txy.tm.DataBase.DB_Thing;
import com.example.txy.tm.R;
import com.example.txy.tm.SecondActivity.AddThingActivity;
import com.example.txy.tm.SecondActivity.TmtClockShowActivity;
import com.example.txy.tm.Adapter.ThingAdapter;
import com.example.txy.tm.HelpClass.ThingInfo;
import com.example.txy.tm.TouchHelper.MyItemTochHelper;
import com.example.txy.tm.TouchHelper.MyItemTouchHelpCallback;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //全局变量
    private GoogleApiClient client;
    private RecyclerView recyclerView;
    public List<ThingInfo> ThingsInfoList = new ArrayList<>();
    private int updatePosition;
    private ThingAdapter mainAdapter;
    private MyItemTochHelper itemTouchHelper;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //监听器
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("今日事务");
        setSupportActionBar(toolbar);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(new DrawerArrowDrawable(getApplicationContext()));
        }


        //添加事件按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddThingActivity.class);
                ThingInfo temp = null;
                intent.putExtra("showThingInfo",temp);
                startActivityForResult(intent, 1);
            }
        });



        //获得数据库
        DB_Thing.connect();

        //recyclerView 相关初始化
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new ThingAdapter(ThingsInfoList);
        mainAdapter.setOnItemClickListener(onItemClickListener);
        mainAdapter.setOnCheckedChangeListener(onCheckedChangeListener);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 6, 0, 6);//设置itemView中内容相对边框左，上，右，下距离
            }
        });


        // 把ItemTouchHelper和itemTouchHelper绑定
        itemTouchHelper = new MyItemTochHelper(onItemTouchCallbackListener);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mainAdapter.setItemTouchHelper(itemTouchHelper);

        itemTouchHelper.setDragEnable(true);
        itemTouchHelper.setSwipeEnable(true);

        //初始显示今日任务
        ThingsInfoList.clear();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new java.util.Date());
        ThingsInfoList = DB_Thing.findtoday(today);
        mainAdapter.notifyDataSetChanged(ThingsInfoList);
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
            if(ThingsInfoList != null){
                DB_Thing.deleteone(ThingsInfoList.get(adapterPosition).getId());
                ThingsInfoList.remove(adapterPosition);
                mainAdapter.notifyItemRemoved(adapterPosition);
            }
        }

        //交换
        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            if(ThingsInfoList != null){
                // 更换数据源中的数据Item的位置
                Collections.swap(ThingsInfoList, srcPosition,targetPosition);

                // 更新UI中的Item的位置，主要是给用户看到交互效果
                mainAdapter.notifyItemMoved(srcPosition, targetPosition);
                return true;
            }
            return false;
        }
    };


    //单击Item事件
    private ThingAdapter.OnItemClickListener onItemClickListener = new ThingAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ThingInfo temp = ThingsInfoList.get(position);
            updatePosition = position;
            Intent intent = new Intent(MainActivity.this, AddThingActivity.class);
            intent.putExtra("showThingInfo",temp);
            startActivityForResult(intent,2);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //右菜单栏初始化
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    //右菜单点击事件
    public boolean onOptionsItemSelected(MenuItem item){
        int Mitemid=item.getItemId();
        if (Mitemid==R.id.todo_today){
            toolbar.setTitle("今日事务");
            ThingsInfoList.clear();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String today = format.format(new java.util.Date());
            ThingsInfoList = DB_Thing.findtoday(today);
            mainAdapter.notifyDataSetChanged(ThingsInfoList);
        }else if(Mitemid==R.id.todo_done){
            toolbar.setTitle("已完成");
            ThingsInfoList.clear();
            ThingsInfoList = DB_Thing.findTdone();
            mainAdapter.notifyDataSetChanged(ThingsInfoList);
        }else {
            toolbar.setTitle("每日任务");
            ThingsInfoList.clear();
            ThingsInfoList = DB_Thing.findEdone();
            mainAdapter.notifyDataSetChanged(ThingsInfoList);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //左侧边栏菜单点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_todo) {
            Intent intent_todo = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent_todo);

        } else if (id == R.id.nav_methods) {
            Intent intent_tomato = new Intent(MainActivity.this, TomatoActivity.class);
            startActivity(intent_tomato);

        } else if (id == R.id.nav_fight) {
            Intent intent = new Intent(MainActivity.this, FightActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //添加事件
       @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           if(requestCode==1){
               if(data==null)
               {
                   Toast.makeText(MainActivity.this,"暂无添加",Toast.LENGTH_SHORT).show();
                   return ;
               }
               ThingInfo newThinginfo = (ThingInfo) data.getSerializableExtra("addThing");

               DB_Thing.add(newThinginfo);

               ThingsInfoList.add(newThinginfo);
               mainAdapter.notifyDataSetChanged(ThingsInfoList);
           }
        else if(requestCode==2){
            if(data==null) {
                Toast.makeText(MainActivity.this,"暂无添加",Toast.LENGTH_SHORT).show();
                return ;
            }
            ThingInfo newThinginfo = (ThingInfo) data.getSerializableExtra("addThing");
            if(newThinginfo==null) {
                Toast.makeText(MainActivity.this,"暂无添加",Toast.LENGTH_SHORT).show();
                return ;
            }
            ThingInfo updateinfo = DB_Thing.find(newThinginfo.getId());

            updateinfo.setWhatthing(newThinginfo.getWhatthing());
            updateinfo.setDone(newThinginfo.isDone());
            updateinfo.setRepeat(newThinginfo.getRepeat());
            updateinfo.setNote(newThinginfo.getNote());
               updateinfo.setTime(newThinginfo.getTime());
               updateinfo.setDate(newThinginfo.getDate());
               updateinfo.setBackground(newThinginfo.getBackground());
            updateinfo.save();

            ThingsInfoList.set(updatePosition,updateinfo);
            mainAdapter.notifyDataSetChanged(ThingsInfoList);
        }

    }

    /*
    CheckBox 点击监听操作
     */
    private ThingAdapter.OnCheckedChangeListener onCheckedChangeListener = new ThingAdapter.OnCheckedChangeListener() {
        @Override
        public void onItemCheckedChange(CompoundButton view, int position, boolean checked) {

            //更新数据库
            ThingInfo click = DB_Thing.find(ThingsInfoList.get(position).getId());  //被点击的事情
            click.setDone(checked);
            if(click.getRepeat()==2){
                Date date= new   Date();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(calendar.DATE,1);
                date=calendar.getTime(); SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String date_time = format.format(date);
                click.date=date_time;
            }
            click.save();

            //更新数组内容
            ThingsInfoList.get(position).setDone(checked);
            mainAdapter.notifyItemChanged(position);

            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);


        }
    };


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