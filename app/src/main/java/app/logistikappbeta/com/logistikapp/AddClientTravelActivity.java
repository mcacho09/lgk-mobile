package app.logistikappbeta.com.logistikapp;

import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.AddClientTravelAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.AddClient;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.Params.AddStoreTravelParam;
import app.logistikappbeta.com.logistikapp.Params.GetClientTravelParam;
import app.logistikappbeta.com.logistikapp.Results.ClientTravelResult;
import app.logistikappbeta.com.logistikapp.Results.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClientTravelActivity extends AppCompatActivity implements View.OnClickListener {

  @BindView(R.id.etSearch)
  EditText etSearch;
  @BindView(R.id.rvList)
  RecyclerView rvList;

  private ArrayList<AddClient> data;
  private Long idTravel;
  private Login login;
  private BigInteger token;
  private Gson gson;
  private boolean isAdd = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_client_travel);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);

    setTitle("Viajes");

    idTravel = getIntent().getLongExtra("idTravel", 0l);
    login = Utils.getSessionInfo(this);
    token = new BigInteger(Utils.getSaveToken(this));

    gson = new Gson();

    data = new ArrayList<>();

    rvList.setHasFixedSize(true);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new AddClientTravelAdapter(data));

  }

  @Override
  public void onClick(View view) {
    finish();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    setResult(isAdd ? RESULT_OK : RESULT_CANCELED);
    finish();
  }

  @OnClick({R.id.btnSearch, R.id.btnCancel, R.id.btnAdd})
  public void OnClick(View v) {

    switch (v.getId()) {
      case R.id.btnSearch: {

        if (TextUtils.isEmpty(etSearch.getText())) {
          etSearch.setError("Ingresar mínimo un carácter");
        } else {

          getClientes();

        }

      }
      break;
      case R.id.btnCancel: {
        setResult(isAdd ? RESULT_OK : RESULT_CANCELED);
        finish();
      }
      break;
      case R.id.btnAdd: {

        ArrayList<Long> ids = new ArrayList<>();

        Log.d(getString(R.string.app_name), gson.toJson(data));

        for (AddClient i : data) {
          if (i.getSelected()) {
            ids.add(i.getId_store());
          }
        }

        if (ids.size() > 0) {

          final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
          dialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorGreen));
          dialog.setTitleText("Espere por favor")
              .setContentText("Agregando clientes al viaje")
              .setCancelable(false);
          dialog.show();

          AddStoreTravelParam param = new AddStoreTravelParam();
          param.setTid(idTravel);
          param.setToken(token);
          param.setUid(login.getId());
          param.setIds(ids);

          Call<Result> call = Utils.buildRetrofit(this).create(WSInterface.class).addStoreTravel(param);
          call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
              Result result = response.body();
              dialog.dismiss();
              SweetAlertDialog alert = new SweetAlertDialog(AddClientTravelActivity.this,
                  (result.getStatus() == 0 ? SweetAlertDialog.SUCCESS_TYPE :
                      SweetAlertDialog.ERROR_TYPE));
              alert.setTitleText((result.getStatus() == 0 ? "Exito" : "Error") +
                  " al agregar los clientes");

              if (result.getStatus() == 0) {
                alert.setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                      @Override
                      public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                      }
                    });
              }

              alert.show();
              getClientes();
              if (result.getStatus() > 0) {
                Toast.makeText(AddClientTravelActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
              }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
              t.printStackTrace();
              dialog.dismiss();
              Toast.makeText(AddClientTravelActivity.this, "No se obtuvo respuesta", Toast.LENGTH_SHORT).show();
            }
          });

        } else {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Snackbar snackbar = Snackbar.make(v,
                "Debe seleccionar minimo un cliente para agregar",
                Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(getResources().getColor(R.color.colorRedInfo));
            snackbar.setAction("Ok", new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                snackbar.dismiss();
              }
            }).show();
          } else {
            Toast.makeText(AddClientTravelActivity.this, "Debe seleccionar minimo un cliente para agregar", Toast.LENGTH_SHORT).show();
          }
        }

      }
      break;
    }

  }

  void getClientes() {

    final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
    dialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorGreen));
    dialog.setTitleText("Espere por favor")
        .setContentText("Obteniendo listado de clientes")
        .setCancelable(false);
    dialog.show();

    GetClientTravelParam param = new GetClientTravelParam();
    param.setSearch(etSearch.getText().toString());
    param.setUid(login.getId());
    param.setToken(token);
    param.setTid(idTravel);

    Call<ClientTravelResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getClientTravel(param);
    call.enqueue(new Callback<ClientTravelResult>() {
      @Override
      public void onResponse(Call<ClientTravelResult> call, Response<ClientTravelResult> response) {
        dialog.dismiss();
        data.clear();

        ClientTravelResult result = response.body();

        if (result.getStores() != null) {

          for (AddClient add : result.getStores()) {
            add.setSelected(false);
          }
          data.addAll(result.getStores());

        }
        rvList.getAdapter().notifyDataSetChanged();
      }

      @Override
      public void onFailure(Call<ClientTravelResult> call, Throwable t) {
        t.printStackTrace();
        dialog.dismiss();
        Toast.makeText(AddClientTravelActivity.this, "No se pudo obtener los clientes", Toast.LENGTH_SHORT).show();
      }
    });
  }

}