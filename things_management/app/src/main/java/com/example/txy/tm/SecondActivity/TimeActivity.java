package com.example.txy.tm.SecondActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.txy.tm.CountDownProgressBar;
import com.example.txy.tm.FirstActivity.FightActivity;
import com.example.txy.tm.FirstActivity.MainActivity;
import com.example.txy.tm.FirstActivity.TomatoActivity;
import com.example.txy.tm.R;

public class TimeActivity extends AppCompatActivity {
    private CountDownProgressBar cpb_countdown;
    int rnd;
    int num;
    int time;
    int interval;
    public MediaPlayer bell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_tmt_clock);
        bell=MediaPlayer.create(this,R.raw.bell);

        RelativeLayout rl= (RelativeLayout) findViewById(R.id.activity_tmt_clock_show);
        TextView tv= (TextView) findViewById(R.id.textView_clock);
        ImageView im= (ImageView) findViewById(R.id.imageView_clock);
        TextView tv1= (TextView) findViewById(R.id.textView_info);

        Intent intent=getIntent();
        num=intent.getIntExtra("Total",-1);
        rnd=intent.getIntExtra("Round",-1);
        time=intent.getIntExtra("Time",0);
        interval=intent.getIntExtra("Interval",0);

        rl.setBackgroundResource(R.mipmap.default_background_2);
        tv.setText("keep going never give up!");
        tv1.setText("正在进行中 还剩"+(num-rnd-1)/2+"次");
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeActivity.this, TomatoActivity.class);
                startActivity(intent);
                finish();
                onDestroy();
            }
        });



        if (rnd==-1){rnd=0;}
        if(rnd!=(num-1)){
            this.start_NotFinal(time,rnd,num,interval);
        }else{
            this.start_Final(time);
        }



    }

    public void start_NotFinal(final int time, final int rnd, final int num, final int interval){
        cpb_countdown = (CountDownProgressBar) findViewById(R.id.cpb_countdown);
        cpb_countdown.setDuration(time, new CountDownProgressBar.OnFinishListener() {
            @Override
                public void onFinish() {
                Intent intent = new Intent(TimeActivity.this, IntervalActivity.class);
                intent.putExtra("Round",rnd+1);
                intent.putExtra("Total",num);
                intent.putExtra("Interval",interval);
                intent.putExtra("Time",time);
                bell.start();
                startActivity(intent);
            }
        });
    }

    public void start_Final(int time){
        cpb_countdown = (CountDownProgressBar) findViewById(R.id.cpb_countdown);
        cpb_countdown.setDuration(time, new CountDownProgressBar.OnFinishListener() {
            @Override
            public void onFinish() {
                bell.start();
                Intent intent = new Intent(TimeActivity.this, TomatoActivity.class);
                startActivity(intent);

            }
        });
    }
}
