package com.example.todo;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todo.Sql.Contract;
import com.example.todo.Sql.TodoOpenHelper;
import com.example.todo.widget.TodoCustomAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    private Button btnDatePicker,insert,cancel;
    private EditText inputTitle,inputContent;
    private TextView txtDate;
    private Button btnCamera;
    private ImageView imageView;
    private final int CAMERA_REQUEST = 10;
    private TodoCustomAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);


        cancel=findViewById(R.id.cancel);
        insert=findViewById(R.id.insert);
        imageView=findViewById(R.id.image);
        btnCamera = findViewById(R.id.camera);
        btnDatePicker = findViewById(R.id.btn_date);
        txtDate =findViewById(R.id.in_date);
        inputTitle = findViewById(R.id.title);
        inputContent = findViewById(R.id.content);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);
            }
        });
        //时间选择
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过自定义控件AlertDialog实现
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                View view = (LinearLayout) getLayoutInflater().inflate(R.layout.date_dialog, null);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
                //设置日期简略显示 否则详细显示 包括:星期\周
                datePicker.setCalendarViewShown(false);
                //设置date布局
                builder.setView(view);
                builder.setTitle("选择截止日期");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //日期格式
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth()+1;
                        int dayOfMonth = datePicker.getDayOfMonth();
                        txtDate.setText(year+"年"+month+"月"+dayOfMonth+"日");
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
        //插入数据
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase db = todoOpenHelper.getWritableDatabase();
//                @SuppressLint("ResourceType") Drawable drawable = context.getResources().getDrawable(R.id.image);
                ContentValues contentValues = new ContentValues();
//                contentValues.put(Contract.TODO_PHOTO,getPicture(drawable));
                contentValues.put(Contract.TODO_TITLE, inputTitle.getText().toString());
                contentValues.put(Contract.TODO_CONTENT, inputContent.getText().toString());
                contentValues.put(Contract.TODO_DATE, txtDate.getText().toString());
                long currentEpoch = Calendar.getInstance().getTimeInMillis();
                contentValues.put(Contract.TODO_ACCESSED, currentEpoch);
                contentValues.put(Contract.TODO_CREATED, currentEpoch);
                db.insert(Contract.TODO_TABLE_NAME, null, contentValues);
                adapter.notifyDataSetChanged();

            }
        });



        //返回
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    //拍照并显示到imageview

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);

                }
                break;
        }
    }



//    public byte[] bitmabToBytes(Context context){
//        //将图片转化为位图
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.id.image);
//        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
//        //创建一个字节数组输出流,流的大小为size
//        ByteArrayOutputStream baos= new ByteArrayOutputStream(size);
//        try {
//            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            //将字节数组输出流转化为字节数组byte[]
//            byte[] imagedata = baos.toByteArray();
//            return imagedata;
//        }catch (Exception e){
//        }finally {
//            try {
//                bitmap.recycle();
//                baos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return new byte[0];
//    }
private byte[] getPicture(Drawable drawable) {
    if(drawable == null) {
        return null;
    }
    BitmapDrawable bd = (BitmapDrawable) drawable;
    Bitmap bitmap = bd.getBitmap();
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
    return os.toByteArray();
}





}