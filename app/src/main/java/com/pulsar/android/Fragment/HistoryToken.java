package com.pulsar.android.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.pulsar.android.Adapter.HistoryListAdapter;
import com.pulsar.android.GlobalVar;
import com.pulsar.android.R;
import com.pulsar.android.components.UltraPagerAdapter;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;

public class HistoryToken extends Fragment {
    UltraViewPager ultraViewPager;
    ListView mListView;
    SearchView sv_key;

    public HistoryToken() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_token, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initSlider();
        initList();
    }
    public void initList(){
        mListView = getView().findViewById(R.id.list_view);
        sv_key = getView().findViewById(R.id.search_view);
        final HistoryListAdapter adapter=new HistoryListAdapter(getActivity(), GlobalVar.mHistoryData);
        mListView.setAdapter(adapter);
        sv_key.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // TODO Auto-generated method stub

                adapter.getFilter().filter(query);

                return false;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initSlider(){
        ultraViewPager = getView().findViewById(R.id.ultra_viewpager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        PagerAdapter adapter = new UltraPagerAdapter(true, getActivity());
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.initIndicator();
        ultraViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(getActivity().getColor(R.color.colorTabSelect))
                .setNormalColor(getActivity().getColor(R.color.colorSpliter))
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        ultraViewPager.getIndicator().build();
        ultraViewPager.setInfiniteLoop(false);
        ultraViewPager.setMultiScreen(0.6f);
        ultraViewPager.setItemRatio(1.0f);
        ultraViewPager.setRatio(2.0f);
        ultraViewPager.setAutoMeasureHeight(true);
        ultraViewPager.setPageTransformer(false, new UltraScaleTransformer());
        ultraViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                // Check if this is the page you want.
                Log.d("Page","changed" + position);
            }
        });
    }
}
