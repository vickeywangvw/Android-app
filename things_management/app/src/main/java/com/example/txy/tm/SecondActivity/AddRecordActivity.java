package com.example.txy.tm.SecondActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.txy.tm.HelpClass.RecordItem;
import com.example.txy.tm.HelpClass.getPhotoFromPhotoAlbum;
import com.example.txy.tm.R;
import com.example.txy.tm.Recorder.PlaybackDialogFragment;
import com.example.txy.tm.Recorder.RecordAudioDialogFragment;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class AddRecordActivity extends AppCompatActivity {
    public RecordItem rd;
    public EditText name;
    public Button record;
    public Button tryplay;
    public Button save;
    public Button delete;
    public ImageView change;
    public ImageView select;
    public ImageView back;
    public int n;
    public String imagePath;
    public boolean isupdate = false;

    private File cameraSavePath;//拍照照片路径
    private Uri uritempFile;
    private Uri uri;
    private String photoName = System.currentTimeMillis() + ".jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        name= (EditText) findViewById(R.id.edit_Rname);
        record= (Button) findViewById(R.id.btn_record);
        tryplay= (Button) findViewById(R.id.btn_tryplay);
        save= (Button) findViewById(R.id.btn_Rsave);
        delete= (Button) findViewById(R.id.btn_delete);
        back= (ImageView) findViewById(R.id.cim_show);
        change= (ImageView) findViewById(R.id.imageView_change);
        select= (ImageView) findViewById(R.id.imageView_select);

        Intent intent=getIntent();
        rd=(RecordItem) intent.getSerializableExtra("Record");

        if(rd == null) {
            isupdate = false;
        }
        else {
            isupdate = true;
        }

        if(isupdate){
            //修改原有的事件
            name.setText(rd.getName());
            if(isNumeric(rd.getBack())){
                n=Integer.parseInt(rd.getBack());;
                switch (n){
                    case 1:back.setImageResource(R.mipmap.headcard1);break;
                    case 2:back.setImageResource(R.mipmap.headcard2);break;
                    case 3:back.setImageResource(R.mipmap.headcard3);break;
                    case 4:back.setImageResource(R.mipmap.headcard4);break;
                    case 5:back.setImageResource(R.mipmap.headcard5);break;
                    case 6:back.setImageResource(R.mipmap.headcard6);break;
                }
            }else {
                Bitmap bitmap= BitmapFactory.decodeFile(rd.getBack());
                back.setImageBitmap(bitmap);
            }
        }else{
            rd = new RecordItem();
            n=(int)(Math.random()*6)+1;
            imagePath=""+n;
            switch (n){
            case 1:back.setImageResource(R.mipmap.headcard1);break;
            case 2:back.setImageResource(R.mipmap.headcard2);break;
            case 3:back.setImageResource(R.mipmap.headcard3);break;
            case 4:back.setImageResource(R.mipmap.headcard4);break;
            case 5:back.setImageResource(R.mipmap.headcard5);break;
            case 6:back.setImageResource(R.mipmap.headcard6);break;
            }
        }

        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + photoName);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                goPhotoAlbum();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                n=n%6+1;
                imagePath=""+n;
                switch (n){
                    case 1:back.setImageResource(R.mipmap.headcard1);break;
                    case 2:back.setImageResource(R.mipmap.headcard2);break;
                    case 3:back.setImageResource(R.mipmap.headcard3);break;
                    case 4:back.setImageResource(R.mipmap.headcard4);break;
                    case 5:back.setImageResource(R.mipmap.headcard5);break;
                    case 6:back.setImageResource(R.mipmap.headcard6);break;
                }

            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                final RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
                fragment.show(getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
                fragment.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                    @Override
                    public void onCancel() {
                        fragment.dismiss();
                    }
                });

            }
        });

        tryplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordItem recordingItem = new RecordItem();
                recordingItem=recordsave();
                if(recordingItem.getFilePath()==""){
                    Toast.makeText(AddRecordActivity.this, "尚未录音",Toast.LENGTH_SHORT).show();
                }else {
                    final PlaybackDialogFragment fragmentPlay = PlaybackDialogFragment.newInstance(recordingItem);
                    fragmentPlay.show(getSupportFragmentManager(), PlaybackDialogFragment.class.getSimpleName());
                    fragmentPlay.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                        @Override
                        public void onCancel() {
                            fragmentPlay.dismiss();
                        }
                    });
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                //修改或者添加
                RecordItem recordingItem = new RecordItem();
                recordingItem=recordsave();
                if(recordingItem.getFilePath()!=""){
                    rd=recordingItem;
                }
                rd.setName(name.getText().toString());
                rd.setBack(imagePath);

                getSharedPreferences("try_audio", MODE_PRIVATE)
                        .edit()
                        .putString("audio_path", "")
                        .apply();

                if(isupdate) Toast.makeText(AddRecordActivity.this, "更新成功",Toast.LENGTH_SHORT).show();
                else Toast.makeText(AddRecordActivity.this, "添加成功",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("addRecord",rd);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                finish();
            }
        });

    }

    public RecordItem recordsave(){
        RecordItem recordingItem = new RecordItem();
        SharedPreferences sharePreferences = getSharedPreferences("try_audio", MODE_PRIVATE);
        final String filePath = sharePreferences.getString("audio_path", "");
        long elpased = sharePreferences.getLong("elpased", 0);
        String name=sharePreferences.getString("audio_name","");
        recordingItem.setFilePath(filePath);
        recordingItem.setLength((int) elpased);
        recordingItem.setName(name);
        return recordingItem;
    }

    private void photoClip(Uri uri) {
        Log.d("裁剪:", "我被调用了吗？");
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 3);



    }
    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String photoPath;
        if (requestCode == 1 && resultCode == RESULT_OK) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
                photoClip(Uri.fromFile(cameraSavePath));
            } else {
                photoPath = uri.getEncodedPath();
                photoClip(uri);
            }
            Log.d("拍照返回图片路径:", photoPath);
            Glide.with(AddRecordActivity.this).load(photoPath).into(back);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Log.d("相册返回图片路径:", photoPath);
            photoClip(data.getData());
            //Glide.with(AddRecordActivity.this).load(photoPath).into(back);
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            Glide.with(AddRecordActivity.this).load(uritempFile).into(back);
            File file = null;
            try {
                file = new File(new URI(uritempFile.toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            //照片路径
            String path = Objects.requireNonNull(file).getPath();
            imagePath=path;
            Log.d("裁剪路径:", "null"+path);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}

