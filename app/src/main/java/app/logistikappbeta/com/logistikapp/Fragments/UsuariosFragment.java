package app.logistikappbeta.com.logistikapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.UsersAdapter;
import app.logistikappbeta.com.logistikapp.AdminUserActivity;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.IUsuariosFragment;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.UserPosition;
import app.logistikappbeta.com.logistikapp.POJOs.Users;
import app.logistikappbeta.com.logistikapp.Params.GetUserPositionParam;
import app.logistikappbeta.com.logistikapp.Params.GetUsersParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.GetUsersResult;
import app.logistikappbeta.com.logistikapp.Results.UserPositionResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnMapReadyCallback, IUsuariosFragment {

  private static final String TAG = "Logistikapp";

  @BindView(R.id.sRefresh)
  SwipeRefreshLayout sRefresh;
  @BindView(R.id.rvList)
  RecyclerView rvList;
  @BindView(R.id.cbNoData)
  CardView cvNoData;
  @BindView(R.id.btnAddUser)
  Button btnAddUser;
  @BindView(R.id.fMap)
  MapView mapView;
  GoogleMap gMap;

  ArrayList<Users> data;
  Login login;
  String token;

  BottomSheetBehavior bsb;
  private Marker mMarker;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_usuarios, container, false);
    ButterKnife.bind(this, v);

    login = Utils.getSessionInfo(getContext());
    token = Utils.getToken();

    btnAddUser.setVisibility(login.getSuperuser().contentEquals("S") ? View.VISIBLE : View.GONE);

    data = new ArrayList<>();

    sRefresh.setOnRefreshListener(this);
    sRefresh.setRefreshing(true);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new UsersAdapter(data, getContext(), this));

    mapView.onCreate(savedInstanceState);
    mapView.onResume();

    MapsInitializer.initialize(this.getContext());
    mapView.getMapAsync(this);

    bsb = BottomSheetBehavior.from((LinearLayout) v.findViewById(R.id.bottomSheet));

    return v;
  }

  @Override
  public void onRefresh() {
    fillList();
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    this.gMap = googleMap;
    this.gMap.getUiSettings().setZoomControlsEnabled(true);
    this.gMap.getUiSettings().setZoomGesturesEnabled(false);
  }

  @Override
  public void showUbication(Long id_user) {
    if (gMap == null) {
      SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
      dialog.setTitleText("Mapa no cargado")
          .setContentText("Tu dispositivo no puede cargar mapas")
          .setConfirmText("Aceptar");
      dialog.show();
    } else {
      final SweetAlertDialog progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
      progress.getProgressHelper().setBarColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
      progress.setTitleText("Espere")
          .setContentText("Obteniendo posición")
          .setCancelable(false);
      progress.show();

      GetUserPositionParam param = new GetUserPositionParam();
      param.setToken(new BigInteger(token));
      param.setUid(login.getId());
      param.setUpid(id_user);

      Call<UserPositionResult> call = Utils.buildRetrofit(getContext()).create(WSInterface.class).getUserPosition(param);
      call.enqueue(new Callback<UserPositionResult>() {
        @Override
        public void onResponse(Call<UserPositionResult> call, Response<UserPositionResult> response) {
          UserPositionResult result = response.body();
          if (result == null) {
            progress.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            progress.setContentText("Ubicación no disponible en este momento");
          } else if (result.getStatus() == 0) {

            UserPosition userPosition = result.getUser_position();

            if (mMarker == null) {
              mMarker = gMap.addMarker(new MarkerOptions().title("Posición actual")
                  .position(new LatLng(userPosition.getLatitude(), userPosition.getLongitude())));
              BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
              if (icon != null) {
                mMarker.setIcon(icon);
              }
            } else {
              mMarker.setPosition(new LatLng(userPosition.getLatitude(), userPosition.getLongitude()));
            }

            bsb.setState(BottomSheetBehavior.STATE_EXPANDED);

            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), 18));

            progress.dismiss();

          } else {
            progress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            progress.setContentText("Usuario no localizado");
          }
        }

        @Override
        public void onFailure(Call<UserPositionResult> call, Throwable t) {
          t.printStackTrace();
          progress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
          progress.setContentText("Usuario no localizado");
        }
      });
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    fillList();
  }

  public void fillList() {
    GetUsersParam param = new GetUsersParam();
    param.setToken(new BigInteger(token));
    param.setUid(login.getId().intValue());

    Call<GetUsersResult> call = Utils.buildRetrofit(getContext())
        .create(WSInterface.class)
        .getUsers(param);

    sRefresh.setRefreshing(true);
    cvNoData.setVisibility(View.GONE);

    call.enqueue(new Callback<GetUsersResult>() {
      @Override
      public void onResponse(Call<GetUsersResult> call, Response<GetUsersResult> response) {
        data.clear();
        GetUsersResult result = response.body();
        if (result.getStatus() == 0) {
          int size = result.getUsers().size();

          if (size == 0) {
            cvNoData.setVisibility(View.VISIBLE);
          } else {
            cvNoData.setVisibility(View.GONE);
          }

          data.addAll(result.getUsers());

        } else {
          Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
          cvNoData.setVisibility(View.VISIBLE);
        }
        sRefresh.setRefreshing(false);
        rvList.getAdapter().notifyDataSetChanged();
      }

      @Override
      public void onFailure(Call<GetUsersResult> call, Throwable t) {
        Toast.makeText(getContext(), "Verifique su acceso a internet", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error: onFailure", t);
        cvNoData.setVisibility(View.VISIBLE);
        sRefresh.setRefreshing(false);
      }
    });

  }

  @OnClick({R.id.btnCloseBs, R.id.btnAddUser})
  public void click(View v) {
    switch (v.getId()) {
      case R.id.btnCloseBs: {
        bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
        bsb.setPeekHeight(0);
      }
      break;
      case R.id.btnAddUser: {
        Intent intent = new Intent(getContext(), AdminUserActivity.class);
        intent.putExtra("new", true);
        startActivity(intent);
      }
      break;

    }
  }

}
