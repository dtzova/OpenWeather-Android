package com.example.retrofitopenweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrofitopenweather.weather.WeatherResponse;

public class WeatherActivity extends AppCompatActivity {

    private final String ErrorMessage = "Cannot find such place!";

    private TextView textMain;
    private TextView textDetailed;
    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        textMain = findViewById(R.id.weatherMain);
        textDetailed = findViewById(R.id.weatherDetailed);
        icon = findViewById(R.id.weatherIcon);

        if (WeatherResponse.Response != null) {

            WeatherResponse response = WeatherResponse.Response;
            MainResponse(response);

            String details = DetailedResponse(response);
            textDetailed.setText(details);

        } else {
            WrongLocation();
        }
    }

    private void WrongLocation() {
        textMain.setText(ErrorMessage);
        textDetailed.setText("");
        icon.setImageResource(0);
    }

    private void MainResponse(WeatherResponse response) {

        WeatherResponse.Weather weather = response.weather.get(0);
        String status = weather.main;

        String message = response.name.toUpperCase() +
                ": " + ((int) Math.round(response.main.temp)) +
                "°\n\n" + status +
                "\n" + weather.description;

        textMain.setText(message);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        int id = R.drawable.ic_clear;

        switch (status.toLowerCase()) {
            case "clear":
                break;
            case "snow":
                id = R.drawable.ic_snow;
                break;
            case "clouds":
                id = R.drawable.ic_clouds;
                break;
            case "rain":
                id = R.drawable.ic_rain;
                break;
            case "drizzle":
                id = R.drawable.ic_drizzle;
                break;
            case "thunderstorm":
                id = R.drawable.ic_thunder;
            default:
                id = R.drawable.ic_misc;

        }
        icon.setImageResource(id);
    }

    private String DetailedResponse(WeatherResponse response) {
        return
//                "Min: " +
//                        (int) Math.round(response.main.temp_min) + "°" +
//                        "\n" +
//                        "Max: " +
//                        (int) Math.round(response.main.temp_max) + "°" +
//                        "\n" +
                        "Humidity: " +
                        response.main.humidity +
                        "\n" +
                        "Pressure: " +
                        response.main.pressure +
                        "\n" +
                        "Wind speed: " + response.wind.speed + " meter/sec";
    }
}