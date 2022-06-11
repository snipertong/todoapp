package com.example.todo.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.EditActivity;
import com.example.todo.R;
import com.example.todo.Sql.Contract;
import com.example.todo.Sql.TodoOpenHelper;
import com.example.todo.widget.ListItem;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TodoCustomAdapter extends ArrayAdapter<ListItem> {
    private Context mContext;
    private ArrayList<ListItem> mListItems;
    private DeleteButtonClickListener mDeleteButtonClickListener;
    private boolean isChecked;
    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";
    private boolean flag=false;
    private static SQLiteDatabase db;

    public TodoCustomAdapter(@NonNull Context context, @NonNull ArrayList<ListItem> listItems, DeleteButtonClickListener deleteButtonClickListener) {
        super(context, 0, listItems);
        mContext = context;
        mListItems = listItems;
        mDeleteButtonClickListener = deleteButtonClickListener;
        TodoOpenHelper todoOpenHelper=new TodoOpenHelper(context);
        db=todoOpenHelper.getWritableDatabase();
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row, null);
            viewHolder = new ViewHolder();

            TextView title = convertView.findViewById(R.id.title);
            TextView content = convertView.findViewById(R.id.content);
            TextView date = convertView.findViewById(R.id.date);
            TextView end = convertView.findViewById(R.id.end);
            Button button = convertView.findViewById(R.id.delete_button);
            LinearLayout linear=convertView.findViewById(R.id.linear);
            viewHolder.checkBox = convertView.findViewById(R.id.checkbox);

            viewHolder.button = button;
            viewHolder.content = content;
            viewHolder.date = date;
            viewHolder.title = title;
            viewHolder.end = end;
            viewHolder.linear=linear;

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteButtonClickListener.onDeleteClicked(position, view);
            }
        });

        ListItem item = mListItems.get(position);
        viewHolder.title.setText(item.getTitle());
        viewHolder.content.setText(item.getContent());
        viewHolder.date.setText(item.getDate());
        viewHolder.end.setText(item.getEnd());

        // 获取当前事项的id
        long id = getItem(position).getId();
        String itemid = String.valueOf(id);


        if (item.getStatus()==0){
            Long it =item.getId();
            String string2 = String.valueOf(it);

            String string=item.getEnd();
            string = string.replaceAll(REGEX_CHINESE,"");
            int b=Integer.parseInt(string);

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            String str=year+"年"+month+"月"+dayOfMonth+"日";
            str = str.replaceAll(REGEX_CHINESE,"");
            int a=Integer.parseInt(str);
            if (a>b){
                ContentValues contentValues=new ContentValues();
                contentValues.put(Contract.TODO_STATUS,2);
                db.update(Contract.TODO_TABLE_NAME,contentValues,Contract.TODO_ID + "=?",new String[]{string2});
            }

        }


        //延期事件设置状态为2
        if (item.getStatus()==2){
                viewHolder.linear.setBackgroundColor(Color.RED);
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                Toast.makeText(mContext, ""+isChecked, Toast.LENGTH_SHORT).show();
                long id = getItem(position).getId();
                String string = String.valueOf(id);
                if (isChecked){
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(Contract.TODO_STATUS,1);
                    db.update(Contract.TODO_TABLE_NAME,contentValues,Contract.TODO_ID + "=?",new String[]{string});
                    Toast.makeText(mContext, "该事件完成", Toast.LENGTH_SHORT).show();
                }else {
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(Contract.TODO_STATUS,0);
                    db.update(Contract.TODO_TABLE_NAME,contentValues,Contract.TODO_ID + "=?",new String[]{string});
                    Toast.makeText(mContext, "取消完成事件", Toast.LENGTH_SHORT).show();

                }
            }
        });
        notifyDataSetChanged();
        return convertView;
    }

    public interface DeleteButtonClickListener {
        void onDeleteClicked(int position, View v);
    }

    static class ViewHolder {

        TextView title;
        TextView content;
        TextView date;
        Button button;
        TextView end;
        LinearLayout linear;
        CheckBox checkBox;
    }
}
