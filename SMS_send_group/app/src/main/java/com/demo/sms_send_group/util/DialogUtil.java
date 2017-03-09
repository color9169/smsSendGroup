package com.demo.sms_send_group.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.demo.sms_send_group.listener.WhetherListener;


/**
 * Created by Administrator on 2016/2/19.
 */
public class DialogUtil {

    /**
     * 打开一个加载提示框
     *
     * @return
     */

    private static Dialog noticeDialog;

    private static boolean isShowDialog = true;


    /**
     * 申请权限
     *
     * @param activity
     * @param title
     * @param content
     * @param confirm
     * @param cancelStr
     * @param listener
     */
    public static void toSetPermission(final Activity activity, String title, String content, String confirm, String cancelStr, final WhetherListener listener) {

        if (isShowDialog) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setMessage(content);
            // 更新

            builder.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dissmissDialog();
                    isShowDialog = true;
                    listener.determine();
//                    BaseApplication.addOrderActivity(activity);
//                    BaseApplication.clearActivityList(BaseApplication.hotelOrderList);
                }
            });
            builder.setNegativeButton(cancelStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dissmissDialog();
                    isShowDialog = true;
                    listener.cancel();
                }
            });

            noticeDialog = builder.create();
            noticeDialog.setCancelable(false);
            noticeDialog.show();
            isShowDialog = false;
        }
    }

    public static void dissmissDialog() {
        if (noticeDialog != null) {
            noticeDialog.setCancelable(true);
            noticeDialog.dismiss();
            isShowDialog = true;
        }
    }


}
