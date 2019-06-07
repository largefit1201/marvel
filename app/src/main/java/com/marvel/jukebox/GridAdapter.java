package com.marvel.jukebox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inf;
    ArrayList<ItemMusic> items;

    public GridAdapter(Context context, ArrayList<ItemMusic> items) {
        this.context = context;
        this.items = items;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) view = inf.inflate(R.layout.item_grid, null);
        //ImageView iv = view.findViewById(R.id.imageView1);
        //iv.setImageResource(img[i]);
        TextView name = view.findViewById(R.id.name);
        name.setText(items.get(i).name);

        return view;
    }
}
