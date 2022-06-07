package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.example.todo.widget.DragFloatActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ImageView navi=findViewById(R.id.navi);
        navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加要响应的内容
                showPopupMenu(navi);
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
//                    case R.id.chart:
//                        Toast.makeText(CountActivity.this, "您当前已在统计界面！", Toast.LENGTH_SHORT).show();
//                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }


}
