package com.dahoraapps.snowmanfinder.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dahoraapps.snowmanfinder.R;
import com.dahoraapps.snowmanfinder.models.Snowman;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SnowmanRecyclerViewAdapter extends RecyclerView.Adapter<SnowmanRecyclerViewAdapter.ViewHolder> {

    private List<Snowman> mValues;
    private Context mContext;

    public SnowmanRecyclerViewAdapter(List<Snowman> items, Context context) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_snowman, parent, false);
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
        if(holder.snowman.getFavorite()){
            holder.button.setChecked(true);
        }else{
            holder.button.setChecked(false);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.snowman.getFavorite()){
                    holder.snowman.setFavorite(false);
                    holder.snowman.delete();
                }else{
                    holder.snowman.setFavorite(true);
                    holder.snowman.save();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setValues(List<Snowman> mValues) {
        this.mValues = mValues;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @InjectView(R.id.photo)
        public ImageView photo;
        @InjectView(R.id.name)
        public TextView name;
        @InjectView(R.id.button)
        public CheckBox button;
        public Snowman snowman;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.inject(this, view);
        }
    }
}
