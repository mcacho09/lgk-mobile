package app.logistikappbeta.com.logistikapp;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.Params.GetUserPositionParam;
import app.logistikappbeta.com.logistikapp.Results.UserPositionResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBackgroundLocationService extends Service {
    private static final String TAG = MyBackgroundLocationService.class.getSimpleName();
    private FusedLocationProviderClient mLocationClient;
    private LocationCallback mLocationCallback;
    private Long id_user;
    private BigInteger token;
    private int login;
    private float ubi_time;

    public MyBackgroundLocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mLocationCallback = new LocationCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.d(TAG, "onLocationResult: location error");
                    return;
                }

                List<Location> locations = locationResult.getLocations();
                LocationResultHelper helper = new LocationResultHelper(getApplicationContext(),
                        locations);
                //helper.showNotification();
                helper.saveLocationResults();

                token = new BigInteger(Utils.getSaveToken(getApplicationContext()));
                login = Math.toIntExact(Utils.getSessionInfo(getApplicationContext()).getId());

                //save positions --------------------------------------------------

                //Second ws
                GetUserPositionParam param = new GetUserPositionParam();
                param.setToken(new BigInteger(Utils.getSaveToken(getApplicationContext())));
                param.setUid(Utils.getSessionInfo(getApplicationContext()).getId());
                param.setUpid(Utils.getSessionInfo(getApplicationContext()).getId());

                Call<UserPositionResult> call = Utils.buildRetrofit(getApplicationContext()).
                        create(WSInterface.class).getUserPosition(param);
                    call.enqueue(new Callback<UserPositionResult>() {
                    @Override
                    public void onResponse(Call<UserPositionResult> call,
                                           Response<UserPositionResult> response) {
                        UserPositionResult result = response.body();
                        if (result == null) {
                            Log.e("Res: ", "Error");
                        } else if (result.getStatus() == 0) {
                            SimpleDateFormat formatter= new SimpleDateFormat(
                                    "yyyy-MM-dd ' a las' HH:mm:ss z");
                            Date date = new Date(System.currentTimeMillis());
                            Log.e("Res: ", "Ubicación " +
                                    "actualizada " + formatter.format(date));
                        } else {
                            Log.e("Res: ", "Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserPositionResult> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: called");
        startForeground(1001, getNotification());
        //String aux = intent.getStringExtra("ubi_time") + " ";
        //Log.e("hi, ", aux);
        getLocationUpdates();
        return START_STICKY;
    }

    private Notification getNotification() {
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),
                App.CHANNEL_ID)
                .setContentTitle("Location Notification")
                .setContentText("Location service is running in the background.")
                .setAutoCancel(true);

        return notificationBuilder.build();
    }

    public void setUbi_time(float _ubi_time){
        ubi_time = _ubi_time * 60000;
    }

    private void getLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        ubi_time = Float.parseFloat(Utils.getSessionInfo(getApplicationContext()).getUbi_time()) * 60000;
        Toast.makeText(getApplicationContext(), "Ubicación cada: " + (ubi_time/60000) + " min" , Toast.LENGTH_LONG).show();
        locationRequest.setInterval((long) (ubi_time * 1.5));
        locationRequest.setFastestInterval((long) ubi_time);

        locationRequest.setMaxWaitTime((long) (ubi_time * 3));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            stopSelf();
            Toast.makeText(getApplicationContext(), "Por favor acepte los permisos para " +
                    "poder continuar. ", Toast.LENGTH_SHORT).show();
            return;
        }
        mLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
        stopForeground(true);
        mLocationClient.removeLocationUpdates(mLocationCallback);
    }
}