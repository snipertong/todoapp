package com.example.todo.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.example.todo.CountActivity;
import com.example.todo.ListActivity;
import com.example.todo.MainActivity;
import com.example.todo.R;

public class TitleLayout extends RelativeLayout {

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //from方法构造出一个LayoutInflater对象，然后调用inflate加载布局
        //第一个参数是要加载的布局文件id，第二个参数给加载好的文件添加一个父布局
        LayoutInflater.from(context).inflate(R.layout.title_layout, this);
        ImageView navi=findViewById(R.id.navi);


//        navi.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(navi);
//            }
//        });
    }




//    private void showPopupMenu(View view) {
//
//        PopupMenu popupMenu = new PopupMenu(this, view);
//        // menu布局
//        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
//        // menu的item点击事件
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.ParameterConfig:
//                        Intent intent1=new Intent(TitleLayout.this, ListActivity.class);
//                        startActivity(intent1);
//                        //what you want to to do
//                        break;
//                    case R.id.ProgramEdit:
//                        Intent intent2=new Intent(MainActivity.this, CountActivity.class);
//                        startActivity(intent2);
//
//                        //what you want to to do
//                }
//                return true;
//            }
//
//
//        });



}
