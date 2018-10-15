package com.example.a1.lab1weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
        Button btn = findViewById(R.id.button);
        Button btnSave = findViewById(R.id.buttonSave);
        Button btnShow = findViewById(R.id.buttonShowAll);
        Button btnDelete = findViewById(R.id.buttonDelete);
        final TextView allStrings = findViewById(R.id.allStrings);
        final TextView txt = findViewById(R.id.cityName);
        final EditText editTxt = findViewById(R.id.editText);
        final TextView cityName = findViewById(R.id.cityName);
        final TextView degrees = findViewById(R.id.degrees);
        allStrings.setMovementMethod(new ScrollingMovementMethod());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReq(editTxt.getText().toString());
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allStrings.setText("");
                List<WeatherToday> weathers = WeatherToday.listAll(WeatherToday.class);
                for (WeatherToday weather : weathers) {
                    allStrings.append(weather.getCity() + ": " + weather.getTemperature() + "\n");
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityName.getText() != "" && degrees.getText() != "") {
                    WeatherToday weatherToday = new WeatherToday(cityName.getText().toString(), degrees.getText().toString());
                    weatherToday.save();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<WeatherToday> weathers = WeatherToday.listAll(WeatherToday.class);
                WeatherToday.deleteAll(WeatherToday.class);
            }
        });
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
