package com.example.a1.lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        final EditText city = findViewById(R.id.city);
        if(!city.getText().toString().isEmpty()) {
            intent.putExtra("city", city.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "City is not typed!", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
        return true;
    }

    public void onClickGo(View view) {
        final EditText city = findViewById(R.id.city);
        if(!city.getText().toString().isEmpty()) {
            sendReq(city.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "City is not typed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAllFromDB(View view) {
        final TextView showDB_tv = findViewById(R.id.showDB_tv);
        showDB_tv.setText("");
        List<WeatherToday> weathers = WeatherToday.listAll(WeatherToday.class);
        for (WeatherToday weather : weathers) {
            showDB_tv.append(weather.getCity() + ": " + weather.getTemperature() + "\n");
        }
    }

    public void deleteAllFromDB(View view) {
        final TextView showDB_tv = findViewById(R.id.showDB_tv);
        showDB_tv.setText("");
        List<WeatherToday> weathers = WeatherToday.listAll(WeatherToday.class);
        WeatherToday.deleteAll(WeatherToday.class);
        Toast.makeText(getApplicationContext(), "All records was deleted!", Toast.LENGTH_SHORT);
    }

    public void saveIntoDB(View view) {
        final TextView cityName = findViewById(R.id.cityName);
        final TextView degrees = findViewById(R.id.degrees);
        if (cityName.getText() != "" && degrees.getText() != "") {
            WeatherToday weatherToday = new WeatherToday(cityName.getText().toString(), degrees.getText().toString());
            weatherToday.save();
            Toast.makeText(getApplicationContext(), "Record was added!", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getApplicationContext(), "Record was not added!", Toast.LENGTH_SHORT);
        }
    }

    public void sendReq (String city) {
        final TextView cityName = findViewById(R.id.cityName);
        final TextView degrees = findViewById(R.id.degrees);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&APPID=a3af4d3264bc6e28866eb9e57e1bb87e";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                try {
                    JSONObject main = response.getJSONObject("main");
                    cityName.setText(response.getString("name"));
                    degrees.setText(main.getString("temp") + "°C");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });
        queue.add(jsObjRequest);
    }
}
