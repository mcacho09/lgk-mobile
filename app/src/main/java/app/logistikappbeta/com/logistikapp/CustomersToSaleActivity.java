package app.logistikappbeta.com.logistikapp;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.CustomersToSaleAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Store;
import app.logistikappbeta.com.logistikapp.Params.GetCRMParams;
import app.logistikappbeta.com.logistikapp.Results.CRMResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomersToSaleActivity extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,
    SearchView.OnQueryTextListener {

  private static final String TAG = "Logistikapp";

  @BindView(R.id.sRefresh)
  SwipeRefreshLayout sRefresh;
  @BindView(R.id.rvList)
  RecyclerView rvList;
  @BindView(R.id.cbNoData)
  CardView cvNoData;
  @BindView(R.id.etBusqueda)
  EditText etBusqueda;

  SearchView searchView;

  ArrayList<Store> data;
  Login login;
  String token;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customers_to_sale);
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);

    setTitle("Venta Clientes");

    login = Utils.getSessionInfo(this);
    token = Utils.getToken();

    data = new ArrayList<>();

    sRefresh.setOnRefreshListener(this);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new CustomersToSaleAdapter(data, this));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_customers_activity, menu);

    return true;
  }

  @Override
  public void onRefresh() {
    if (!TextUtils.isEmpty(etBusqueda.getText()) || etBusqueda.getText().length() < 2) {
      fillList();
    } else {
      etBusqueda.setError("Ingresar mínimo un carácter");
      sRefresh.setRefreshing(false);
    }
  }

  @Override
  public void onClick(View view) {
    finish();
  }

  @OnClick({R.id.btnBusqueda})
  public void click(View v) {
    switch (v.getId()) {
      case R.id.btnBusqueda: {
        if (!TextUtils.isEmpty(etBusqueda.getText()) && etBusqueda.getText().length() >= 2) {
          sRefresh.setRefreshing(true);
          fillList();
        } else {
          etBusqueda.setError("Ingresar mínimo dos caracteres");
          sRefresh.setRefreshing(false);
        }
      }
      break;
    }
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    ((CustomersToSaleAdapter) rvList.getAdapter()).search(query);
    rvList.getAdapter().notifyDataSetChanged();
    searchView.setIconified(true);
    return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    ((CustomersToSaleAdapter) rvList.getAdapter()).search(newText);
    rvList.getAdapter().notifyDataSetChanged();
    return false;
  }

  public void fillList() {
    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    if (inputMethodManager != null) {
      inputMethodManager.hideSoftInputFromWindow(etBusqueda.getWindowToken(), 0);
    }

    GetCRMParams param = new GetCRMParams();
    param.setOption(1);
    param.setProfile(login.getProfile());
    param.setSearch(etBusqueda.getText().toString());
    param.setToken(new BigInteger(token));
    param.setUid(login.getId().intValue());

    sRefresh.setRefreshing(true);
    cvNoData.setVisibility(View.GONE);

    Call<CRMResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getCRM(param);
    call.enqueue(new Callback<CRMResult>() {
      @Override
      public void onResponse(Call<CRMResult> call, Response<CRMResult> response) {
        data.clear();
        CRMResult result = response.body();
        if (result.getStatus() == 0) {

          if (result.getStores().size() == 0) {
            cvNoData.setVisibility(View.VISIBLE);
          } else {
            cvNoData.setVisibility(View.GONE);
          }

          data.addAll(result.getStores());

        } else {
          Toast.makeText(CustomersToSaleActivity.this, "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
        }

        rvList.getAdapter().notifyDataSetChanged();
        sRefresh.setRefreshing(false);
      }

      @Override
      public void onFailure(Call<CRMResult> call, Throwable t) {
        t.printStackTrace();
        rvList.getAdapter().notifyDataSetChanged();
        sRefresh.setRefreshing(false);
      }
    });
  }

  @Override
  public void onBackPressed() {
    if (!searchView.isIconified()) {
      searchView.setIconified(true);
    } else if (sRefresh.isRefreshing()) {
      sRefresh.setRefreshing(false);
    } else {
      finish();
    }
  }
}
