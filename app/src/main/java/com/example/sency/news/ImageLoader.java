package com.example.sency.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sency on 2016/7/26.
 */
//用来处理图片的加载
public class ImageLoader {
    private ImageView mImageView;
    private String mUrl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    //通过线程获取图片
    public void showImageByThread(final ImageView imageView, final String url) {
        mImageView = imageView;
        mUrl = url;
        new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromURL(url);
                Message message = Message.obtain();
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }.start();
    }

    //通过url得到bitmap并返回
    public Bitmap getBitmapFromURL(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            conn.disconnect();
            //模拟网速不好
            //Thread.sleep(1000);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

   
}
