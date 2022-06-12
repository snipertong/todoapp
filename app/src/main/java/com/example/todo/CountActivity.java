package com.example.todo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.androidx.widget.CircleStatisticsView;
import com.androidx.widget.Colors;
import com.androidx.widget.Statistical;
import com.example.todo.Sql.Contract;
import com.example.todo.Sql.TodoOpenHelper;
import com.example.todo.widget.DragFloatActionButton;
import com.example.todo.widget.ListItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CountActivity extends AppCompatActivity {
    private TextView start,end,number,delay,finish,cancel;
    private Button startbutton,endbutton;
    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";
    private int a,b;
    private String number1,number2,number3,number4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ImageView navi=findViewById(R.id.navi);
        start=findViewById(R.id.start_date);
        end=findViewById(R.id.end_date);
        number=findViewById(R.id.number);
        delay=findViewById(R.id.delay);
        finish=findViewById(R.id.finish);
        cancel=findViewById(R.id.cancel);
        startbutton=findViewById(R.id.start);
        endbutton=findViewById(R.id.end);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int month2=c.get(Calendar.MONTH)+2;
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        start.setText(year+"年"+month+"月"+dayOfMonth+"日");
        end.setText(year+"年"+month2+"月"+dayOfMonth+"日");






        TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = todoOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.TODO_TABLE_NAME, null, null, null, null, null, null);
        int num=cursor.getCount();
        number1=String.valueOf(num);
        number.setText(number1);
        cursor.close();

        String select=0+"";
        Cursor cursor1 = db.query(Contract.TODO_TABLE_NAME, null, "status=?", new String[]{select}, null, null, null);
        int num1=cursor1.getCount();
        number2=String.valueOf(num1);
        cancel.setText(number2);
        cursor1.close();

        String select1=1+"";
        Cursor cursor2 = db.query(Contract.TODO_TABLE_NAME, null, "status=?", new String[]{select1}, null, null, null);
        int num2=cursor2.getCount();
        number3=String.valueOf(num2);
        finish.setText(number3);
        cursor2.close();

        String select2=2+"";
        Cursor cursor3 = db.query(Contract.TODO_TABLE_NAME, null, "status=?", new String[]{select2}, null, null, null);
        int num3=cursor3.getCount();
        number4=String.valueOf(num3);
        delay.setText(number4);
        cursor3.close();

        db.close();

        CircleStatisticsView csv = findViewById(R.id.csv);
        List<String> data = new ArrayList<>();
        data.add(number3);
        data.add(number2);
        data.add(number4);
//构建显示数据
        float percent[] = Statistical.toPercent(data);
        int color[] = Colors.randomColors(data.size());
        String[] markBottom = Statistical.toMarks(data);
        String[] markTop = {"完成", "未完成", "逾期"};
        List<Statistical> list = new ArrayList<>();
        for (int i = 0; i < percent.length; i++) {
            list.add(new Statistical(percent[i], color[i], color[i], markTop[i], markBottom[i]));
        }
//设置数据
        csv.setDataSource(list);






        navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加要响应的内容
                showPopupMenu(navi);
            }
        });
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过自定义控件AlertDialog实现
                AlertDialog.Builder builder = new AlertDialog.Builder(CountActivity.this);
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
                        start.setText(year+"年"+month+"月"+dayOfMonth+"日");
                        String str=year+"年"+month+"月"+dayOfMonth+"日";
                        str = str.replaceAll(REGEX_CHINESE,"");
                        a=Integer.parseInt(str);
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
        endbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过自定义控件AlertDialog实现
                AlertDialog.Builder builder = new AlertDialog.Builder(CountActivity.this);
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
                        end.setText(year+"年"+month+"月"+dayOfMonth+"日");
                        String str=year+"年"+month+"月"+dayOfMonth+"日";
                        str = str.replaceAll(REGEX_CHINESE,"");
                        b=Integer.parseInt(str);
                        if (a>b){
                            Toast.makeText(CountActivity.this, "请输入正确的截止日期", Toast.LENGTH_SHORT).show();
                        }
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
    }

    
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.viewchange:
                        Intent intent1=new Intent(CountActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.chart:
                        Toast.makeText(CountActivity.this, "您当前已在统计界面！", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }


}
