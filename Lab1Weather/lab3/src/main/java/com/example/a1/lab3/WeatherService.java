package com.example.a1.lab3;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

import java.util.concurrent.TimeUnit;

public class WeatherService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Service has stopped", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private NotificationManager nm;
    private final int NOTIFICATION_ID = 127;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int timer = intent.getIntExtra("timer", 0);
        final String city = intent.getStringExtra("city");

        System.out.println(city);
        if(!city.isEmpty()) {
            Toast.makeText(getApplicationContext(), "" + timer + city, Toast.LENGTH_SHORT).show();
            Thread weather = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            sendReq(city);
                            TimeUnit.MINUTES.sleep(timer);
                        }
                    } catch(InterruptedException ie){
                        ie.printStackTrace();
                    }
                }
            });
            weather.start();
        } else {
            Toast.makeText(getApplicationContext(), "Требуемые параметры не были заданы", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    public void sendReq (final String city) {
        nm = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&APPID=a3af4d3264bc6e28866eb9e57e1bb87e";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                try {
                    JSONObject main = response.getJSONObject("main");
                    String cityName = response.getString("name");
                    String degrees = main.getString("temp") + "°C";
                    WeatherToday weatherToday = new WeatherToday(cityName, degrees);
                    weatherToday.save();

                    Intent intent1 = new Intent(getApplicationContext(), SettingsActivity.class);
                    Notification.Builder builder = new Notification.Builder(getApplicationContext());
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
                    builder
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                            .setTicker("new notification")
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(true)
                            .setContentTitle("Weather right now")
                            .setContentText(cityName + ": " + degrees);

                    Notification notification = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        notification = builder.build();
                    }
                    nm.notify(NOTIFICATION_ID, notification);

                    Toast.makeText(getApplicationContext(), "New records was added!", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    System.out.println("I am in catch in sendReq");
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
