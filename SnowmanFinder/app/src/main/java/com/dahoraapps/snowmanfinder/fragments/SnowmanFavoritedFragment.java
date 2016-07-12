package com.dahoraapps.snowmanfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.dahoraapps.snowmanfinder.R;
import com.dahoraapps.snowmanfinder.adapters.SnowmanFavoritedRecyclerViewAdapter;
import com.dahoraapps.snowmanfinder.adapters.SnowmanRecyclerViewAdapter;
import com.dahoraapps.snowmanfinder.models.Snowman;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SnowmanFavoritedFragment extends Fragment {

    public SnowmanFavoritedFragment() {
    }

    public static SnowmanFavoritedFragment newInstance() {
        return new SnowmanFavoritedFragment();
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
            List<Snowman> snowmen = getAllSnowmen();
            if(snowmen==null)
                snowmen = new ArrayList<>();
            recyclerView.setAdapter(new SnowmanFavoritedRecyclerViewAdapter(snowmen, getContext()));
        }
        return view;
    }

    public List<Snowman> getAllSnowmen() {
        return new Select()
                .from(Snowman.class)
                .orderBy("name ASC")
                .execute();
    }

}
