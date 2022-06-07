package com.example.todo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.todo.widget.ListItem;
import com.example.todo.widget.TodoCustomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Context context;
    private CalendarView calendarView;
    private ListView listView;
    private ArrayList<ListItem> todos;
    private FloatingActionButton fab;
    public static View alertView, expandView;
    private Button btnDatePicker, btnTimePicker;
    private TextView txtDate, txtTime;
    private TodoCustomAdapter adapter;
    private ImageView navi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        context = this;

        calendarView = (CalendarView)findViewById(R.id.calendarViewId);
        listView = (ListView) findViewById(R.id.todo_list);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        navi=findViewById(R.id.navi);
        navi.setOnClickListener(this);

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