package com.marvel.jukebox;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String TAG = "marvel";
    Close close;
     TabLayout mTabLayout;
     ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        close = new Close(this);

        ActionBar actionBar = getSupportActionBar();
        Drawable logo = getDrawable(R.drawable.logo_title);
        Bitmap bitmap = ((BitmapDrawable) logo).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 120, 120, true));
        actionBar.setLogo(d);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle(R.string.title);
        actionBar.setElevation(0);

        mTabLayout = findViewById(R.id.tab);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createTabView()));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createTabView()));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createTabView()));

        vp = findViewById(R.id.vp);
        ContentsPagerAdapter adapter = new ContentsPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.music).setVisibility(View.GONE);
                tab.getCustomView().findViewById(R.id.music_on).setVisibility(View.VISIBLE);
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.music).setVisibility(View.VISIBLE);
                tab.getCustomView().findViewById(R.id.music_on).setVisibility(View.GONE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.music).setVisibility(View.GONE);
                tab.getCustomView().findViewById(R.id.music_on).setVisibility(View.VISIBLE);
            }
        });
        vp.setOffscreenPageLimit(3);
        mTabLayout.getTabAt(0).select();
    }

    private View createTabView() {
        View tabView = LayoutInflater.from(this).inflate(R.layout.item_tab,null);
        return tabView;
    }

    @Override
    public void onBackPressed() {
        close.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.menu0:
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
        private int mPageCount;

        public ContentsPagerAdapter(FragmentManager fm, int pageCount) {
            super(fm);
            this.mPageCount = pageCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MyFragment frag0 = MyFragment.create();
                    return frag0;
                case 1:
                    MyFragment frag1 = MyFragment.create();
                    return frag1;
                case 2:
                    MyFragment frag2 = MyFragment.create();
                    return frag2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mPageCount;
        }
    }

}
