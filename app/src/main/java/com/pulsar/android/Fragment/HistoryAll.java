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
import com.pulsar.android.GlobalVar;
import com.pulsar.android.R;

public class HistoryAll extends Fragment {
    public HistoryAll() {
        // Required empty public constructor
    }
    ListView mListView;
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
    public void initList(){
        mListView = getView().findViewById(R.id.list_view);
        final HistoryListAdapter adapter=new HistoryListAdapter(getActivity(), GlobalVar.mHistoryData);
        mListView.setAdapter(adapter);
    }
}
