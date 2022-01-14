package app.logistikappbeta.com.logistikapp.Fragments.TravelOnWayTabs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import app.logistikappbeta.com.logistikapp.App;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.IUsuariosFragment;
import app.logistikappbeta.com.logistikapp.Interfaces.TOWInterface;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.UserPosition;
import app.logistikappbeta.com.logistikapp.POJOs.Users;
import app.logistikappbeta.com.logistikapp.POJOs.Waybill;
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

public class TOWTresFragment extends Fragment implements LocationListener, View.OnClickListener,
        GoogleMap.OnMarkerClickListener, CompoundButton.OnCheckedChangeListener, OnMapReadyCallback
{

    @BindView(R.id.chMyUbicacion)
    CheckBox chMyUbicacion;
    @BindView(R.id.fMap)
    MapView mapView;
    @BindView(R.id.btnLocate)
    LinearLayout btnLocate;

    GoogleMap gMap;
    LocationManager locationManager;

    ArrayList<Marker> markers;
    private GoogleApiClient mGApi;
    private Location mLocation;
    Marker myMarker;
    Circle mCircleBlue;
    Circle mCircleOrange;

    Login login;
    String token;

    TOWInterface towInterface;

    String stores;

    BottomSheetBehavior bsb;

    private IUsuariosFragment iUsuariosFragment;

    Long id_user;
    String driver_username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_towtres, container, false);
        ButterKnife.bind(this, v);

        boolean isCheck = Utils.getMyUbicationPreferences(getContext(),
                Utils.TOW_UBICATION_PREFERENCES);

        chMyUbicacion.setChecked(isCheck);

        chMyUbicacion.setOnCheckedChangeListener(this);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        MapsInitializer.initialize(this.getContext());
        mapView.getMapAsync(this);

        locationManager = (LocationManager) getActivity().getSystemService(
                Context.LOCATION_SERVICE);

        login = Utils.getSessionInfo(getContext());
        token = Utils.getToken();

        iUsuariosFragment = null;

        if(login.getProfile().contains("DRI"))
            btnLocate.setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillList();
        if (gMap != null) {
            turnGPS();
            if (App.getGoogleApiHelper().isConnected()) {
                mGApi = App.getGoogleApiHelper().getGoogleApiClient();
            }
        }
    }


    public void fillList() {
        GetUsersParam param = new GetUsersParam();
        param.setToken(new BigInteger(token));
        param.setUid(login.getId().intValue());

        Call<GetUsersResult> call = Utils.buildRetrofit(getContext())
                .create(WSInterface.class)
                .getUsers(param);


        call.enqueue(new Callback<GetUsersResult>() {
            @Override
            public void onResponse(Call<GetUsersResult> call, Response<GetUsersResult> response) {
                GetUsersResult result = response.body();
                if (result.getStatus() == 0) {
                    int size = result.getUsers().size();

                    if (size == 0) {
                        Toast.makeText(getContext(), "No es posible identificar al chófer en " +
                                "este momento. " + result.getMessage() + "\nCodigo: " +
                                result.getStatus(), Toast.LENGTH_LONG).show();
                    } else {
                        for (Users user:result.getUsers()) {
                            String aux = user.getUsername();
                            String aux_d = driver_username;
                            if(user.getUsername().equals(driver_username)){
                                id_user = user.getId_user();
                                return;
                            }
                        }
                        //Toast.makeText(getContext(), "SI: " + result.getMessage() + "\nCodigo: " +
                        // result.getStatus(), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCodigo: "
                            + result.getStatus(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetUsersResult> call, Throwable t) {
                Toast.makeText(getContext(), "Verifique su acceso a internet",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(this);
            } else {
                Toast.makeText(getContext(), "Permiso de ubicación denegado",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(this);
            } else {
                Toast.makeText(getContext(), "Permiso de ubicación denegado",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        towInterface = (TOWInterface) context;
    }

    void getLocation() {
        if (mGApi == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGApi);
            }
        } else {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGApi);
        }
    }

    public void changeMarkerState(int position, int status) {
        Marker marker = markers.get(position);
        switch (status) {
            case 1: {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED));
            }
            break;
            case 2: {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN));
            }
            break;
            case 3: {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_ORANGE));
            }
            break;
        }
    }

    public boolean isGPSConnect() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = getProviderEnabled() != null ? getProviderEnabled() :
                locationManager.getBestProvider(criteria, true);

        return provider != null && locationManager.isProviderEnabled(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mLocation = location;
        if (mLocation == null) {
            this.getLocation();
        }
        if (this.mLocation != null) {
            configUbication();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {

        if (chMyUbicacion.isChecked()) {

            if (this.mCircleBlue != null) {
                this.mCircleBlue.setVisible(true);
            }

            if (this.mCircleOrange != null) {
                this.mCircleOrange.setVisible(true);
            }

            if (this.myMarker != null) {
                this.myMarker.setVisible(true);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if ((ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                    gMap.setMyLocationEnabled(true);

                }

            } else {
                gMap.setMyLocationEnabled(true);
            }

        }

    }

    @Override
    public void onProviderDisabled(String s) {

        if (chMyUbicacion.isChecked()) {

            if (this.mCircleBlue != null) {
                this.mCircleBlue.setVisible(false);
            }

            if (this.mCircleOrange != null) {
                this.mCircleOrange.setVisible(false);
            }

            if (myMarker != null) {
                myMarker.setVisible(false);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if ((ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                    gMap.setMyLocationEnabled(false);

                }

            } else {
                gMap.setMyLocationEnabled(false);
            }

        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        int position = markers.indexOf(marker);

        if (position >= 0) {
            towInterface.moveToStoreListPosition(position);
        }

        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Utils.setMyUbicationPreferences(getContext(), Utils.TOW_UBICATION_PREFERENCES, b);
        configUbication();
    }

    void configUbication() {
        if (chMyUbicacion.isChecked()) {

            if (isGPSConnect()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) &&
                            ActivityCompat.checkSelfPermission(getContext(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED) {

                        gMap.setMyLocationEnabled(true);
                        if (myMarker != null) {
                            if (mLocation != null) {
                                myMarker.setPosition(new LatLng(mLocation.getLatitude(),
                                        mLocation.getLongitude()));
                                myMarker.setVisible(true);
                                if (this.mCircleBlue != null) {
                                    this.mCircleBlue.setVisible(true);
                                    this.mCircleBlue.setCenter(myMarker.getPosition());
                                }

                                if (this.mCircleOrange != null) {
                                    this.mCircleOrange.setVisible(true);
                                    this.mCircleOrange.setCenter(myMarker.getPosition());
                                }
                            }
                        } else {
                            if (mLocation != null) {
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.title("Mi posición")
                                        .position(new LatLng(mLocation.getLatitude(),
                                                mLocation.getLongitude()))
                                        .icon(BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_VIOLET));
                                myMarker = gMap.addMarker(markerOptions);
                                myMarker.showInfoWindow();

                                CircleOptions circleOptions = new CircleOptions();
                                circleOptions.center(myMarker.getPosition())
                                        .fillColor(Color.parseColor("#e67e22"))
                                        .strokeColor(ContextCompat.getColor(getContext(),
                                                android.R.color.white))
                                        .strokeWidth(4)
                                        .radius(150);
                                this.mCircleOrange = gMap.addCircle(circleOptions);

                                circleOptions = new CircleOptions();
                                circleOptions.center(myMarker.getPosition())
                                        .fillColor(Color.parseColor("#3498db"))
                                        .strokeColor(ContextCompat.getColor(getContext(),
                                                android.R.color.white))
                                        .strokeWidth(4)
                                        .radius(50);
                                this.mCircleBlue = gMap.addCircle(circleOptions);

                            }
                        }

                    } else {
                        Toast.makeText(getContext(), "Permiso de ubicación denegado",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    gMap.setMyLocationEnabled(true);
                    if (myMarker != null) {
                        if (mLocation != null) {
                            myMarker.setPosition(new LatLng(mLocation.getLatitude(),
                                    mLocation.getLongitude()));
                            myMarker.setVisible(true);
                            if (this.mCircleBlue != null) {
                                this.mCircleBlue.setVisible(true);
                                this.mCircleBlue.setCenter(myMarker.getPosition());
                            }
                            if (this.mCircleOrange != null) {
                                this.mCircleOrange.setVisible(true);
                                this.mCircleOrange.setCenter(myMarker.getPosition());
                            }
                        }
                    } else {
                        if (mLocation != null) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.title("Mi posición")
                                    .position(new LatLng(mLocation.getLatitude(),
                                            mLocation.getLongitude()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_VIOLET));
                            myMarker = gMap.addMarker(markerOptions);
                            myMarker.showInfoWindow();

                            CircleOptions circleOptions = new CircleOptions();
                            circleOptions.center(myMarker.getPosition())
                                    .fillColor(Color.parseColor("#e67e22"))
                                    .strokeColor(ContextCompat.getColor(getContext(),
                                            android.R.color.white))
                                    .strokeWidth(4)
                                    .radius(150);
                            this.mCircleOrange = gMap.addCircle(circleOptions);

                            circleOptions = new CircleOptions();
                            circleOptions.center(myMarker.getPosition())
                                    .fillColor(Color.parseColor("#3498db"))
                                    .strokeColor(ContextCompat.getColor(getContext(),
                                            android.R.color.white))
                                    .strokeWidth(4)
                                    .radius(50);
                            this.mCircleBlue = gMap.addCircle(circleOptions);
                        }
                    }
                }

            } else {
                Toast.makeText(getContext(), "GPS desactivado", Toast.LENGTH_SHORT).show();
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if ((ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
                    gMap.setMyLocationEnabled(false);
                    if (myMarker != null) {
                        myMarker.setVisible(false);
                    }
                    if (this.mCircleBlue != null) {
                        this.mCircleBlue.setVisible(false);
                    }
                    if (this.mCircleOrange != null) {
                        this.mCircleOrange.setVisible(false);
                    }
                } else {
                    Toast.makeText(getContext(), "Permiso de ubicación denegado",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                gMap.setMyLocationEnabled(false);
                if (myMarker != null) {
                    myMarker.setVisible(false);
                }
                if (this.mCircleBlue != null) {
                    this.mCircleBlue.setVisible(false);
                }
                if (this.mCircleOrange != null) {
                    this.mCircleOrange.setVisible(false);
                }
                locationManager.removeUpdates(this);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
        turnGPS();
        configUbication();

        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.setOnMarkerClickListener(this);

        if (stores != null) {
            updateStores(driver_username, this.stores);
        }
    }

    public void updateStores(String driver_username, String stores) {
        this.stores = stores;
        this.driver_username = driver_username;
        if (gMap == null) return;
        new AsyncTaskAddMarkers().execute(stores);
    }

    public String getProviderEnabled() {
        List<String> providers = locationManager.getProviders(true);
        Location locationTemp = null;
        String provider = null;
        for (String i : providers) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                    locationTemp = locationManager.getLastKnownLocation(i);
                } else {
                    return null;
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

    @OnClick({R.id.btnLocate})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLocate: {
                showUbication(id_user);
                break;
            }
        }
    }


    public void showUbication(Long id_user) {
        final SweetAlertDialog progress = new SweetAlertDialog(getContext(),
                SweetAlertDialog.PROGRESS_TYPE);
        progress.getProgressHelper().setBarColor(ContextCompat.getColor(getContext(),
                R.color.colorGreen));
        progress.setTitleText("Espere")
                .setContentText("Obteniendo posición...")
                .setCancelable(false);
        progress.show();

        GetUserPositionParam param = new GetUserPositionParam();
        param.setToken(new BigInteger(token));
        param.setUid(login.getId());
        param.setUpid(id_user);

        Call<UserPositionResult> call = Utils.buildRetrofit(getContext()).create(WSInterface.class).
                getUserPosition(param);
        call.enqueue(new Callback<UserPositionResult>() {
            @Override
            public void onResponse(Call<UserPositionResult> call, Response<UserPositionResult>
                    response) {
                UserPositionResult result = response.body();
                if (result == null) {
                    progress.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                    progress.setContentText("Ubicación no disponible en este momento");
                } else if (result.getStatus() == 0) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            UserPosition userPosition = result.getUser_position();
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(userPosition.getLatitude(),
                                    userPosition.getLongitude()));
                            myMarker = gMap.addMarker(new MarkerOptions().title("Posición actual " +
                                    "de " + driver_username)
                                    .position(new LatLng(userPosition.getLatitude(),
                                            userPosition.getLongitude()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE)));
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_AZURE));
                            myMarker = gMap.addMarker(markerOptions);
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    myMarker.getPosition(), 18));
                            myMarker.setVisible(true);
                            progress.dismiss();
                        }
                    }, 1000);
                } else {
                    progress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    progress.setContentText("Usuario no localizado, intentaremos de nuevo...");
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


    protected class AsyncTaskAddMarkers extends AsyncTask<String, Integer, ArrayList<Waybill>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Waybill> doInBackground(String... strings) {

            Gson gson = new Gson();
            ArrayList<Waybill> list = gson.fromJson(strings[0], new TypeToken<ArrayList<Waybill>>()
            {}.getType());

            return list;

        }

        private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight());
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }

        private Bitmap createStoreMarker(String marker_text, int color) {
            View markerLayout = getLayoutInflater().inflate(R.layout.view_custom_marker, null);

            ImageView markerImage = (ImageView) markerLayout.findViewById(R.id.marker_image);
            TextView markerOrder = (TextView) markerLayout.findViewById(R.id.marker_text);
            markerImage.setImageResource(R.drawable.ic_place);
            markerImage.setColorFilter(color);
            markerOrder.setText(marker_text);

            markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED));
            markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.
                    getMeasuredHeight());

            final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(),
                    markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            markerLayout.draw(canvas);
            return bitmap;
        }

        @Override
        protected void onPostExecute(ArrayList<Waybill> s) {
            super.onPostExecute(s);

            if (s != null) {

                Marker marker = null;
                MarkerOptions markerOptions = null;

                markers = new ArrayList<>();

                for (Waybill i : s) {

                    markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(i.getLatitude(), i.getLongitude());
                    markerOptions.position(latLng)
                            .title("(" + i.getOrderby() + ") " + i.getName())
                            .snippet(i.getAddress1() + ", " + i.getAddress2());

                    if (i.getOutrange() != null && i.getOutrange().equals("S")) {
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                        // BitmapDescriptorFactory.HUE_ORANGE));
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createStoreMarker(
                                i.getOrderby() + "", Color.parseColor("#ff9900"))));
                    } else if (i.getStatus().equals("S")) {
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                        // BitmapDescriptorFactory.HUE_GREEN));
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createStoreMarker(
                                i.getOrderby() + "", Color.parseColor("#009e0f"))));
                    } else {
                        //markerOptions.icon(bitmapDescriptorFromVector(getActivity(),
                        // R.drawable.marker));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                        // BitmapDescriptorFactory.HUE_RED));
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createStoreMarker(
                                i.getOrderby() + "", Color.parseColor("#cf2a27"))));
                    }
                    marker = gMap.addMarker(markerOptions);
                    markers.add(marker);
                }
                LatLng latLng = markers.get(0).getPosition();
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        }
    }

    void turnGPS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {

                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);

                String provider = getProviderEnabled() != null ? getProviderEnabled() :
                        locationManager.getBestProvider(criteria, true);

                if (provider != null) {

                    locationManager.requestLocationUpdates(provider, 0, 0,
                            this);

                    mLocation = locationManager.getLastKnownLocation(provider);

                    if (mLocation == null) {
                        this.getLocation();
                    }

                    if (mLocation != null) {
                        this.onLocationChanged(mLocation);
                    }

                } else {
                    Toast.makeText(getContext(), "No se encontraron proveedores de GPS",
                            Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(getContext(), "Permiso de ubicación denegado",
                        Toast.LENGTH_SHORT).show();
            }

        } else {

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);

            String provider = getProviderEnabled() != null ? getProviderEnabled() :
                    locationManager.getBestProvider(criteria, true);

            if (provider != null) {

                locationManager.requestLocationUpdates(provider, 0, 0,
                        this);

                mLocation = locationManager.getLastKnownLocation(provider);

                if (mLocation == null) {
                    this.getLocation();
                }

                if (mLocation != null) {
                    this.onLocationChanged(mLocation);
                }

            } else {
                Toast.makeText(getContext(), "No se encontraron proveedores de GPS",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
