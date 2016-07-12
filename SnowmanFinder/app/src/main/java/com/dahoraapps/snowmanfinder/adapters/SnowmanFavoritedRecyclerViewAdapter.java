package com.dahoraapps.snowmanfinder.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dahoraapps.snowmanfinder.R;
import com.dahoraapps.snowmanfinder.models.Snowman;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SnowmanFavoritedRecyclerViewAdapter extends RecyclerView.Adapter<SnowmanFavoritedRecyclerViewAdapter.ViewHolder> {

    private final List<Snowman> mValues;
    private Context mContext;

    public SnowmanFavoritedRecyclerViewAdapter(List<Snowman> items, Context context) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_snowman_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.snowman = mValues.get(position);
        holder.name.setText(mValues.get(position).getName());
        Picasso.with(mContext).load(mValues.get(position).getPhoto())
                .resize(100,100)
                .centerCrop()
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @InjectView(R.id.photo)
        public ImageView photo;
        @InjectView(R.id.name)
        public TextView name;
        public Snowman snowman;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.inject(this, view);
        }
    }
}
