package com.example.retrofitopenweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.retrofitopenweather.weather.WeatherResponse;
import com.example.retrofitopenweather.weather.WeatherService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String GoogleApiKey = "AIzaSyABSkYsO-tCIDeDfHm2SpeXbppxCewnEvs";
    private final String BaseUrl = "https://api.openweathermap.org/";
    private final String OpenWeatherApiKey = "e06d840f6a76c90c14d26bd50a696187";
    private final String Units = "metric";

    private TextView latLonText;
    private ImageButton btn;
    private EditText editText;

    private Double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.searchButton);
        editText = findViewById(R.id.cityName);
        latLonText = findViewById(R.id.latLonText);

        Places.initialize(getApplicationContext(), GoogleApiKey);

        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fieldList).build(MainActivity.this);

                startActivityForResult(intent,100);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String coordinates;
        if(requestCode == 100 && resultCode == RESULT_OK){

            Place place = Autocomplete.getPlaceFromIntent(data);

            editText.setText(place.getAddress());

            LatLng latLng = place.getLatLng();
            lat = latLng.latitude;
            lon = latLng.longitude;

            coordinates = "lat: " + lat + "\nlon: " + lon;
            latLonText.setText(coordinates);
        }
    }

    private void getCurrentData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);

        Call<WeatherResponse> call = service.getByLatLon(lat,lon,OpenWeatherApiKey,Units);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {

                    // successfully found a location

                    WeatherResponse.Response = response.body();
                    assert WeatherResponse.Response != null;

                } else {
                    // wrong location name
                    WeatherResponse.Response = null;
                }
                MoveToNextActivity();
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {

            }
        });
    }

    private void MoveToNextActivity(){
        Intent intent = new Intent(this,WeatherActivity.class);
        startActivity(intent);
    }
}