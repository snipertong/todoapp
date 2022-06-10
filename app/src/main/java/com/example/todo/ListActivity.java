package com.example.todo;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.Sql.Contract;
import com.example.todo.Sql.TodoOpenHelper;
import com.example.todo.widget.ListItem;
import com.example.todo.widget.TodoCustomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    public static View alertView, expandView;
    private ArrayList<ListItem> todos;
    private TextView txtDate;
    private TodoCustomAdapter adapter;
    private ListView listView;
    private FloatingActionButton fab;
    private ImageView navi;
    private Button btnDatePicker,insert,cancel;
    private Button btnCamera;
    private ImageView imageView;
    private byte[] photo;
    private Bitmap bm;
    private Drawable drawable;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(this);

        navi=findViewById(R.id.navi);
        navi.setOnClickListener(this);

        listView = findViewById(R.id.todo_list);
        todos = new ArrayList<>();
        TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = todoOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(Contract.TODO_TABLE_NAME, null, null, null, null, null, null);
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
        adapter = new TodoCustomAdapter(this, todos, new TodoCustomAdapter.DeleteButtonClickListener() {
            @Override
            public void onDeleteClicked(int position, View view) {
                TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase db = todoOpenHelper.getReadableDatabase();
                String id = String.valueOf(todos.get(position).getId());
                db.delete(Contract.TODO_TABLE_NAME, Contract.TODO_ID + " = " + id, null);
                todos.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Create Alert Dialog displaying title and expanded-content of selected list item.
                final ListItem listItem = todos.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                expandView = getLayoutInflater().inflate(R.layout.item_expanded_view, null);
                builder.setView(expandView);
                TextView todoTitle = expandView.findViewById(R.id.expanded_todo_title);
                TextView todoContent = expandView.findViewById(R.id.expanded_todo_content);
                ImageView todoPhoto =expandView.findViewById(R.id.expanded_todo_photo);
                todoTitle.setText(listItem.getTitle());
                todoContent.setText(listItem.getContent());

                //将图片显示出来
                byte[] photo = listItem.getPhoto();
                Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length, null);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                Drawable drawable = bitmapDrawable;

                if (drawable != null){
                todoPhoto.setImageDrawable(drawable);}

                builder.setView(expandView);
                builder.setNegativeButton("取消", null);
                builder.show();
            }


        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ListItem listItem = todos.get(i);
                Long id =listItem.getId();
                String title=listItem.getTitle();
                String content=listItem.getContent();
                String end=listItem.getEnd();
                String date=listItem.getDate();
                byte[] photo=listItem.getPhoto();

                Toast.makeText(ListActivity.this, "长按", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ListActivity.this,EditActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("1",title);
                intent.putExtra("2",content);
                intent.putExtra("3",end);
                intent.putExtra("4",date);
                intent.putExtra("5",photo);
                startActivity(intent);
                return true;
            }
        });

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.navi:
                showPopupMenu(navi);
                break;
            case R.id.fab:
                Intent intent=new Intent(ListActivity.this, EditActivity.class);
                startActivity(intent);
                break;
        }
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
                        Intent intent1=new Intent(ListActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.chart:
                        Intent intent2=new Intent(ListActivity.this, CountActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }


}
