package app.logistikappbeta.com.logistikapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.CustomersAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Store;
import app.logistikappbeta.com.logistikapp.Params.GetCustomersParam;
import app.logistikappbeta.com.logistikapp.Results.GetCustomersResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomersActivity extends AppCompatActivity
    implements OnRefreshListener, View.OnClickListener,
    SearchView.OnQueryTextListener {

  private static final String TAG = "Logistikapp";

  @BindView(R.id.sRefresh)
  SwipeRefreshLayout sRefresh;
  @BindView(R.id.rvList)
  RecyclerView rvList;
  @BindView(R.id.cbNoData)
  CardView cvNoData;
  @BindView(R.id.btnAddUser)
  Button btnAddUser;

  SearchView searchView;

  ArrayList<Store> data;
  Login login;
  String token;
  Long rid;

  MenuItem itemSearch;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customers);
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);

    setTitle("Clientes");

    login = Utils.getSessionInfo(this);
    token = Utils.getToken();

    btnAddUser.setVisibility(login.getSuperuser().contentEquals("S") ? View.VISIBLE : View.GONE);

    data = new ArrayList<>();

    rid = getIntent().getLongExtra("idr", 0);

    sRefresh.setOnRefreshListener(this);
    sRefresh.setRefreshing(true);

    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new CustomersAdapter(data, this));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_customers_activity, menu);
    itemSearch = menu.findItem(R.id.iSearch);
    searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
    searchView.setOnQueryTextListener(this);
    searchView.setQueryHint("Busqueda...");
    return true;
  }

  @Override
  public void onRefresh() {
    fillList();
  }

  @Override
  public void onClick(View view) {
    finish();
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    Log.d("BUSQUEDA", "BUSCANDO " + query);
    ((CustomersAdapter) rvList.getAdapter()).search(query);
    searchView.setIconified(true);
    MenuItemCompat.collapseActionView(itemSearch);
    rvList.getAdapter().notifyDataSetChanged();
    rvList.scrollToPosition(0);
    return true;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    Log.d("BUSQUEDA", "Buscando " + newText);
    ((CustomersAdapter) rvList.getAdapter()).search(newText);
    rvList.getAdapter().notifyDataSetChanged();
    rvList.scrollToPosition(0);
    return true;
  }

  @Override
  public void onResume() {
    super.onResume();
    fillList();
  }

  @OnClick({R.id.btnAddUser})
  public void click(View v) {
    if (v.getId() == R.id.btnAddUser) {
      Intent intent = new Intent(this, AdminCustomersActivity.class);
      intent.putExtra("new", true);
      intent.putExtra("rid", rid);
      startActivity(intent);
    }
  }

  @Override
  public void onBackPressed() {
    if (!searchView.isIconified()) {
      searchView.setIconified(true);
      MenuItemCompat.collapseActionView(itemSearch);
    } else {
      finish();
    }
  }

  public void fillList() {
    GetCustomersParam param = new GetCustomersParam();
    param.setToken(new BigInteger(token));
    param.setUid(login.getId().intValue());
    param.setRid(rid.intValue());

    Call<GetCustomersResult> call = Utils.buildRetrofit(this)
        .create(WSInterface.class)
        .getCustomers(param);

    sRefresh.setRefreshing(true);
    cvNoData.setVisibility(View.GONE);

    call.enqueue(new Callback<GetCustomersResult>() {
      @Override
      public void onResponse(Call<GetCustomersResult> call, Response<GetCustomersResult> response) {
        sRefresh.setRefreshing(false);
        GetCustomersResult result = response.body();
        if (result.getStatus() == 0) {
          int size = result.getCustomers().size();

          if (size == 0) {
            cvNoData.setVisibility(View.VISIBLE);
          } else {
            cvNoData.setVisibility(View.GONE);
          }

          data.clear();
          ArrayList<Store> list = result.getCustomers();
          data.addAll(list);
          rvList.getAdapter().notifyDataSetChanged();

        } else {
          Toast.makeText(CustomersActivity.this, "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<GetCustomersResult> call, Throwable t) {
        Log.e(TAG, "Error: onFailure", t);
        sRefresh.setRefreshing(false);
        Toast.makeText(CustomersActivity.this, "Sin acceso a internet", Toast.LENGTH_SHORT).show();
        cvNoData.setVisibility(View.VISIBLE);
      }
    });
  }

}
