package app.logistikappbeta.com.logistikapp;

import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import app.logistikappbeta.com.logistikapp.Adapters.OfflineSaleAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.Params.AddOrderParam;
import app.logistikappbeta.com.logistikapp.Params.CheckinParam;
import app.logistikappbeta.com.logistikapp.RealmObjects.SaleRO;
import app.logistikappbeta.com.logistikapp.Results.AddOrderResult;
import app.logistikappbeta.com.logistikapp.Results.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflineActivity extends AppCompatActivity implements Callback<AddOrderResult> {

    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.btnSync)
    Button btnSync;
    @BindView(R.id.layProgress)
    LinearLayout layProgress;

    Toolbar toolbar;

    RealmResults<SaleRO> list;
    Realm realm;
    Gson gson;
    SaleRO currentUpdate;
    int position = 0;
    NotificationManager notificationManager;
    OfflineSaleAdapter mAdapter;

    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            Log.d("Offline Activity", "On Change: CALLED");
            mAdapter.update(list);
            if (list.size() == 0) {
                changeToSuccessView();
            } else if (realm.where(SaleRO.class).isNull("id_order").findAll().size() == 0) {
                btnSync.setEnabled(false);
            } else {
                layProgress.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();
        RealmQuery<SaleRO> query = realm.where(SaleRO.class);
    /*realm.beginTransaction();
    query.findAll().deleteAllFromRealm();
    realm.commitTransaction();*/
        list = query.findAll();
        list.addChangeListener(realmChangeListener);

        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.setHasFixedSize(true);
        rvList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new OfflineSaleAdapter(this, realm, list);
        rvList.setAdapter(mAdapter);

        gson = new Gson();


        if (query.findAll().size() == 0) {
            changeToSuccessView();
        } else if (query.isNull("id_order").findAll().size() == 0) {
            btnSync.setEnabled(false);
        } else {
            layProgress.setVisibility(View.GONE);
        }
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }


    @Override
    protected void onStart() {
        super.onStart();
        notificationManager.cancel(3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.beginTransaction();

        realm.where(SaleRO.class)
                .isNotNull("id_order")
                .findAll()
                .deleteAllFromRealm();

        realm.commitTransaction();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.btnSync})
    public void click(View v) {
        this.startUpdate();
    }

  /*
  public void setOutrange(final SweetAlertDialog dialog){
    dialog.setTitleText("Alerta");
    dialog.setContentText("Checkin fuera del rango permitido\n¿Desea continuar?");
    dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
    dialog.show();
    dialog.setConfirmClickListener(sweetAlertDialog -> sweetAlertDialog.dismiss());
    dialog.setConfirmText("Sí")
      .setConfirmClickListener(sweetAlertDialog -> {
        sweetAlertDialog.dismiss();
        Bundle bundle = getIntent().getExtras();
        CheckinParam param = new CheckinParam();
        if (bundle != null) {
          param.setUid(login.getId());
          param.setToken(token.toString());
          param.setWid(bundle.getInt("wid"));
          param.setOutrange("S");
          param.setLatitude(bundle.getDouble("lat"));
          param.setLongitude(bundle.getDouble("lng"));
        }
        final SweetAlertDialog progress = new SweetAlertDialog(ProcessSaleActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        progress.getProgressHelper().setBarColor(ProcessSaleActivity.this.getResources().getColor(R.color.colorGreen));
        progress.setTitleText("Realizando checkin");
        progress.setContentText("Espere por favor");
        progress.setCancelable(false);
        progress.show();

        Call<Result> call = Utils.buildRetrofit(ProcessSaleActivity.this).create(WSInterface.class).checkin(param);
        call.enqueue(new Callback<Result>() {
          @Override
          public void onResponse(Call<Result> call, Response<Result> response) {
            Result result = response.body();
            if (result.getStatus() == 0) {
              //towInterface.updateInfoTravel();
              SweetAlertDialog alert = new SweetAlertDialog(ProcessSaleActivity.this, SweetAlertDialog.SUCCESS_TYPE);
              alert.setTitleText("Éxito")
                      .setContentText("Se realizó el checkin con éxito")
                      .show();
            } else {
              SweetAlertDialog alert = new SweetAlertDialog(ProcessSaleActivity.this, SweetAlertDialog.WARNING_TYPE);
              alert.setTitleText("Alerta")
                      .setContentText("Error: " + result.getMessage() + "\nCódigo: " + result.getStatus())
                      .show();
            }
            progress.dismiss();
          }

          @Override
          public void onFailure(Call<Result> call, Throwable t) {
            t.printStackTrace();
            progress.dismiss();
          }
        });
        saveSale(progress);
      });
  }*/

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startUpdate() {
        if (position < list.size()) {
            currentUpdate = list.get(position);
            layProgress.setVisibility(View.VISIBLE);

            updateSaleType(SaleRO.SaleType.SYNC);

            AddOrderParam param = gson.fromJson(currentUpdate.getParams(), AddOrderParam.class);

            Call<AddOrderResult> call = Utils.buildRetrofit(this)
                    .create(WSInterface.class)
                    .addOrder(param);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            CheckinParam checkinParam = new CheckinParam();
            if (param != null) {
                checkinParam.setUid(param.getUid());
                checkinParam.setToken(param.getToken().toString());
                checkinParam.setWid(Math.toIntExact(param.getWid()));
                checkinParam.setOutrange("S");
                checkinParam.setLatitude(param.getLatitude());
                checkinParam.setLongitude(param.getLongitude());
               // checkinParam.setDate(sdf.format(new Date()));
            }

            Call<Result> callTwo = Utils.buildRetrofit(this).create(WSInterface.class).checkin(checkinParam);
            callTwo.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Result result = response.body();
                    if (result != null) {
                        //towInterface.updateInfoTravel();
                        SweetAlertDialog alert = new SweetAlertDialog(OfflineActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        alert.setTitleText("Éxito")
                                .setContentText("Se sincronizaron las ventas.")
                                .show();
                    } else {
                        SweetAlertDialog alert = new SweetAlertDialog(OfflineActivity.this, SweetAlertDialog.WARNING_TYPE);
                        alert.setTitleText("Alerta")
                                .setContentText("Error: " + result.getMessage() + "\nCódigo: " + result.getStatus())
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            call.enqueue(this);

            /*
            Utils.buildRetrofit(this)
                    .create(WSInterface.class)
                    .checkin(checkinParam)
                    .enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            Result result = response.body();
                            if (result != null) {
                                Log.d("Sync checkin", "Message: " + result.getMessage() + "\nCode: " + result.getStatus());
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            t.printStackTrace();
                        }

                    });
                    /*
             */
        }

        btnSync.setEnabled(false);
        layProgress.setVisibility(View.GONE);
        changeToSuccessView();
    }
  /*
    Call<Result> checkinCall = Utils.buildRetrofit(OfflineActivity.this).create(WSInterface.class).checkin(checkinParam);
    call.enqueue(new Callback<Result>() {
      @Override
      public void onResponse(Call<Result> call, Response<Result> response) {
        Result result = response.body();
        if (result.getStatus() == 0) {
          checkinCall.enqueue(this);
        }
      }

      @Override
      public void onFailure(Call<Result> call, Throwable t) {

      }

    }*/


    public void updateSaleType(SaleRO.SaleType type) {

        realm.beginTransaction();

        currentUpdate.setType(type);
        realm.insertOrUpdate(currentUpdate);
        this.list.get(position).setType(type);
        realm.commitTransaction();

        ((OfflineSaleAdapter) rvList.getAdapter()).setList(this.list);
        rvList.getAdapter().notifyDataSetChanged();
    }

    public void updateSaleIdOrder(Long id_order) {
        realm.beginTransaction();
        currentUpdate.setId_order(id_order);
        realm.insertOrUpdate(currentUpdate);
        this.list.get(position).setId_order(id_order);
        realm.commitTransaction();

        this.updateSaleType(SaleRO.SaleType.SYNCED);
        position++;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResponse(Call<AddOrderResult> call, Response<AddOrderResult> response) {
        if (response.isSuccessful()) {
            AddOrderResult result = response.body();
            if (result.getId_order() > 0) {
                this.updateSaleIdOrder(result.getId_order());
                this.startUpdate();
            } else {
                Toast.makeText(this, "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
                layProgress.setVisibility(View.GONE);
                btnSync.setEnabled(true);
            }

        } else {
            Toast.makeText(this, "No se pudo realizar la sincronización verifique su internet", Toast.LENGTH_SHORT).show();
            layProgress.setVisibility(View.GONE);
            btnSync.setEnabled(true);
        }
    }

    @Override
    public void onFailure(Call<AddOrderResult> call, Throwable t) {
        Toast.makeText(this, "No se pudo realizar la sincronización verifique su internet", Toast.LENGTH_SHORT).show();
        layProgress.setVisibility(View.GONE);
        btnSync.setEnabled(true);
    }

    void changeToSuccessView() {
        setContentView(R.layout.screen_offline_success);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
