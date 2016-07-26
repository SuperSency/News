package com.example.sency.news;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ListView mListView;
    private static String url = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        mListView = (ListView) findViewById(R.id.list);
        //将url作为参数传入并执行异步
        new MyAsyncTask().execute(url);
    }

    /**
     * 实现网络的异步访问
     * 第一个参数为我要使用的数据类型，即doInbackground中的参数，这里是url所以为String类型
     * 第二个参数为过程中使用的类型；
     * 第三个参数为结果返回类型，即doInBackground返回的数据类型，作为onPostExecute中的参数
     */
    class MyAsyncTask extends AsyncTask<String, Void, List<ItemBean>> {

        @Override
        protected List<ItemBean> doInBackground(String... params) {
            //获取数据并进行json解析
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<ItemBean> itemBeans) {
            super.onPostExecute(itemBeans);
            //将得到的封装好的数据作为参数传入，通过适配器进行适配
            NewsAdapter adapter = new NewsAdapter(MainActivity.this,itemBeans);
            mListView.setAdapter(adapter);
        }
    }

    /**
     * 将url所对应的JSON格式数据转换为我们所封装的ItemBean对象
     * @param url
     * @return
     */
    private List<ItemBean> getJsonData(String url) {
        List<ItemBean> newsList = new ArrayList<>();
        try {
            //使用自己写的readStream方法获取数据
            String jsonString = readStream(new URL(url).openStream());
            Log.d("jsonString",jsonString);
            //获取json数据
            JSONObject jsonObject;
            ItemBean itemBean ;
            jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                itemBean = new ItemBean();
                itemBean.imageUrl = jsonObject.getString("picSmall");
                itemBean.title = jsonObject.getString("name");
                itemBean.content = jsonObject.getString("description");
                //将封装好的数据添加到List中
                newsList.add(itemBean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //返回List
        return newsList;
    }

    /**
     * 通过InputStream解析网页返回的数据
     */
    public String readStream(InputStream is) {
        InputStreamReader isr;
        String result = "";
        try {
            isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}
