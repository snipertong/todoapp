package com.example.todo.widget;

import android.content.Context;
import android.content.Intent;
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


import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by kapil on 27/9/17.
 */

public class TodoCustomAdapter extends ArrayAdapter<ListItem> {
    private Context mContext;
    private ArrayList<ListItem> mListItems;
    private DeleteButtonClickListener mDeleteButtonClickListener;

    public TodoCustomAdapter(@NonNull Context context, @NonNull ArrayList<ListItem> listItems, DeleteButtonClickListener deleteButtonClickListener) {
        super(context, 0, listItems);
        mContext = context;
        mListItems = listItems;
        mDeleteButtonClickListener = deleteButtonClickListener;
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
            viewHolder.list_content = convertView.findViewById(R.id.list_content);
            viewHolder.checkBox = convertView.findViewById(R.id.checkbox);
            viewHolder.button = button;
            viewHolder.content = content;
            viewHolder.date = date;
            viewHolder.title = title;
            viewHolder.end = end;

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

//        viewHolder.content.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
////                Toast.makeText(mContext, "listContent", Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(mContext, EditActivity.class);
//                intent.putExtra("id",id);
//                mContext.startActivity(intent);
//                return false;
//            }
//        });

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Toast.makeText(mContext, ""+isChecked, Toast.LENGTH_SHORT).show();
            }
        });


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
        LinearLayout list_content;
        CheckBox checkBox;
    }
}
