package app.logistikappbeta.com.logistikapp.Class;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

/**
 * Created by danielmarin on 18/07/17.
 */

public class GoogleApiHelper implements ConnectionCallbacks, OnConnectionFailedListener {

  private static final String TAG = GoogleApiHelper.class.getSimpleName();
  private Context mContext;
  private GoogleApiClient mGApi;
  private Location location;

  public GoogleApiHelper(Context mContext) {
    this.mContext = mContext;
    buildGoogleApiClient();
    this.connect();
  }

  public GoogleApiClient getGoogleApiClient() {
    return this.mGApi;
  }

  public void connect() {
    if (mGApi != null) {
      mGApi.connect();
    }
  }

  public void disconnect() {
    if (mGApi != null && mGApi.isConnected()) {
      mGApi.disconnect();
    }
  }

  public boolean isConnected() {
    if (mGApi != null) {
      return mGApi.isConnected();
    }
    return false;
  }

  public void buildGoogleApiClient() {
    this.mGApi = new GoogleApiClient.Builder(this.mContext)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
          PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.mContext,
          Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
              ActivityCompat.checkSelfPermission(this.mContext,
                      Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
        Log.e(TAG, "Permiso de localización denegado");
        Toast.makeText(GoogleApiHelper.this.mContext, "Permiso de localización denegado", Toast.LENGTH_SHORT).show();
        return;
      }
    }

    location = LocationServices.FusedLocationApi.getLastLocation(this.mGApi);
  }

  @Override
  public void onConnectionSuspended(int i) {
    this.mGApi.connect();
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    Toast.makeText(mContext, "Error al conectar a los servicios de GPS", Toast.LENGTH_SHORT).show();
  }

}
