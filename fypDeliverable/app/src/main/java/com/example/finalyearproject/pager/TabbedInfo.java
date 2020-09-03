package com.example.finalyearproject.pager;

import android.os.AsyncTask;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.finalyearproject.R;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finalyearproject.pager.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class TabbedInfo extends AppCompatActivity {

    private static Map<String, String> webScrape;
    private static String webString;

    private ImageView image;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbed_info);

        Bundle bundle = getIntent().getExtras();
        String thumbnail = bundle.getString("thumbnailURL");
        image = findViewById(R.id.parallaxImage);
        Glide.with(getApplicationContext()).load(thumbnail).into(image);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        getText();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        // TODO
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    public void getText() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        if (webScrape.size() > 0) {
            Iterator it = webScrape.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = (Map.Entry) it.next();

                String x = pair.getKey();
                String y = pair.getValue();

                adapter.addFrag(new TabFragment(y), x.substring(0, x.length() - 6));

            }
        } else {

            adapter.addFrag(new TabFragment(webString), "Overview");
        }
        viewPager.setAdapter(adapter);
    }

    public static void setMapContent(Map<String, String> x) {
        webScrape = x;
    }

    public static void setStringContent(String x) {
        webString = x;
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        webString = "";
        webScrape.clear();
        finish();
        return;
    }



}

