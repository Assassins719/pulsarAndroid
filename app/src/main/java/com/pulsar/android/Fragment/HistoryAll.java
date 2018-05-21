package com.pulsar.android.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pulsar.android.Activity.DashboardActivity;
import com.pulsar.android.Adapter.HistoryListAdapter;
import com.pulsar.android.Adapter.HistoryListAllAdapter;
import com.pulsar.android.GlobalVar;
import com.pulsar.android.Models.HistoryItem;
import com.pulsar.android.R;

import java.util.ArrayList;

public class HistoryAll extends Fragment {
    public HistoryAll() {
        // Required empty public constructor
    }

    ListView mListView;
    HistoryListAllAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_all, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initList();
    }

    public void initList() {
        mListView = getView().findViewById(R.id.list_view);
        ArrayList<HistoryItem> mTemp = new ArrayList<>();
        boolean isFirst = true;
        for (int i = 0; i < GlobalVar.mUnconfirmedDataAll.size(); i++) {
            HistoryItem mItem = GlobalVar.mUnconfirmedDataAll.get(i);
            mItem.setFirst(isFirst);
            mTemp.add(mItem);
            isFirst = false;
        }
        isFirst = true;
        for (int i = 0; i < GlobalVar.mHistoryDataAll.size(); i++) {
            HistoryItem mItem = GlobalVar.mHistoryDataAll.get(i);
            mItem.setFirst(isFirst);
            mTemp.add(mItem);
            isFirst = false;
        }
        adapter = new HistoryListAllAdapter(getActivity(), mTemp);
        mListView.setAdapter(adapter);
    }

    public void updateList() {
        if (adapter != null) {
            ArrayList<HistoryItem> mTemp = new ArrayList<>();
            boolean isFirst = true;
            for (int i = 0; i < GlobalVar.mUnconfirmedDataAll.size(); i++) {
                HistoryItem mItem = GlobalVar.mUnconfirmedDataAll.get(i);
                mItem.setFirst(isFirst);
                mTemp.add(mItem);
                isFirst = false;
            }
            isFirst = true;
            for (int i = 0; i < GlobalVar.mHistoryDataAll.size(); i++) {
                HistoryItem mItem = GlobalVar.mHistoryDataAll.get(i);
                mItem.setFirst(isFirst);
                mTemp.add(mItem);
                isFirst = false;
            }
            adapter.setData(mTemp);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        updateList();
        super.onResume();
    }
}
