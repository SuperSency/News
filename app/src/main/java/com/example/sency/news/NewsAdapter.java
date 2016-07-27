package com.example.sency.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sency on 2016/7/26.
 */
public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private List<ItemBean> lists;
    LayoutInflater inflater;
    private ImageLoader imageLoader;
    private int mStart;
    private int mEnd;
    public static String[] URLS;
    private boolean mFirstIn;

    public NewsAdapter(Context context, List<ItemBean> data, ListView listView) {
        //将传入的List集合中的数据放入lists中，用于后面的item中控件的设置
        lists = data;
        inflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(listView);
        URLS = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            URLS[i] = data.get(i).imageUrl;
        }
        mFirstIn = true;
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.pic);
            viewHolder.title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.content = (TextView) view.findViewById(R.id.tv_content);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //默认图片
        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
        //使用线程设置图片
        // new ImageLoader().showImageByThread(viewHolder.imageView, lists.get(i).imageUrl);
        //使用AsyncTask设置图片
        //new ImageLoader().showImageByAsyncTask(viewHolder.imageView, lists.get(i).imageUrl);
        imageLoader.showImageByAsyncTask(viewHolder.imageView, lists.get(i).imageUrl);
        //设置tag避免图片混乱错位,在ImageLoader中使用
        viewHolder.imageView.setTag(lists.get(i).imageUrl);
        viewHolder.title.setText(lists.get(i).title);
        viewHolder.content.setText(lists.get(i).content);
        return view;
    }

    //在ListView滑动状态更改时才会调用
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        //如果是停止状态
        if (scrollState == SCROLL_STATE_IDLE) {
            //加载可见项
            imageLoader.loadImages(mStart, mEnd);
        } else {
            //其他状态
            //停止任务
            imageLoader.cancelAllTask();
        }
    }

    //整个过程中都会调用
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int endVisibleItem) {
        System.out.println("ccccccc");
        /**获取ListView的起始项*/
        //第一个可见元素
        mStart = firstVisibleItem;
        //第一个可见元素加上可见元素数量
        mEnd = firstVisibleItem + visibleItemCount;
        System.out.println("hahhahahahaah");
        //第一次显示即刚进入界面是调用
        if (mFirstIn == true && visibleItemCount > 0) {
            imageLoader.loadImages(mStart, mEnd);
            mFirstIn = false;
        }
    }
}

class ViewHolder {
    ImageView imageView;
    TextView title;
    TextView content;
}
