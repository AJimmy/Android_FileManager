package com.alice.filemanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 1. 制作一个文件管理器
 * 现整体后细节，能够列出当前目录的所有文件和文件夹，文件夹名称+“/”
 * 点击文件夹，内容刷新为指定文件夹内的内容，包含文件、文件夹。isDirectory() isFile()
 * 每个文件夹中包含 “..”。
 * 点击返回上级目录。getParent();
 * 扩展：点击文件，使用隐式意图ACTION_VIEW打开文件。
 * 自定义适配器，显示图片
 */
public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private List<String> fileList;//用于存储目录下的文件及文件夹
    private ArrayAdapter<String> adapter;
    private String[] filesArray;
    private File parentFile;
    private TextView txt;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fileList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileList);
        ListView listView = (ListView) findViewById(R.id.listView);
        txt = (TextView) findViewById(R.id.txt);
        //获取SD卡根目录
        File externalStorage = Environment.getExternalStorageDirectory();
        parentFile = externalStorage;
        txt.setText(parentFile.toString());
        //获取根目录下的File 列表
        filesArray = externalStorage.list();

        fileList.add("../");
        for (String s : filesArray) {
            if (new File(externalStorage, s).isDirectory()) {
                fileList.add(s + "/");
            } else {
                fileList.add(s);
            }
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file;
        if (fileList.get(position).equals("..")) {//上一级目录
            if (parentFile.toString().equals("/")) {//已经到达根目录
                return;
            }
            file = parentFile.getParentFile();
        } else {
            file = new File(parentFile, fileList.get(position));
        }
        if (file.isDirectory()) {//是目录，点击加载该目录下的文件和文件夹
            fileList.clear();//清除原有目录
            filesArray = file.list();

            fileList.add("../");
            for (String s : filesArray) {
                if (new File(file, s).isDirectory()) {
                    fileList.add(s + "/");
                } else {
                    fileList.add(s);
                }
            }

            parentFile = file;
            adapter.notifyDataSetChanged();
            txt.setText(file.toString());
        } else if (file.isFile()) {//点击是文件，打开文件

            //TODO 压缩文件、图片文件查看此处出现异常，所以使用try-catch结构
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String selectType = "*/*";
            if (file.toString().endsWith("txt") || file.toString().endsWith("xml")) {
                selectType = "text/plain";
            } else if (file.toString().endsWith("png") || file.toString().endsWith("jpg")) {
                selectType = "image/*";
            } else if (file.toString().endsWith("mp4")) {
                selectType = "video/*";
            }
            intent.setDataAndType(Uri.fromFile(file), selectType);
            startActivity(intent);
        }

        Log.e(TAG, file.toString());
        Log.e(TAG, "parent " + parentFile);
        Log.e(TAG, "file.isDirectory()" + file.isDirectory());
        Log.e(TAG, "file.isFile()" + file.isFile());
    }
}
