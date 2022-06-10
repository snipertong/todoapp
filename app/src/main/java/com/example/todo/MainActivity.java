package com.example.todo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.Sql.Contract;
import com.example.todo.Sql.TodoOpenHelper;
import com.example.todo.adapter.CalendarAdapter;
import com.example.todo.widget.ListItem;
import com.example.todo.adapter.TodoCustomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Context context;
    private CalendarView calendarView;
    private ListView listView;
    private ArrayList<ListItem> todos;
    private FloatingActionButton fab;
    private TextView date;
    private CalendarAdapter adapter;
    private ImageView navi;
    private boolean flag=false;
    private String number1,number2;
    private String[] selectionArgs,selectionArgs2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        context = this;


        calendarView = (CalendarView)findViewById(R.id.calendarView);
        date=findViewById(R.id.date);
        listView = (ListView) findViewById(R.id.todo_list);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        navi=findViewById(R.id.navi);
        navi.setOnClickListener(this);
        
        //显示点击日期的item
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month1, int dayOfMonth) {
                flag=true;
                todos = new ArrayList<>();
                TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase db = todoOpenHelper.getReadableDatabase();
                int month = month1+1;
                date.setText(year+"年"+month+"月"+dayOfMonth+"日");
                number2 = date.getText().toString();
            }
        });
        
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        date.setText(year+"年"+month+"月"+dayOfMonth+"日");
        number1 = date.getText().toString();
        selectionArgs = new  String[]{number1};


//        if (flag=true){
//            selectionArgs = new  String[]{number2};
//            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
//        }else
//        {
//            selectionArgs = new  String[]{number1};
//            Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
//        }


        //显示当天item
        todos = new ArrayList<>();
        TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = todoOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(Contract.TODO_TABLE_NAME, null, "date=?", selectionArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        do {
            if (cursor == null || cursor.getCount() == 0) break;
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(Contract.TODO_TITLE));
            @SuppressLint("Range") byte[] photo = cursor.getBlob(cursor.getColumnIndex(Contract.TODO_PHOTO));
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(Contract.TODO_CONTENT));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(Contract.TODO_DATE));
            @SuppressLint("Range") String end = cursor.getString(cursor.getColumnIndex(Contract.TODO_END));
//            @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(Contract.TODO_STATUS));
            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(Contract.TODO_ID));
            ListItem listItem = new ListItem(title, content, id,date,end,photo);
            todos.add(listItem);
        } while (cursor.moveToNext());
        cursor.close();
        db.close();
        adapter=new CalendarAdapter(this,todos);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



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
                        Intent intent1=new Intent(MainActivity.this, ListActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.chart:
                        Intent intent2=new Intent(MainActivity.this, CountActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    //实现返回时刷新
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
//        overridePendingTransition(0, 0);
        finish();
//        overridePendingTransition(0, 0);
        startActivity(intent);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.navi:
                showPopupMenu(navi);
                break;
            case R.id.fab:
                Intent intent3=new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent3);
                break;
        }
    }

}