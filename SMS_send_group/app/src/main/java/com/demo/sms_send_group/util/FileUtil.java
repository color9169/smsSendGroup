package com.demo.sms_send_group.util;

import android.os.AsyncTask;
import android.util.Log;

import com.demo.sms_send_group.entity.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by color on 17/2/28.
 */

public class FileUtil {

    private FileUtil() {
    }

    /**
     * 获取指定位置的指定类型的文件
     *
     * @param path 文件夹路径
     * @param type 文件类型（如“*.jpg;*.png;*.gif”）
     * @return
     */

    public static void getFileList(String path, String type,
                                   final OnFileListCallback onFileListCallback) {
        new AsyncTask<String, String, String>() {
            ArrayList<FileInfo> list = new ArrayList<>();

            @Override
            protected void onPostExecute(String result) {
                onFileListCallback.SearchFileListInfo(list);
            }

            @Override
            protected String doInBackground(String... params) {
                // TODO Auto-generated method stub
                String path = params[1].substring(params[1]
                        .lastIndexOf(".") + 1);


                File file = new File(params[0]);

                scanSDCard(file, path, list);

                return null;
            }
        }.execute(path, type, "");
    }


    /**
     * 扫描完成后的回调，获取文件列表必须实现
     *
     * @author cola
     */

    public interface OnFileListCallback {

        /**
         * 返回查询的文件列表
         *
         * @param list 文件列表
         */

        public void SearchFileListInfo(List<FileInfo> list);

    }


    private static void scanSDCard(File file, String ext, ArrayList<FileInfo> list) {

        if (file.isDirectory()) {

            File[] files = file.listFiles();

            if (files != null) {

                for (int i = 0; i < files.length; i++) {

                    File tmp = files[i];

                    if (tmp.isFile()) {

                        String fileName = tmp.getName();

                        String filePath = tmp.getPath();

                        if (fileName.indexOf(".") >= 0) {
//                            Log.i("FileUtil_to_demo", "fileName = " + fileName + "   filePath = " + filePath + "  fileName.substring(fileName.lastIndexOf(\".\"), fileName.length()) " + fileName.substring(fileName.lastIndexOf("."), fileName.length()));
                            String subName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
//                            Log.i("FileUtil_to_demo", "subName = " + subName + "    fileName = " + fileName
//                                    + "    filePath = " + filePath + "    ext  = " + ext);
                            if (subName.equals(".csv")) {
//                                fileName = fileName.substring(fileName
//                                        .lastIndexOf(".") + 1);

//                                if (ext != null && ext.equalsIgnoreCase(fileName)) {


                                FileInfo info = new FileInfo();

                                info.fileName = fileName;

                                info.filePath = tmp.getAbsolutePath();
                                Log.i("FileUtil_to_demo", "csv    fileName = " + fileName + "   filePath = " + filePath);
                                list.add(info);

//                                }
                            }
                        }

                    } else

                        scanSDCard(tmp, ext, list);

                }

            }

        } else {

            if (file.isFile()) {

                String fileName = file.getName();

                String filePath = file.getName();

                if (fileName.indexOf(".") >= 0) {

                    fileName = fileName

                            .substring(fileName.lastIndexOf(".") + 1);

                    if (ext != null && ext.equalsIgnoreCase(fileName)) {


                        FileInfo info = new FileInfo();

                        info.fileName = filePath;

                        info.filePath = file.getAbsolutePath();

                        list.add(info);

                    }

                }

            }

        }

    }


}
