package com.marvel.jukebox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MyFragment extends Fragment {

    public static MyFragment create() {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        //args.putParcelableArrayList("image", image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //image = getArguments().getInt("image");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.my_fragment, container, false);

        ArrayList<ItemMusic> items = new ArrayList<>();
        items.add(new ItemMusic());
        items.add(new ItemMusic());
        GridAdapter adapter = new GridAdapter(getContext(), items);
        GridView gv = rootView.findViewById(R.id.gv);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });

        return rootView;
    }
}