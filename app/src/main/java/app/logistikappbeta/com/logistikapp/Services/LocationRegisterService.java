package app.logistikappbeta.com.logistikapp.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.AddCurrentPosition;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.Results.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRegisterService extends Service {

  private Login login;
  private BigInteger token;
  private SimpleDateFormat sdf;
  private LocationManager locationManager;
  private Timer timer;
  private UserLocationTimerTask timerTask;
  private Gson gson;
  private double lat = 0.0;
  private double lng = 0.0;

  private GoogleApiClient mGApi;
  private Location mLocation;

  @Override
  public void onCreate() {
    super.onCreate();
    login = Utils.getSessionInfo(this);
    token = new BigInteger(Utils.getSaveToken(this));
    sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    gson = new Gson();
    Log.i("SERVICE_LOCATION", "onCreate");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.i("SERVICE_LOCATION", "onStartCommand");
    timer = new Timer();
    timerTask = new UserLocationTimerTask();
    timer.schedule(timerTask, 0, 1000 * 60);
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    throw null;
  }

  public void setLocation() {
    if (!Utils.isOnline(this) || login == null) {
      Log.i("SERVICE_LOCATION", "No internet o login");
      return;
    }
    if (token == null) {
      Log.i("SERVICE_LOCATION", "No token");
      return;
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
          PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
          Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        Log.i("SERVICE_LOCATION", "permiso no permitido");
        return;
      }
    }

    if (mLocation == null) {
      this.getLocation();
    }

    if (mLocation == null) {
      Log.i("SERVICE_LOCATION", "network_provider");
      mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    if (mLocation == null) {
      Log.i("SERVICE_LOCATION", "gps_provider");
      mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }


    if (mLocation != null && mLocation.getLatitude() != 0.0 && mLocation.getLongitude() != 0.0 &&
        mLocation.getLatitude() != lat && mLocation.getLongitude() != lng) {
      Log.i("SERVICE_LOCATION", "location obtenida");
      AddCurrentPosition param = new AddCurrentPosition();
      param.setCreated(sdf.format(new Date()));
      param.setToken(token);
      param.setLat(mLocation.getLatitude());
      param.setLng(mLocation.getLongitude());
      param.setUid(login.getId());
      Log.d("SERVICE_LOCATION", gson.toJson(param));
      lat = mLocation.getLatitude();
      lng = mLocation.getLongitude();


      Utils.buildRetrofit(this)
          .create(WSInterface.class)
          .addUserLocation(param)
          .enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
              Result result = response.body();
              if (result != null) {
                Log.d("SERVICE_LOCATION", "Message: " + result.getMessage() + "\nCode: " + result.getStatus());
              }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
              t.printStackTrace();
            }

          });
    }

  }

  @Override
  public void onDestroy() {
    Log.i("SERVICE_LOCATION", "onDestroy");
    timerTask.cancel();
    timer.cancel();
    super.onDestroy();
  }

  protected class UserLocationTimerTask extends TimerTask {

    @Override
    public void run() {
      Log.i("SERVICE_LOCATION", "run");
      new Handler(Looper.getMainLooper()).post(new Runnable() {
        @Override
        public void run() {
          setLocation();
        }
      });
    }
  }

  void getLocation() {
    if (mGApi == null) return;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
          PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
          Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGApi);
      }
    } else {
      mLocation = LocationServices.FusedLocationApi.getLastLocation(mGApi);
    }
  }

}
