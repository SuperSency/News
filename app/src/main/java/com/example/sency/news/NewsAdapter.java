package com.example.sency.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sency on 2016/7/26.
 */
public class NewsAdapter extends BaseAdapter {
    private List<ItemBean> lists;
    LayoutInflater inflater;

    public NewsAdapter(Context context, List<ItemBean> data) {
        //将传入的List集合中的数据放入lists中，用于后面的item中控件的设置
        lists = data;
        inflater = LayoutInflater.from(context);
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
         new ImageLoader().showImageByThread(viewHolder.imageView, lists.get(i).imageUrl);
       //设置tag避免图片混乱错位,在ImageLoader中使用
        viewHolder.imageView.setTag(lists.get(i).imageUrl);
        viewHolder.title.setText(lists.get(i).title);
        viewHolder.content.setText(lists.get(i).content);
        return view;
    }
}

class ViewHolder {
    ImageView imageView;
    TextView title;
    TextView content;
}
