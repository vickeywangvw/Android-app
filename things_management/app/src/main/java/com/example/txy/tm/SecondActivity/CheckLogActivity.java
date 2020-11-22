package com.example.txy.tm.SecondActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.txy.tm.HelpClass.AtyContainer;
import com.example.txy.tm.HelpClass.ThingInfo;
import com.example.txy.tm.R;
import com.example.txy.tm.SignCalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckLogActivity extends AppCompatActivity {

    ImageView left;
    ImageView right;
    TextView date;
    SignCalendar calendar;
    Button re;
    ThingInfo info;
    private int month;
    private int year;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainer.getInstance().addActivity(this);
        setContentView(R.layout.activity_check_log);
        left= (ImageView) findViewById(R.id.im_left);
        right= (ImageView) findViewById(R.id.im_right);
        date= (TextView) findViewById(R.id.tv_yearmonth);
        calendar= (SignCalendar) findViewById(R.id.calendar);
        re = (Button) findViewById(R.id.bt_return);

        Intent intent = getIntent();
        info = (ThingInfo) intent.getSerializableExtra("showThingInfo");

        //获取当前的月份
        month = Calendar.getInstance().get(Calendar.MONTH);
        //获取当前的年份
        year = Calendar.getInstance().get(Calendar.YEAR);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //设置当前日期
        date.setText(year + "年" + (month + 1) + "月");

        calendar.addMarks(info.checkdate, 0);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.lastMonth();
                date.setText(calendar.getCalendarYear() + "年" + calendar.getCalendarMonth() + "月");
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.nextMonth();
                date.setText(calendar.getCalendarYear() + "年" + calendar.getCalendarMonth() + "月");
            }
        });

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }



}
