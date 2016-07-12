package com.dahoraapps.snowmanfinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dahoraapps.snowmanfinder.R;
import com.dahoraapps.snowmanfinder.activities.MainActivity;
import com.dahoraapps.snowmanfinder.models.Snowman;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SnowmanInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    @InjectView(R.id.photo)
    public ImageView photo;
    @InjectView(R.id.name)
    public TextView name;
    @InjectView(R.id.button)
    public CheckBox button;

    private List<Snowman> snowmen;
    private MainActivity mActivity;

    public SnowmanInfoWindowAdapter(List<Snowman> snowmen, MainActivity mActivity) {
        this.snowmen = snowmen;
        this.mActivity = mActivity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Integer position = Integer.valueOf(marker.getTitle());
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_snowman_marker, null);
        ButterKnife.inject(this, view);
        final Snowman snowman = snowmen.get(position);
        name.setText(snowman.getName());
        Picasso.with(mActivity).load(snowman.getPhoto())
                .resize(50,50)
                .centerCrop()
                .into(photo);
        if(snowman.getFavorite()){
            button.setChecked(true);
        }else{
            button.setChecked(false);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(snowman.getFavorite()){
                    snowman.setFavorite(false);
                    snowman.delete();
                }else{
                    snowman.setFavorite(true);
                    snowman.save();
                }
            }
        });
        return view;
    }

}
