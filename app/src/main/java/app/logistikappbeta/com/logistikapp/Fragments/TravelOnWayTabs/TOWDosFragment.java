package app.logistikappbeta.com.logistikapp.Fragments.TravelOnWayTabs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.TOWClientesAdapter;
import app.logistikappbeta.com.logistikapp.AddClientTravelActivity;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.TOWInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Travel;
import app.logistikappbeta.com.logistikapp.POJOs.Waybill;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TOWDosFragment extends Fragment
    implements OnConnectionFailedListener, ConnectionCallbacks,
    ResultCallback<LocationSettingsResult>, LocationListener {

  @BindView(R.id.rvList)
  RecyclerView rvList;
  @BindView(R.id.lyProgress)
  LinearLayout lyProgress;

  private static final int PETITION_CONFIG_UBICATION = 1;
  private ArrayList<Waybill> datos;
  private TOWInterface towInterface;
  private Location myLocation;
  private Login login;
  private Long idTravel;
  private GoogleApiClient gClient;
  private LocationRequest locRequest;
  private Travel travel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_towdos, container, false);
    ButterKnife.bind(this, v);

    gClient = new GoogleApiClient.Builder(getContext())
        .enableAutoManage(getActivity(), 0, this)
        .addConnectionCallbacks(this)
        .addApi(LocationServices.API)
        .build();

    travel = new Travel();
    datos = new ArrayList<>();

    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new TOWClientesAdapter(datos, travel, towInterface, this));

    login = Utils.getSessionInfo(getContext());
    idTravel = getArguments().getLong("idTravel");

    if (!login.getProfile().contains("DRI")) {
      lyProgress.setVisibility(View.GONE);
    }

    return v;
  }

  @Override
  public void onResume() {
    super.onResume();
    enabledLocUpd();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    towInterface = (TOWInterface) context;
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @OnClick(R.id.btnCliente)
  public void onClick(View v) {
    Intent intent = new Intent(getContext(), AddClientTravelActivity.class);
    intent.putExtra("idTravel", this.idTravel);
    startActivity(intent);
  }

  public void moveToStoreListPosition(int positionMarker) {
    //f2
    rvList.scrollToPosition(positionMarker);
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Toast.makeText(getContext(), "Error al conectar a los servicios de GPS", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
            Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(getContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      Toast.makeText(getContext(), "Permiso de localización denegado", Toast.LENGTH_SHORT).show();
      return;
    }
    myLocation = LocationServices.FusedLocationApi.getLastLocation(gClient);
  }

  @Override
  public void onConnectionSuspended(int i) {
    Toast.makeText(getContext(), "Se ha interrumpido la conexión a los servicios de GPS", Toast.LENGTH_SHORT).show();
  }

  public void updateStores(ArrayList<Waybill> stores, Travel _travel) {
    datos.clear();
    datos.addAll(stores);
    travel = _travel;
    rvList.getAdapter().notifyDataSetChanged();
  }

  public void enabledLocUpd() {
    locRequest = new LocationRequest();
    locRequest.setInterval(2000);
    locRequest.setFastestInterval(1000);
    locRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
        .addLocationRequest(locRequest)
        .build();

    PendingResult<LocationSettingsResult> results = LocationServices.SettingsApi.checkLocationSettings(gClient, locationSettingsRequest);
    results.setResultCallback(this);
  }

  @Override
  public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
    final Status status = locationSettingsResult.getStatus();
    switch (status.getStatusCode()) {
      case LocationSettingsStatusCodes.SUCCESS: {
        startLocationUpd();
      }
      break;
      case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: {
        try {
          status.startResolutionForResult(getActivity(), PETITION_CONFIG_UBICATION);
        } catch (IntentSender.SendIntentException e) {
          e.printStackTrace();
          Toast.makeText(getContext(), "Error en la configuración de ubicación", Toast.LENGTH_SHORT).show();
        }
      }
      break;
      case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {
        Toast.makeText(getContext(), "No se pudo realizar la configuración de la ubicación", Toast.LENGTH_SHORT).show();
      }
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    myLocation = location;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case PETITION_CONFIG_UBICATION: {
        switch (resultCode) {
          case Activity.RESULT_OK: {
            startLocationUpd();
          }
          break;
          case Activity.RESULT_CANCELED: {
            Toast.makeText(getContext(), "No se realizaron los cambios necesarios", Toast.LENGTH_SHORT).show();
          }
          break;
        }
      }
      break;
    }
  }

  public void startLocationUpd() {
    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(getContext(),
        Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
      Toast.makeText(getContext(), "Permiso de localización denegado", Toast.LENGTH_SHORT).show();
      return;
    }
    LocationServices.FusedLocationApi.requestLocationUpdates(gClient, locRequest, this);
    lyProgress.setVisibility(View.GONE);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    gClient.stopAutoManage(getActivity());
    gClient.disconnect();
  }
}
