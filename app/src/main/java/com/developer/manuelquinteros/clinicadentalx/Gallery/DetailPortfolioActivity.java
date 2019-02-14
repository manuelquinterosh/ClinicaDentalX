package com.developer.manuelquinteros.clinicadentalx.Gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.developer.manuelquinteros.clinicadentalx.R;
import com.developer.manuelquinteros.clinicadentalx.model.Gallery;

import java.util.ArrayList;

public class DetailPortfolioActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

   public ArrayList<Gallery> data = new ArrayList<>();
   int pos;



   private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_galery_detail);
        setSupportActionBar(toolbar);

        data = getIntent().getParcelableArrayListExtra("data");
        pos = getIntent().getIntExtra("pos", 0);

        setTitle(data.get(pos).getName());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), data);

        mViewPager = (ViewPager) findViewById(R.id.container_page);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(pos);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
               setTitle(data.get(i).getName());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public ArrayList<Gallery> data = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Gallery> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int i) {
            return PlaceholderFragment.newInstance(i, data.get(i).getName(), data.get(i).getUrl());
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position).getName();
        }
    }

    public static class PlaceholderFragment extends Fragment {
        String name, url;
        int pos;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_IMG_TITLE = "image_title";
        private static final String ARG_IMG_URL = "image_url";

        @Override
        public void setArguments(@Nullable Bundle args) {
            super.setArguments(args);
            this.pos = args.getInt(ARG_SECTION_NUMBER);
            this.name = args.getString(ARG_IMG_TITLE);
            this.url = args.getString(ARG_IMG_URL);
        }

        public static PlaceholderFragment newInstance(int sectionNumber, String name, String url) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_IMG_TITLE, name);
            args.putString(ARG_IMG_URL, url);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onStart() {
            super.onStart();

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail_gallery, container, false);

            final ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_gallery);

            Glide.with(getActivity()).load(url).thumbnail(0.1f).into(imageView);

            return rootView;
        }

    }
}
