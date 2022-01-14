package app.logistikappbeta.com.logistikapp.Fragments.TravelWayBillTabs;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.logistikappbeta.com.logistikapp.POJOs.Waybill;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TWBTresFragment extends Fragment implements OnMapReadyCallback {

  @BindView(R.id.fMap)
  MapView mapView;
  GoogleMap gMap;

  ArrayList<Marker> markers;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_twbtres, container, false);
    ButterKnife.bind(this, v);

    mapView.onCreate(savedInstanceState);
    mapView.onResume();

    MapsInitializer.initialize(this.getContext());
    mapView.getMapAsync(this);

    return v;
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  public String getStreetName(LatLng coords) throws IOException {
    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
    List<Address> list = geocoder.getFromLocation(coords.latitude, coords.longitude, 1);
    if (!list.isEmpty()) {
      Address address = list.get(0);
      return address.getAddressLine(0);
    }
    return null;
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    this.gMap = googleMap;
    String stores = getArguments().getString("stores");

    Log.d("WAYBILLS", stores);

    Gson gson = new Gson();
    ArrayList<Waybill> list = gson.fromJson(stores, new TypeToken<ArrayList<Waybill>>() {
    }.getType());

    markers = new ArrayList<>();
    Marker marker = null;
    LatLng latLng = null;
    MarkerOptions markerOptions = null;

    for (Waybill i : list) {
      markerOptions = new MarkerOptions();
      latLng = new LatLng(i.getLatitude(), i.getLongitude());
      markerOptions.position(latLng)
          .title("(" + i.getOrderby() + ") " + i.getName());

      if (i.getOutrange() != null && i.getOutrange().equals("S")) {
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
      } else if (i.getStatus().equals("S")) {
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
      } else {
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
      }

      marker = googleMap.addMarker(markerOptions);

      markers.add(marker);

    }

    latLng = markers.get(0).getPosition();
    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

  }
}
