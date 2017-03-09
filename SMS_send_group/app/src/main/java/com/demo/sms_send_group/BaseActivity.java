package com.demo.sms_send_group;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.demo.sms_send_group.listener.WhetherListener;
import com.demo.sms_send_group.util.DialogUtil;

/**
 * Created by color on 17/3/2.
 */

public class BaseActivity extends AppCompatActivity {


    protected String[] permissions;  //申请的权限组
    protected boolean outrightBan = false; //彻底禁止
    protected boolean notOutrightBan = false; // 没彻底禁止

    /**
     * 初始化权限
     */
    protected void initPermission() {
        if (Build.VERSION.SDK_INT >= 23)
            ActivityCompat.requestPermissions(this, permissions, 1);
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
//
//                } else {
//                }
//            }
    }

    /**
     * 权限回调监听
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:

                for (int i = 0; i < permissions.length; i++) {
                    boolean tag = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);

                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        if (tag) { //表明用户没有彻底禁止弹出权限请求
//                            Toast.makeText(this, "没有彻底禁止，只是点击了拒绝" + grantResults[i] + "   " + i, Toast.LENGTH_SHORT).show();

//                            builder.show();
                            outrightBan = true;

                        } else { // 表明用户已经彻底禁止弹出权限请求
                            notOutrightBan = true;

                        }
                    }
                }

                if (notOutrightBan && outrightBan) {
                    DialogUtil.toSetPermission(this, getString(R.string.permission_prompt), getString(R.string.permission_notification), getString(R.string.go_to_settings), getString(R.string.cancel), new WhetherListener() {
                                @Override
                                public void determine() {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS); //跳转到该包名的设置界面
                                    intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                                    startActivity(intent);
                                    DialogUtil.dissmissDialog();
                                }

                                @Override
                                public void cancel() {
                                    DialogUtil.dissmissDialog();
                                }
                            }

                    );
                } else if (notOutrightBan) {
                    DialogUtil.toSetPermission(this, getString(R.string.permission_prompt), getString(R.string.permission_notification), getString(R.string.go_to_settings), getString(R.string.cancel), new WhetherListener() {
                                @Override
                                public void determine() {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS); //跳转到该包名的设置界面
                                    intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                                    startActivity(intent);
                                    DialogUtil.dissmissDialog();
                                }

                                @Override
                                public void cancel() {
                                    DialogUtil.dissmissDialog();
                                }
                            }

                    );
                } else if (outrightBan) {
                    DialogUtil.toSetPermission(this, getString(R.string.permission_prompt), getString(R.string.permission_refused), getString(R.string.go_to_allow), getString(R.string.cancel), new WhetherListener() {
                        @Override
                        public void determine() {
                            initPermission();
                            DialogUtil.dissmissDialog();
                        }

                        @Override
                        public void cancel() {
                            DialogUtil.dissmissDialog();
                        }
                    });

                }

                outrightBan = false;
                notOutrightBan = false;

                break;
        }
    }
}
