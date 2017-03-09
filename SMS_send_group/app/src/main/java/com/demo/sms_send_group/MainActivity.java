package com.demo.sms_send_group;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.sms_send_group.entity.SmsEntity;
import com.demo.sms_send_group.util.CsvFileUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.send_sms_content_edit)
    EditText sendSmsContentEdit;
    @Bind(R.id.send_sms_btn)
    ImageView sendSmsBtn;
    @Bind(R.id.file_name_txt)
    TextView fileNameTxt;
    @Bind(R.id.open_mail_list)
    Button openMailList;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    @Bind(R.id.send_progress)
    ProgressBar sendProgress;


    List<SmsEntity> lists;
    String filePath;
    String fileName;
    private Context mContext;

    /**
     * 发送与接收的广播
     **/
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initPermission();
        init();
        setListener();
    }

    private void init() {
        lists = new ArrayList<>();
        // 注册广播 发送消息
//        registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
//        registerReceiver(receiver, new IntentFilter(DELIVERED_SMS_ACTION));
    }


    /**
     * 初始化权限
     */
    protected void initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            permissions = new String[]{
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    private void setListener() {
        openMailList.setOnClickListener(this);
        sendSmsBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_mail_list:
                Intent intent = new Intent(MainActivity.this, ChooseFileActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.send_sms_btn:
                if (filePath != null) {
                    mHandler.sendEmptyMessage(1);
                } else {
                    Toast.makeText(getApplicationContext(), "请先选择文件", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(sendMessage);
//        unregisterReceiver(receiver);
    }

    /**
     * 追加联系人信息到集合中
     *
     * @param filePath
     */
    private void doMethod(String filePath) {
        List<String[]> csv = CsvFileUtil.readCsvFile(filePath, "GBK", false);
        try {
            long l = Long.parseLong(csv.get(0)[1]);
        } catch (Exception e) {
            csv.clear();
            csv = CsvFileUtil.readCsvFile(filePath, "GBK", true);
        }

        for (int i = 0; i < csv.size(); i++) {
            String[] s = csv.get(i);
            SmsEntity entity = new SmsEntity();

            entity.setName(s[0]);
            entity.setPhoneNumber(s[1]);

            lists.add(entity);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:

                filePath = data.getStringExtra("FilePath");
                fileName = data.getStringExtra("FileName");
                fileNameTxt.setText("文件名称：" + fileName);
                fileNameTxt.setVisibility(View.VISIBLE);
                doMethod(filePath);

                break;
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    sendProgress.setVisibility(View.VISIBLE);

                    String contentStr = sendSmsContentEdit.getText().toString();
                    for (int i = 0; i < lists.size(); i++) {
                        SmsEntity s = lists.get(i);
                        String content = contentStr.replace("xxx", s.getName());
                        String phone = s.getPhoneNumber();

                        SmsManager manager = SmsManager.getDefault();

                        // create the sentIntent parameter
                        Intent sentIntent = new Intent(SENT_SMS_ACTION);
                        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, sentIntent, 0);

                        // create the deilverIntent parameter
                        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
                        PendingIntent deliverPI = PendingIntent.getBroadcast(mContext, 0, deliverIntent, 0);

                        if (content.length() > 70) {
                            ArrayList<String> list = manager.divideMessage(content);  //因为一条短信有字数限制，因此要将长短信拆分
                            for (String text : list) {
                                manager.sendTextMessage(phone, null, text, sentPI, deliverPI);
                            }
                        } else {
                            manager.sendTextMessage(phone, null, content, sentPI, deliverPI);
                        }
                    }
                    mHandler.sendEmptyMessage(2);
                    break;

                case 2:
                    sendProgress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "发送完毕", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

//    private BroadcastReceiver sendMessage = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //判断短信是否发送成功
//            switch (getResultCode()) {
//                case Activity.RESULT_OK:
//                    Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    Toast.makeText(mContext, "发送失败", Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    };
//
//
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //表示对方成功收到短信
//            Toast.makeText(mContext, "对方接收成功", Toast.LENGTH_LONG).show();
//        }
//    };

}
