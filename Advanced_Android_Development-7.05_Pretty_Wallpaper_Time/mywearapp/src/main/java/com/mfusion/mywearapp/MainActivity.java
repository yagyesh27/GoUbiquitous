package com.mfusion.mywearapp;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.*;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener{


    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

        Log.d("DataChange", "11111");

        for(DataEvent dataEvent : dataEventBuffer ){

            if(dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String path = dataEvent.getDataItem().getUri().getPath();
                if (path.equals("/wearable_data")) {

                    int weatherId = dataMap.getInt("weatherId");
                    int highTemp = dataMap.getInt("highTemp");
                    int lowTemp = dataMap.getInt("lowTemp");


                    Log.d("Listener service:", weatherId + " " + highTemp + " " + lowTemp);


                }
            }

        }
    }

    private TextView mTextViewTime, mTextViewDate;
    Calendar c ;
    SimpleDateFormat dfTime, dfDate;
    String formattedTime;
    String formattedDate;
    WatchViewStub stub;
    GoogleApiClient mGoogleApiClient;
    String TAG = "Tag Wearable";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        dfTime = new SimpleDateFormat("HH:mm");
        dfDate = new SimpleDateFormat("E, yyyy-MM-dd");
        formattedTime = dfTime.format(c.getTime());
        formattedDate = dfDate.format(c.getTime());


        setContentView(R.layout.activity_main);
        stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextViewTime = (TextView) stub.findViewById(R.id.timeText);
                mTextViewTime.setText(formattedTime);
                mTextViewDate = (TextView) stub.findViewById(R.id.dateText);
                mTextViewDate.setText(formattedDate);
            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();


    }


    @Override
    protected void onStart() {
        super.onStart();

            mGoogleApiClient.connect();

    }





    public static int getIconResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Connected to Google Api Service");
        }
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }




}
