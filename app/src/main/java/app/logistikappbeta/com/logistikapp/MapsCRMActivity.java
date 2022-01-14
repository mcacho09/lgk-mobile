package app.logistikappbeta.com.logistikapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsCRMActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {

  @BindView(R.id.tvNombre)
  TextView tvNombre;
  @BindView(R.id.tvCalle)
  TextView tvCalle;
  @BindView(R.id.lyPromo)
  LinearLayout lyPromo;
  @BindView(R.id.lyProgress)
  LinearLayout lyProgress;

  private GoogleMap mMap;
  private Marker mMarker;
  private LocationManager locationManager;

  private double cLat;
  private double cLon;
  private String name;
  private String street;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps_crm);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);

    setTitle("Ubicación del cliente");

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    Intent intent = getIntent();
    name = intent.getStringExtra("name");
    street = intent.getStringExtra("street");
    tvNombre.setText(name);
    tvCalle.setText(street);
    lyPromo.setVisibility(intent.getBooleanExtra("promo", false) ? View.VISIBLE : View.GONE);
    cLat = intent.getDoubleExtra("lat", 0);
    cLon = intent.getDoubleExtra("lon", 0);


    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    LatLng cposition = new LatLng(cLat, cLon);
    mMap.addMarker(new MarkerOptions().position(cposition).title(name + "\nCalle: " + street).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    mMap.getUiSettings().setZoomControlsEnabled(true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_LONG).show();
      } else {
        mMap.setMyLocationEnabled(true);
      }
    } else {
      mMap.setMyLocationEnabled(true);
    }
    turnOnConnect();
  }

  boolean mover = true;

  @Override
  public void onLocationChanged(Location location) {
    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

    if (myLocation != null) {
      lyProgress.setVisibility(View.GONE);
    }

    if (mMarker == null) {
      mMarker = mMap.addMarker(new MarkerOptions()
          .position(myLocation).title("Mi posición")
          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    PolylineOptions polylineOptions = new PolylineOptions();
    polylineOptions.add(new LatLng(cLat, cLon)).add(mMarker.getPosition()).geodesic(true)
        .geodesic(true);
    mMap.addPolyline(polylineOptions);

    mMarker.setPosition(myLocation);
    if (mover) {
      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 6));
      mover = false;
    }
  }

  @Override
  public void onStatusChanged(String s, int i, Bundle bundle) {
  }

  @Override
  public void onProviderEnabled(String s) {
  }

  @Override
  public void onProviderDisabled(String s) {
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (mMap != null) {
      turnOnConnect();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    turnOffConnect();
  }

  void turnOnConnect() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED &&
          ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
              == PackageManager.PERMISSION_GRANTED) {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String provider = getProviderEnabled() != null ? getProviderEnabled() : locationManager.getBestProvider(criteria, true);

        if (provider != null) {

          locationManager.requestLocationUpdates(provider, 0, 0, this);

          Location myLocation = locationManager.getLastKnownLocation(provider);

          if (myLocation != null) {
            this.onLocationChanged(myLocation);
          }

        } else {
          Toast.makeText(this, "No se encontraron proveedores de GPS", Toast.LENGTH_SHORT).show();
        }

      } else {
        Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
      }
    } else {

      Criteria criteria = new Criteria();
      criteria.setAccuracy(Criteria.ACCURACY_FINE);

      String provider = getProviderEnabled() != null ? getProviderEnabled() : locationManager.getBestProvider(criteria, true);

      if (provider != null) {

        locationManager.requestLocationUpdates(provider, 0, 0, this);

        Location myLocation = locationManager.getLastKnownLocation(provider);

        if (myLocation != null) {
          this.onLocationChanged(myLocation);
        }

      } else {
        Toast.makeText(this, "No se encontraron proveedores de GPS", Toast.LENGTH_SHORT).show();
      }

    }
  }

  void turnOffConnect() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED &&
          ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
              == PackageManager.PERMISSION_GRANTED) {
        locationManager.removeUpdates(this);
      } else {
        Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
      }
    } else {
      locationManager.removeUpdates(this);
    }
  }

  @Override
  public void onClick(View view) {
    finish();
  }

  public String getProviderEnabled() {
    List<String> providers = locationManager.getProviders(true);
    Location locationTemp = null;
    String provider = null;
    for (String i : providers) {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
          locationTemp = locationManager.getLastKnownLocation(i);
        } else {
          break;
        }
      } else {
        locationTemp = locationManager.getLastKnownLocation(i);
      }

      if (locationTemp == null) {
        continue;
      } else {
        provider = i;
        break;
      }

    }

    return provider;

  }


}
