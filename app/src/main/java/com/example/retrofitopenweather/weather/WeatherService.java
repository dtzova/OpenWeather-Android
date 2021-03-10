package com.example.retrofitopenweather.weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("data/2.5/weather?")
    Call<WeatherResponse> getByName(@Query("q") String cityName, @Query("APPID") String app_id, @Query("units") String units);

    @GET("data/2.5/weather?")
    Call<WeatherResponse> getByLatLon(@Query("lat") Double lat, @Query("lon") Double lon, @Query("APPID") String app_id, @Query("units") String units);
}
