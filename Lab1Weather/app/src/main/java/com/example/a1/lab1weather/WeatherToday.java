package com.example.a1.lab1weather;

import com.orm.SugarApp;
import com.orm.SugarRecord;

public class WeatherToday extends SugarRecord {
    public String city;
    public String temperature;

    public WeatherToday() {

    }

    public WeatherToday (String city, String temperature) {
        this.city = city;
        this.temperature = temperature;
    }

    public String getCity() {
        return this.city;
    }

    public String getTemperature() {
        return this.temperature;
    }
}
