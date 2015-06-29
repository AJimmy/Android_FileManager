package com.alice.filemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 15-6-29.
 * Project: FileManager
 * User: Alice
 * Data: 15-6-29
 */
public class FileAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> fileList;
    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent,false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_image);
            holder.txt = (TextView) convertView.findViewById(R.id.item_txt);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.txt.setText(fileList.get(position));
        //TODO 判断是文件、目录，显示图片
//        if (new File(fileList.get(position)))
        return convertView;
    }

    private  static  class ViewHolder{
        ImageView image;
        TextView txt;
    }
}
