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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.todo.Sql.Contract;
import com.example.todo.Sql.TodoOpenHelper;
import com.example.todo.widget.ListItem;
import com.example.todo.widget.TodoCustomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    public static View alertView, expandView;
    private ArrayList<ListItem> todos;
    private Button btnDatePicker;
    private TextView txtDate;
    private TodoCustomAdapter adapter;
    private ListView listView;
    private FloatingActionButton fab;
    private ImageView navi;


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
//            @SuppressLint("Range") byte[] photo = cursor.getBlob(cursor.getColumnIndex(Contract.TODO_PHOTO));
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(Contract.TODO_CONTENT));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(Contract.TODO_DATE));
            @SuppressLint("Range") long createdEpoch = cursor.getLong(cursor.getColumnIndex(Contract.TODO_CREATED));
            @SuppressLint("Range") long accessedEpoch = cursor.getLong(cursor.getColumnIndex(Contract.TODO_ACCESSED));
            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(Contract.TODO_ID));
            ListItem listItem = new ListItem(title, content, id,date, createdEpoch, accessedEpoch);
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
//                ImageView todoPhoto =expandView.findViewById(R.id.expanded_todo_photo);
                todoTitle.setText(listItem.getTitle());
                todoContent.setText(listItem.getContent());
//                todoPhoto.setImageDrawable(getDrawable().get(0));
                long currentEpoch = Calendar.getInstance().getTimeInMillis();
                listItem.setAccessed(currentEpoch);

                TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase db = todoOpenHelper.getReadableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.TODO_ID, listItem.getId());
                contentValues.put(Contract.TODO_ACCESSED, String.valueOf(currentEpoch));
                db.update(Contract.TODO_TABLE_NAME, contentValues, Contract.TODO_ID + "=?", null);

                builder.setView(expandView);
                builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, listItem.getTitle() + "\n" + listItem.getContent());
                        startActivity(sendIntent);
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, final long l) {
                Log.i("TAG", "onItemLongClick: " + todos.size());
                Log.i("TAG", "onItemLongClick: ");
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                TextView title = new TextView(ListActivity.this);
                title.setText("Add/Edit a task");
                title.setBackgroundColor(Color.DKGRAY);
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);
                builder.setCustomTitle(title);
                alertView = getLayoutInflater().inflate(R.layout.item_todo, null);
                builder.setView(alertView);
                btnDatePicker = alertView.findViewById(R.id.btn_date);
                txtDate = alertView.findViewById(R.id.in_date);
                btnDatePicker.setOnClickListener(ListActivity.this);

                final ListItem listItem = todos.get(i);
                final EditText inputTitle = alertView.findViewById(R.id.title);
                final EditText inputContent = alertView.findViewById(R.id.content);
                inputTitle.setText(listItem.getTitle());
                inputContent.setText(listItem.getContent());
                txtDate.setText(listItem.getDate());

                builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(getApplicationContext());
                        SQLiteDatabase db = todoOpenHelper.getWritableDatabase();

                        long currentEpoch = Calendar.getInstance().getTimeInMillis();
                        Log.i("Edit Todo epoch", String.valueOf(currentEpoch));
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Contract.TODO_ID, listItem.getId());
                        contentValues.put(Contract.TODO_TITLE, inputTitle.getText().toString());
                        contentValues.put(Contract.TODO_CONTENT, inputContent.getText().toString());
                        contentValues.put(Contract.TODO_DATE, txtDate.getText().toString());
                        contentValues.put(Contract.TODO_ACCESSED, currentEpoch);
                        db.update(Contract.TODO_TABLE_NAME, contentValues, Contract.TODO_ID + "=?", null);
                        listItem.setTitle(inputTitle.getText().toString());
                        listItem.setContent(inputContent.getText().toString());
                        listItem.setDate(txtDate.getText().toString());
                        listItem.setAccessed(currentEpoch);
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("CANCEL", null);
                builder.show();
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

//    private ArrayList<Drawable> getDrawable() {
//
//        TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(getApplicationContext());
//        SQLiteDatabase db = todoOpenHelper.getReadableDatabase();
//
//        ArrayList<Drawable> drawables = new ArrayList<Drawable>();
//        //查询数据库
//        Cursor c = db.query(Contract.TODO_TABLE_NAME, null, null, null, null, null, null);
//
//        //遍历数据
//        if(c != null && c.getCount() != 0) {
//            while(c.moveToNext()) {
//                //获取数据
//                @SuppressLint("Range")byte[] b = c.getBlob(c.getColumnIndex(Contract.TODO_PHOTO));
//
//                //将获取的数据转换成drawable
//                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
//
//
//                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//                Drawable drawable = bitmapDrawable;
//                drawables.add(drawable);
//            }
//        }
//        return drawables;
//    }


}
