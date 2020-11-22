package com.example.txy.tm.SecondActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.txy.tm.CircleImageView;
import com.example.txy.tm.ColorPickerDialog;
import com.example.txy.tm.R;
import com.example.txy.tm.HelpClass.ThingInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.txy.tm.CountDownProgressBar.dip2px;

public class AddThingActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,View.OnClickListener
{
    public ThingInfo info;
    public boolean isupdate = false;
    public RadioButton rb1 = null;
    public RadioButton rb2 = null;
    public EditText whatthing = null;
    public EditText note = null;
    public ImageView addtime=null;
    public ImageView adddate=null;
    public TextView showtime=null;
    public TextView date=null;
    public Calendar calendar=Calendar.getInstance();
    public String setdate;
    public String settime;
    public CircleImageView circleImageView;
    public Bitmap icon;
    public Bitmap backgound;
    public int backcolor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thing);

        rb1 = (RadioButton)findViewById(R.id.rb_td);
        rb2 = (RadioButton)findViewById(R.id.rb_ed);
        whatthing = (EditText)findViewById(R.id.edit_thing);
        note = (EditText)findViewById(R.id.edit_note);
        addtime= (ImageView) findViewById(R.id.imageView_alarm);
        adddate= (ImageView) findViewById(R.id.imageView_calender);
        showtime= (TextView) findViewById(R.id.time);
        date= (TextView) findViewById(R.id.day);
        circleImageView= (CircleImageView) findViewById(R.id.circleimage_show);

        ImageView h1= (ImageView) findViewById(R.id.habit1);
        h1.setOnClickListener(this);
        ImageView h2= (ImageView) findViewById(R.id.habit2);
        h2.setOnClickListener(this);
        ImageView h3= (ImageView) findViewById(R.id.habit3);
        h3.setOnClickListener(this);
        ImageView h4= (ImageView) findViewById(R.id.habit4);
        h4.setOnClickListener(this);
        ImageView h5= (ImageView) findViewById(R.id.habit5);
        h5.setOnClickListener(this);
        ImageView h6= (ImageView) findViewById(R.id.habit6);
        h6.setOnClickListener(this);
        ImageView h7= (ImageView) findViewById(R.id.habit7);
        h7.setOnClickListener(this);
        ImageView h8= (ImageView) findViewById(R.id.habit8);
        h8.setOnClickListener(this);
        ImageView h9= (ImageView) findViewById(R.id.habit9);
        h9.setOnClickListener(this);


        Intent intent1 = getIntent();
        info = (ThingInfo) intent1.getSerializableExtra("showThingInfo");

        if(info == null) {
            isupdate = false;
        }
        else {
            isupdate = true;
        }

        if(isupdate){
            //修改原有的事件
            whatthing.setText(info.getWhatthing());
            note.setText(info.getNote());
            date.setText(info.getDate().substring(5));
            showtime.setText(info.getTime());
            setdate=info.getDate();
            settime=info.getTime();
            backgound=info.getBackground();
            int mark = info.getRepeat();
            if(mark==1){
                rb1.setChecked(true);
                visibilitysetting(false);
            }else if(mark==2){
                rb2.setChecked(true);
                visibilitysetting(true);
            }
        }else{
            info = new ThingInfo();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            setdate = format.format(new java.util.Date());
            date.setText(setdate.substring(5));
        }

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                visibilitysetting(false);
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                visibilitysetting(true);
            }
        });

        adddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                calendar=Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(AddThingActivity.this, AddThingActivity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();

            }
        });

        addtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                calendar=Calendar.getInstance();
                TimePickerDialog dialog=new TimePickerDialog(AddThingActivity.this,AddThingActivity.this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true);
                dialog.show();

            }
        });

        final ImageView imageView= (ImageView) findViewById(R.id.coloselect);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                ColorPickerDialog dialog = new ColorPickerDialog(AddThingActivity.this,Color.BLACK,
                        getResources().getString(R.string.app_name),
                        new ColorPickerDialog.OnColorChangedListener() {

                            @Override
                            public void colorChanged(int color) {
                                imageView.setBackgroundColor(color);
                                backcolor=color;
                                circleImageView.setImageBitmap(mergeBitmap(icon,backcolor));
                            }
                        });
                dialog.show();
            }
        });


        Button btn = (Button) findViewById(R.id.bt_save_adtg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                //修改或者添加
                info.setWhatthing(whatthing.getText().toString());
                info.setNote(note.getText().toString());
                Log.v("add","add"+setdate);
                info.setDate(setdate);
                info.setTime(settime);
                info.setBackground(backgound);
                if(!isupdate)
                    info.setDone(false);

                if(rb1.isChecked()) {
                    info.setRepeat(1);
                }else if(rb2.isChecked()){
                    info.setRepeat(2);
                }


                if(isupdate) Toast.makeText(AddThingActivity.this, "更新成功",Toast.LENGTH_SHORT).show();
                else Toast.makeText(AddThingActivity.this, "添加成功",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("addThing",info);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button btn1 = (Button) findViewById(R.id.bt_giveup_adtg);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                finish();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String str=String.format("%02d-%02d",month+1,day);
        if(year!=0){setdate = String.format("%d-%02d-%02d",year,month+1,day);}
        date.setText(str);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String str=String.format("%02d:%02d",hour,minute);
        settime=str;
        showtime.setText(str);
    }

    public void visibilitysetting(boolean flag){
        LinearLayout lt1= (LinearLayout) findViewById(R.id.lt_date);
        GridLayout gt= (GridLayout) findViewById(R.id.gt);
        TextView tv= (TextView) findViewById(R.id.textview_seticon);
        CircleImageView cv= (CircleImageView) findViewById(R.id.circleimage_show);
        if(flag){
            lt1.setVisibility(View.GONE);
            gt.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            cv.setVisibility(View.VISIBLE);
        }else{
            lt1.setVisibility(View.VISIBLE);
            gt.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            cv.setVisibility(View.GONE);
        }
    }

    public Bitmap mergeBitmap(Bitmap icon, int color) {
        Bitmap bitmap = Bitmap.createBitmap((int) dip2px(180), (int) dip2px(180), icon.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);
        canvas.drawBitmap(icon, 65,65, null);
        backgound=bitmap;
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.habit1:
                Log.v("btn","我被点击了吗");
                icon= BitmapFactory.decodeResource(getResources(), R.drawable.habit_3);
                circleImageView.setImageBitmap(mergeBitmap(icon,backcolor));
                break;
            case R.id.habit2:
                Log.v("btn2","我被点击了吗");
                icon= BitmapFactory.decodeResource(getResources(), R.drawable.habit_9);
                circleImageView.setImageBitmap(mergeBitmap(icon,backcolor));
                break;
            case R.id.habit3:
                icon= BitmapFactory.decodeResource(getResources(), R.drawable.habit_25);
                circleImageView.setImageBitmap(mergeBitmap(icon,backcolor));
                break;
            case R.id.habit4:
                icon= BitmapFactory.decodeResource(getResources(), R.drawable.habit_42);
                circleImageView.setImageBitmap(mergeBitmap(icon,backcolor));
                break;
            case R.id.habit5:
                icon= BitmapFactory.decodeResource(getResources(), R.drawable.habit_48);
                circleImageView.setImageBitmap(mergeBitmap(icon,backcolor));
                break;
            case R.id.habit6:
                icon= BitmapFactory.decodeResource(getResources(), R.drawable.habit_51);
                circleImageView.setImageBitmap(mergeBitmap(icon,backcolor));
                break;
            case R.id.habit7:
                icon= BitmapFactory.decodeResource(getResources(), R.drawable.habit_54);
                circleImageView.setImageBitmap(mergeBitmap(icon,backcolor));
                break;
            case R.id.habit8:
                icon= BitmapFactory.decodeResource(getResources(), R.drawable.habit_63);
                circleImageView.setImageBitmap(mergeBitmap(icon,backcolor));
                break;
            case R.id.habit9:
                icon= BitmapFactory.decodeResource(getResources(), R.drawable.habit_77);
                circleImageView.setImageBitmap(mergeBitmap(icon,backcolor));
                break;
        }
    }
}
