package com.demo.sms_send_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.demo.sms_send_group.entity.FileInfo;
import com.demo.sms_send_group.util.FileUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by color on 17/3/1.
 */

public class ChooseFileActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.no_have_data)
    FrameLayout noHaveData;

    private String filePath;
    private String fileName;

    private ChooseFileAdapter adapter;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_file_layout);
        ButterKnife.bind(this);
        mContext = this;
        init();
        setListener();
    }

    private void init() {
        String path = Environment.getExternalStorageDirectory().getPath();
        FileUtil.getFileList(path, "", new FileUtil.OnFileListCallback() {
            @Override
            public void SearchFileListInfo(List<FileInfo> list) {
                if (list.size() == 0) {
                    noHaveData.setVisibility(View.VISIBLE);
                }
                if (list.size() > 0) {
                    FileInfo info = list.get(0);
                    filePath = info.filePath;
                    fileName = info.fileName;
                }
                progress.setVisibility(View.GONE);
                adapter = new ChooseFileAdapter(mContext, list);
                listView.setAdapter(adapter);

            }
        });
    }


    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                closePage();
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileInfo info = (FileInfo) parent.getItemAtPosition(position);
                filePath = info.filePath;
                fileName = info.fileName;
                closePage();
            }
        });
    }

    private void closePage() {
        if (filePath != null) {
            Intent intent = new Intent(ChooseFileActivity.this, MainActivity.class);
            intent.putExtra("FilePath", filePath);
            intent.putExtra("FileName", fileName);
            setResult(1, intent);
        }
        finish();
    }


}
