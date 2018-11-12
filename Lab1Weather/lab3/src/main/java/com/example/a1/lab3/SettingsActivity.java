package com.example.a1.lab3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends Activity {
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        setContentView(R.layout.activity_settings);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void oneMin_clicked(View view) {
        startService(new Intent(this, WeatherService.class).putExtra("timer", 1).putExtra("city", city));
    }

    public void fiveMin_clicked(View view) {
        startService(new Intent(this, WeatherService.class).putExtra("timer", 5).putExtra("city", city));
    }

    public void thirtyMin_clicked(View view) {
        startService(new Intent(this, WeatherService.class).putExtra("timer", 30).putExtra("city", city));
    }

    public void drop_clicked(View view) {
        stopService(new Intent(this, WeatherService.class));
    }
}
