package com.example.txy.tm.SecondActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.txy.tm.HelpClass.ThingInfo;
import com.example.txy.tm.HelpClass.TomatoClk;
import com.example.txy.tm.R;

public class AddClockActivity extends AppCompatActivity {

    public TomatoClk clk;
    public boolean isupdate = false;
    public EditText name;
    public EditText num;
    public EditText time;
    public EditText interval;
    public ImageView back;
    public int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clock);

        name= (EditText) findViewById(R.id.edit_name);
        num= (EditText) findViewById(R.id.edit_num);
        time= (EditText) findViewById(R.id.edit_time);
        interval= (EditText) findViewById(R.id.edit_interval);


        Intent intent = getIntent();
        clk = (TomatoClk) intent.getSerializableExtra("setTomatoClk");

        if(clk == null) {
            isupdate = false;
        }
        else {
            isupdate = true;
        }

        if(isupdate){
            //修改原有的事件
            name.setText(clk.getName());
            num.setText(""+clk.getNum());
            time.setText(""+clk.getTime());
            interval.setText(""+clk.getInterval());
            n=clk.getback();

        }else{
            clk = new TomatoClk();
            n=(int)(Math.random()*8)+1;
        }


        back= (ImageView) findViewById(R.id.im_back);
        switch (n){
            case 1:back.setImageResource(R.mipmap.art_card1);break;
            case 2:back.setImageResource(R.mipmap.art_card2);break;
            case 3:back.setImageResource(R.mipmap.art_card3);break;
            case 4:back.setImageResource(R.mipmap.art_card4);break;
            case 5:back.setImageResource(R.mipmap.art_card5);break;
            case 6:back.setImageResource(R.mipmap.art_card6);break;
            case 7:back.setImageResource(R.mipmap.art_card7);break;
            case 8:back.setImageResource(R.mipmap.art_card8);break;

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n=n%8+1;
                switch (n){
                    case 1:back.setImageResource(R.mipmap.art_card1);break;
                    case 2:back.setImageResource(R.mipmap.art_card2);break;
                    case 3:back.setImageResource(R.mipmap.art_card3);break;
                    case 4:back.setImageResource(R.mipmap.art_card4);break;
                    case 5:back.setImageResource(R.mipmap.art_card5);break;
                    case 6:back.setImageResource(R.mipmap.art_card6);break;
                    case 7:back.setImageResource(R.mipmap.art_card7);break;
                    case 8:back.setImageResource(R.mipmap.art_card8);break;

                }
            }
        });


        Button btn = (Button) findViewById(R.id.bt_save_adck);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                //修改或者添加
                clk.setName(name.getText().toString());
                clk.setNum(Integer.parseInt(num.getText().toString()));
                clk.setTime(Integer.parseInt(time.getText().toString()));
                clk.setInterval(Integer.parseInt(interval.getText().toString()));
                clk.setback(n);


                if(isupdate) Toast.makeText(AddClockActivity.this, "更新成功",Toast.LENGTH_SHORT).show();
                else Toast.makeText(AddClockActivity.this, "添加成功",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("addClock",clk);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button btn1 = (Button) findViewById(R.id.bt_giveup_adck);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                finish();
            }
        });

    }


}
