package app.logistikappbeta.com.logistikapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.logistikappbeta.com.logistikapp.Adapters.ActivesAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Fragments.ActivesBarcodeFragment;
import app.logistikappbeta.com.logistikapp.Interfaces.IBarcodeActives;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Active;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.Params.ActivesAndVisitsParam;
import app.logistikappbeta.com.logistikapp.Params.AddActiveVisitParam;
import app.logistikappbeta.com.logistikapp.Params.CheckinParam;
import app.logistikappbeta.com.logistikapp.Results.ActivesAndVisitsResult;
import app.logistikappbeta.com.logistikapp.Results.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivesActivity extends AppCompatActivity implements IBarcodeActives {

    @BindView(R.id.rvList)
    public RecyclerView mRvList;

    public ActivesBarcodeFragment mBarcodeFragment;
    private ArrayList<Active> data;
    private Long id_store;
    private Long id_waybill;
    private Login login;
    private BigInteger token;
    private String checkInOutRange;
    AddActiveVisitParam param;

    SimpleDateFormat sdf;

    private GoogleApiClient mGApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actives);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.mBarcodeFragment = new ActivesBarcodeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, mBarcodeFragment)
                .commit();

        this.data = new ArrayList<>();

        this.mRvList.setHasFixedSize(true);
        this.mRvList.setItemAnimator(new DefaultItemAnimator());
        this.mRvList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvList.setAdapter(new ActivesAdapter(getApplicationContext(), this.data));

        this.login = Utils.getSessionInfo(this);
        this.token = new BigInteger(Utils.getSaveToken(this));

        param = new AddActiveVisitParam();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        checkInOutRange = intent.getExtras().getString("outrange");

        if (bundle != null) {
            if (bundle.containsKey("ids")) {
                this.id_store = intent.getLongExtra("ids", 0);
                param.setIds(id_store);
            }

            if (bundle.containsKey("idw")) {
                this.id_waybill = bundle.getLong("idw", 0);
                param.setIdw(id_waybill);
            }
        }

        this.sdf = new SimpleDateFormat("dd/MM/yyyy");
        param.setUid(login.getId());
        param.setToken(token);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getActives();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actives, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
            break;

            case R.id.item_flash: {
                if (mBarcodeFragment != null) {
                    mBarcodeFragment.flash();
                }
            }
        }

        return true;
    }

    @OnClick({R.id.btnAddActive})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.btnAddActive: {
                Intent intent = new Intent(this, AddActiveActivity.class);
                intent.putExtra("ids", id_store);
                startActivity(intent);
            }
            break;
        }
    }

    @Override
    public void sendBarcode(final String barcode) {
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorGreen));
        dialog.setTitleText("Espere por favor")
                .setContentText("Buscando activo")
                .show();
        Active active = null;

        for (Active i : this.data) {
            if (i.getCode().contentEquals(barcode)) {
                active = i;
                break;
            }
        }


        if (active != null) {
            param.setId_actives(active.getIdActives().longValue());

            if (active.getVisited().contentEquals("S")) {
                dialog.changeAlertType(SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("Activo visitado")
                        .setContentText("El activo ya fue visitado el día de hoy")
                        .show();
            } else {

                final String code = active.getCode();

                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitleText("Activo encontrado")
                        .setContentText("¿El activo presenta daños?")
                        .setConfirmText("Si")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(ActivesActivity.this, ActiveCommentActivity.class);
                                intent.putExtra("code", code);
                                startActivityForResult(intent, 1);
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                saveVisit();
                            }
                        })
                        .show();

            }
        } else {
            dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("No encontrado")
                    .setContentText("El código " + barcode + " no fue encontrado\n¿Desea agregarlo?")
                    .setConfirmText("Sí, agregarlo")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent intent = new Intent(ActivesActivity.this, AddActiveActivity.class);
                            intent.putExtra("barcode", barcode);
                            intent.putExtra("ids", id_store);
                            startActivity(intent);
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setCancelText("No, continuar")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
        }

    }

    public void getActives() {
        ActivesAndVisitsParam param = new ActivesAndVisitsParam();
        param.setDate(sdf.format(new Date()));
        param.setIds(this.id_store);
        param.setToken(this.token);
        param.setUid(this.login.getId());

        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorGreen));
        dialog.setTitleText("Espere por favor")
                .setContentText("Obteniendo información")
                .setCancelable(false);
        dialog.show();


        Call<ActivesAndVisitsResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getActivesAndVisits(param);
        call.enqueue(new Callback<ActivesAndVisitsResult>() {
            @Override
            public void onResponse(Call<ActivesAndVisitsResult> call, Response<ActivesAndVisitsResult> response) {

                dialog.dismiss();

                ActivesAndVisitsResult result = response.body();

                if (result == null) return;

                if (result.getStatus() > 0) {
                    Toast.makeText(ActivesActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
                } else {
                    data.clear();
                    data.addAll(result.getActives());
                    mRvList.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ActivesAndVisitsResult> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
                Toast.makeText(ActivesActivity.this, "No se obtuvo respuesta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();

            param.setComment(bundle.getString("comment", null));
            param.setImage1(bundle.getString("image1", null));
            param.setImage2(bundle.getString("image2", null));

            Log.d("LGK", "RESULT_OK");
            saveVisit();

        } else {
            Log.d("LGK", "RESULT_CANCEL");
            saveVisit();
        }

    }


    private void sendOK() {
        setResult(RESULT_OK, new Intent());
        ActivesActivity.this.finish();
    }


    private Location mLocation = null;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveVisit() {

        GoogleApiClient gApiClient = App.getGoogleApiHelper().getGoogleApiClient();
        gApiClient.connect();

        if (this.mGApi == null) {
            this.mGApi = App.getGoogleApiHelper().getGoogleApiClient();
            if (this.mGApi == null) return;
        }

        if (!App.getGoogleApiHelper().isConnected()) {
            Toast.makeText(this, "Necesita el GPS para registrar el check del activo", Toast.LENGTH_SHORT).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocation = LocationServices.FusedLocationApi.getLastLocation(mGApi);
                    if (mLocation == null) {
                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            } else {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGApi);
                if (mLocation == null) {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

            if (mLocation == null) {
                Toast.makeText(this, "Ubicación no obtenida", Toast.LENGTH_SHORT).show();
            } else {
                final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                dialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorGreen));
                dialog.setTitleText("Espere por favor")
                        .setContentText("Registrando check de activo")
                        .setCancelable(false);
                dialog.show();

                param.setIds(id_store);
                param.setUid(login.getId());
                param.setToken(token);
                param.setLatitude(mLocation.getLatitude());
                param.setLongitude(mLocation.getLongitude());

                Call<Result> call = Utils.buildRetrofit(this).create(WSInterface.class).addActiveVisit(param);
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        dialog.dismiss();
                        Result result = response.body();

                        if (result == null) return;

                        if (result.getStatus() > 0) {
                            Toast.makeText(ActivesActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            dialog.setTitleText("Exito")
                                    .setContentText("Check de activo registrado");
                            getActives();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(ActivesActivity.this, "No se obtuvo respuesta", Toast.LENGTH_SHORT).show();
                    }
                });

                if(checkInOutRange.contentEquals("S")){
                    final SweetAlertDialog progress = new SweetAlertDialog(ActivesActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    progress.getProgressHelper().setBarColor(ContextCompat.getColor(ActivesActivity.this, R.color.colorGreen));
                    progress.setTitleText("Procesando transacción")
                            .setCancelable(false);
                    progress.show();

                    Intent intent = getIntent();
                    Bundle bundle = getIntent().getExtras();
                    String idw = intent.getExtras().getString("idw");
                    CheckinParam params = new CheckinParam();
                    params.setUid(login.getId());
                    params.setToken(String.valueOf(token));
                    params.setWid(Integer.parseInt(idw));
                    SimpleDateFormat ese = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    params.setDate(ese.format(new Date()));
                    params.setLatitude(bundle.getDouble("lat"));
                    params.setOutrange(checkInOutRange);
                    params.setLongitude(bundle.getDouble("lng"));

                    Call<Result> call1 = Utils.buildRetrofit(getApplicationContext()).create(WSInterface.class).checkin(params);
                    call1.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call1, Response<Result> response) {
                            progress.dismiss();
                            Result result = response.body();
                            if (result.getStatus() == 0) {
                                progress.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                progress.setTitleText("Transacción exitosa");
                                progress.setConfirmClickListener(sDialog -> sendOK());
                                progress.show();
                            } else {
                                Toast.makeText(ActivesActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call1, Throwable t) {
                            t.printStackTrace();
                            progress.dismiss();
                        }
                    });
                }
            }
        }
    }
}