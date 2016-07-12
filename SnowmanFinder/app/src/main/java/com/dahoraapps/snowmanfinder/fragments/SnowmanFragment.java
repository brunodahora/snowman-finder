package com.dahoraapps.snowmanfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dahoraapps.snowmanfinder.R;
import com.dahoraapps.snowmanfinder.adapters.SnowmanRecyclerViewAdapter;
import com.dahoraapps.snowmanfinder.dtos.SnowmanDTO;
import com.dahoraapps.snowmanfinder.helpers.ApiHelper;
import com.dahoraapps.snowmanfinder.models.Snowman;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnowmanFragment extends Fragment implements Callback<SnowmanDTO> {

    private static final String ARG_SNOWMEN = "snowmen";
    private List<Snowman> mSnowmen;
    private SnowmanRecyclerViewAdapter adapter;

    public SnowmanFragment() {
    }

    public static SnowmanFragment newInstance(List<Snowman> snowmen) {
        SnowmanFragment fragment = new SnowmanFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SNOWMEN, (Serializable) snowmen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApiHelper.getApi().getSnowmen(-25.455906,-49.275269, 10000).enqueue(this);
        mSnowmen = new ArrayList<>();

//        if (getArguments() != null) {
//            mSnowmen = (List<Snowman>) getArguments().getSerializable("snowmen");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snowman_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new SnowmanRecyclerViewAdapter(mSnowmen, getContext());
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onResponse(Call<SnowmanDTO> call, Response<SnowmanDTO> response) {
        mSnowmen = response.body().getResults();
        if(adapter!=null) {
            adapter.setValues(mSnowmen);
            adapter.notifyDataSetChanged();
        }else{
            adapter = new SnowmanRecyclerViewAdapter(mSnowmen, getContext());
        }
    }

    @Override
    public void onFailure(Call<SnowmanDTO> call, Throwable t) {

    }
}
