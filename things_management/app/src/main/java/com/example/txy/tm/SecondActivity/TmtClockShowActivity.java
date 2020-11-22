package com.example.txy.tm.SecondActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.txy.tm.HelpClass.TomatoClk;
import com.example.txy.tm.R;

public class TmtClockShowActivity extends AppCompatActivity {
    public int TotalNum;
    public int time;
    public int interval;
    public TomatoClk clk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmt_clock_show);

        Log.v("show","我运行了");

        Intent intent = getIntent();
        clk = (TomatoClk) intent.getSerializableExtra("showTomatoClk");
        TotalNum=clk.getNum()*2-1;
        time=clk.getTime()*1000;
        interval=clk.getInterval()*1000;


        Intent intent1 = new Intent(TmtClockShowActivity.this, TimeActivity.class);
        intent1.putExtra("Total",TotalNum);
        intent1.putExtra("Time",time);
        intent1.putExtra("Interval",interval);
        startActivity(intent1);

        finish();
    }
}
