package com.shuai.android.textshow;

import android.app.ActivityManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements Runnable {
    public static final String TAG = MainActivity.class.getSimpleName();
    private MoreTextView mTextView;
    private Thread mThread=new Thread(this);
    private RandomAccessFile mRandomAccessFile;
    private int mLineBytes;                         //一行占多少字节
    private int mCurrentShow;                       //当前展示

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 0:
                    String s = (String) msg.obj;
                    mTextView.setText(s);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (MoreTextView) findViewById(R.id.mTextView);
        printMemory();
        loadData();

        mTextView.setScrollListener(new MoreTextView.TextScrollListener() {
            @Override
            public void scorll(float y,float height) {
                //滑动Y轴的距离
                if(y>0) {
                    mCurrentShow = (int) (y / height);
                }else {
                    mCurrentShow=0;
                }
                Log.e("y",""+y);
                Log.e("height",""+height);
                Log.e("mCurrentShow",""+mCurrentShow);
                mThread.start();
            }
        });
    }

    private void loadData() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "shuai.txt");
            if (!file.exists())
                file.createNewFile();
            mRandomAccessFile = new RandomAccessFile(file, "r");
            mThread.start();
            printMemory();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(byte[] bytes) {
        Message message = new Message();
        try {
            message.obj = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.what = 0;
        mHandler.sendMessage(message);
    }


    public void printMemory() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        Log.e(TAG, "系统总内存:" + (info.totalMem >> 10) + "KB");
        Log.e(TAG, "系统剩余内存:" + (info.availMem >> 10) + "KB");
    }

    @Override
    public void run() {
        byte[] bytes = new byte[4096];
        try {
            int start=mCurrentShow>1?((mCurrentShow-1)*1024):0;
            mRandomAccessFile.read(bytes,start,4096);
            mRandomAccessFile.seek(bytes.length);
            sendMessage(bytes);
        }catch (Exception e){
            e.fillInStackTrace();
        }
        sendMessage(bytes);
        printMemory();
    }


}
