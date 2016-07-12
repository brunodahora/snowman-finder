package com.dahoraapps.snowmanfinder.helpers;

import com.dahoraapps.snowmanfinder.dtos.SnowmanDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("snowmen")
    Call<SnowmanDTO> getSnowmen(@Query("latitude") Double latitude, @Query("longitude") Double longitude, @Query("radius") Integer radius);

}
