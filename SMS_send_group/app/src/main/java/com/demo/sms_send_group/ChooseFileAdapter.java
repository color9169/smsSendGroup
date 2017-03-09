package com.demo.sms_send_group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.sms_send_group.entity.FileInfo;

import java.util.List;

/**
 * Created by color on 17/3/1.
 */

public class ChooseFileAdapter extends BaseAdapter {
    private Context mContext;
    private List<FileInfo> fileInfo;

    public ChooseFileAdapter(Context mContext, List<FileInfo> fileInfo) {
        this.mContext = mContext;
        this.fileInfo = fileInfo;
    }

    public ChooseFileAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setFileInfo(List<FileInfo> fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public int getCount() {
        if (fileInfo != null) {
            return fileInfo.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return fileInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChooseVH vh;
        if (convertView == null) {
            vh = new ChooseVH();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.file_item_layout, null, false);
            vh.fileName = (TextView) convertView.findViewById(R.id.file_name);
            vh.filePath = (TextView) convertView.findViewById(R.id.file_path);
            convertView.setTag(vh);
        } else {
            vh = (ChooseVH) convertView.getTag();
        }

        FileInfo info = fileInfo.get(position);

        vh.fileName.setText(info.fileName);
        vh.filePath.setText(info.filePath);
        return convertView;
    }

    private class ChooseVH {
        TextView fileName;
        TextView filePath;
    }
}
