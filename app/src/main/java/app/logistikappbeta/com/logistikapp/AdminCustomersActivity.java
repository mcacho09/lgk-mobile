package app.logistikappbeta.com.logistikapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.mikepenz.iconics.view.IconicsButton;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.AddStoreInitialInfo;
import app.logistikappbeta.com.logistikapp.POJOs.Category;
import app.logistikappbeta.com.logistikapp.POJOs.City;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Retail;
import app.logistikappbeta.com.logistikapp.POJOs.State;
import app.logistikappbeta.com.logistikapp.POJOs.StoreInfo;
import app.logistikappbeta.com.logistikapp.Params.AddNewCustomerParam;
import app.logistikappbeta.com.logistikapp.Params.GetAddStoreInitialnfoParam;
import app.logistikappbeta.com.logistikapp.Params.GetCitiesByIdStateParam;
import app.logistikappbeta.com.logistikapp.Params.UpdateCustomerParam;
import app.logistikappbeta.com.logistikapp.Results.GetAddStoreInitialInfoResult;
import app.logistikappbeta.com.logistikapp.Results.GetCitiesByIdStateResult;
import app.logistikappbeta.com.logistikapp.Results.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCustomersActivity extends AppCompatActivity
    implements View.OnClickListener {

  private final static String TAG = "LOGISTIKAPP";

  @BindView(R.id.tvSubtitle)
  TextView tvSubtitle;
  @BindView(R.id.lay_retails)
  LinearLayout lay_retails;
  @BindView(R.id.spRetail)
  Spinner spRetail;
  @BindView(R.id.etName)
  TextView etName;
  @BindView(R.id.etCode)
  TextView etCode;
  @BindView(R.id.spState)
  Spinner spState;
  @BindView(R.id.spCity)
  Spinner spCity;
  @BindView(R.id.spCategory)
  Spinner spCategory;
  @BindView(R.id.etAddress1)
  TextView etAddress1;
  @BindView(R.id.etAddress2)
  TextView etAddress2;
  @BindView(R.id.etPostal)
  TextView etPostal;
  @BindView(R.id.etPhone)
  TextView etPhone;
  @BindView(R.id.etEmail)
  TextView etEmail;
  @BindView(R.id.tvLat)
  TextView tvLat;
  @BindView(R.id.tvLong)
  TextView tvLong;
  @BindView(R.id.etOrden)
  TextView etOrden;
  @BindView(R.id.cbActive)
  CheckBox cbActive;
  @BindView(R.id.cbPromo)
  CheckBox cbPromo;
  @BindView(R.id.btnGetPosition)
  IconicsButton btnGetPosition;

  GoogleApiClient mGApi;
  Location mLocation;
  Gson gson;
  StoreInfo si;
  Login login;
  String token;

  ArrayList<Integer> id_retails;
  ArrayList<String> retails_name;
  ArrayList<Integer> id_states;
  ArrayList<String> states_name;
  ArrayList<Integer> id_cities;
  ArrayList<String> cities_name;
  ArrayList<Integer> id_categories;
  ArrayList<String> categories_name;
  String citySearch = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_customers);
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);

    setTitle("Clientes");

    tvSubtitle.setText(getString(R.string.subtitle_admin_customers, (getIntent().getBooleanExtra("new", false) ? "Creación" : "Edición")));

    id_retails = new ArrayList<>();
    retails_name = new ArrayList<>();
    spRetail.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, retails_name));

    id_states = new ArrayList<>();
    states_name = new ArrayList<>();
    spState.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, states_name));
    spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        getCities(id_states.get(i));
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }

    });

    id_cities = new ArrayList<>();
    cities_name = new ArrayList<>();
    spCity.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities_name));

    id_categories = new ArrayList<>();
    categories_name = new ArrayList<>();
    spCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories_name));

    gson = new Gson();

    login = Utils.getSessionInfo(this);
    token = Utils.getToken();

    if (!getIntent().getBooleanExtra("nor", false)) {
      lay_retails.setVisibility(View.GONE);
    }

  }

  @Override
  protected void onResume() {
    super.onResume();
    getInfo();
    if (App.getGoogleApiHelper().isConnected()) {
      mGApi = App.getGoogleApiHelper().getGoogleApiClient();
    }
  }

  @Override
  public void onClick(View view) {
    finish();
  }

  @OnClick({R.id.btnGetPosition, R.id.btnCancel, R.id.btnSave})
  public void click(View v) {
    switch (v.getId()) {
      case R.id.btnGetPosition: {

        this.getLocation();

        if (mLocation == null || (mLocation.getLatitude() == 0 && mLocation.getLongitude() == 0)) {
          SweetAlertDialog swal = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
          swal.setTitleText("Error")
              .setContentText("No se pudo obtener la ubicación actual\nIntente nuevamente")
              .setConfirmText("Entendido")
              .show();
        } else {

          tvLat.setText("" + mLocation.getLatitude());
          tvLong.setText("" + mLocation.getLongitude());

          Address address = null;

          try {
            address = this.getLocationInfo(mLocation);
          } catch (IOException ex) {
            ex.getMessage();
          }

          if (address != null) {
            etPostal.setText(address.getPostalCode());
            etAddress1.setText(address.getAddressLine(0));
            etAddress2.setText(address.getSubLocality());
            etPhone.setText(address.getPhone());
            if (states_name.indexOf(address.getAdminArea()) > -1) {
              spState.setSelection(states_name.indexOf(address.getAdminArea()));
            }

            citySearch = address.getLocality();

            if (citySearch != null) {
              int indexCity = cities_name.indexOf(citySearch);
              spCity.setSelection(indexCity);
              if (indexCity > -1) {
                citySearch = null;
              }
            }

          }

        }
      }
      break;
      case R.id.btnCancel: {
        finish();
      }
      break;
      case R.id.btnSave: {
        if (getIntent().getBooleanExtra("new", false)) {
          addCliente();
        } else {
          updCliente();
        }
      }
      break;
    }
  }

  public Address getLocationInfo(Location location) throws IOException {

    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
    return addressList.get(0);

  }

  public void getInfo() {
    GetAddStoreInitialnfoParam param = new GetAddStoreInitialnfoParam();
    param.setCountry(1l);
    param.setSid(getIntent().getIntExtra("sid", 0));
    param.setToken(new BigInteger(token));
    param.setUid(login.getId());

    Call<GetAddStoreInitialInfoResult> call = Utils.buildRetrofit(this)
        .create(WSInterface.class)
        .getAddStoreInitialInfo(param);

    final SweetAlertDialog dialog = new SweetAlertDialog(AdminCustomersActivity.this, SweetAlertDialog.PROGRESS_TYPE);
    dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
    dialog.setTitleText("Espere")
        .setContentText("Descargando información")
        .setCancelable(false);
    dialog.show();

    call.enqueue(new Callback<GetAddStoreInitialInfoResult>() {
      @Override
      public void onResponse(Call<GetAddStoreInitialInfoResult> call, Response<GetAddStoreInitialInfoResult> response) {
        dialog.dismiss();

        GetAddStoreInitialInfoResult result = response.body();

        if (result.getStatus() == 0) {
          AddStoreInitialInfo data = result.getData();
          ArrayList<String> temp = new ArrayList<>();

          for (State i : data.getStates()) {
            id_states.add(i.getId());
            temp.add(i.getName());
          }
          states_name.addAll(temp);
          ((ArrayAdapter) spState.getAdapter()).notifyDataSetChanged();

          temp.clear();

          for (City i : data.getCities()) {
            id_cities.add(i.getId());
            temp.add(i.getName());
          }
          cities_name.addAll(temp);
          ((ArrayAdapter) spCity.getAdapter()).notifyDataSetChanged();

          temp.clear();

          for (Category i : data.getCategories()) {
            id_categories.add(i.getId());
            temp.add(i.getName());
          }
          categories_name.addAll(temp);
          ((ArrayAdapter) spCategory.getAdapter()).notifyDataSetChanged();

          if (getIntent().getBooleanExtra("nor", false)) {

            for (Retail i : data.getRetails()) {
              id_retails.add(i.getId().intValue());
              retails_name.add(i.getName());
            }
            ((ArrayAdapter) spRetail.getAdapter()).notifyDataSetChanged();
          }

          if (!getIntent().getBooleanExtra("new", false)) {
            si = data.getStore();
            etAddress1.setText(si.getAddress1());
            etAddress2.setText(si.getAddress2());
            etCode.setText(si.getCode());
            etEmail.setText(si.getEmail());
            etName.setText(si.getName());
            etOrden.setText("" + si.getOrderby());
            etPhone.setText(si.getPhone());
            etPostal.setText("" + si.getPostal());
            tvLat.setText(si.getLat().toString());
            tvLong.setText(si.getLng().toString());
            cbActive.setChecked(si.getActive() != null && si.getActive().contentEquals("S"));
            cbPromo.setChecked(si.getShelf() != null && si.getShelf().contentEquals("S"));

            if (id_states.indexOf(si.getId_state().intValue()) > -1) {
              spState.setSelection(id_states.indexOf(si.getId_state().intValue()));
            }

            if (id_cities.indexOf(si.getId_city().intValue()) > -1) {
              spCity.setSelection(id_cities.indexOf(si.getId_city().intValue()));
            }

            if (id_categories.indexOf(si.getId_category().intValue()) > -1) {
              spCategory.setSelection(id_categories.indexOf(si.getId_category().intValue()));
            }

          }
        } else {
          Toast.makeText(AdminCustomersActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
        }

      }

      @Override
      public void onFailure(Call<GetAddStoreInitialInfoResult> call, Throwable t) {
        dialog.dismiss();
        Log.e(TAG, "Error: onFailure", t);
        Toast.makeText(AdminCustomersActivity.this, "Sin acceso a internet", Toast.LENGTH_SHORT).show();
      }
    });

  }

  public void getCities(Integer sid) {
    GetCitiesByIdStateParam param = new GetCitiesByIdStateParam();
    param.setUid(login.getId());
    param.setToken(new BigInteger(token));
    param.setSid(sid.longValue());

    Call<GetCitiesByIdStateResult> call = Utils.buildRetrofit(this)
        .create(WSInterface.class)
        .getCitiesByIdState(param);

    final SweetAlertDialog dialog = new SweetAlertDialog(AdminCustomersActivity.this, SweetAlertDialog.PROGRESS_TYPE);
    dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
    dialog.setTitleText("Espere")
        .setContentText("Obteniendo municipios")
        .setCancelable(false);
    dialog.show();

    call.enqueue(new Callback<GetCitiesByIdStateResult>() {
      @Override
      public void onResponse(Call<GetCitiesByIdStateResult> call, Response<GetCitiesByIdStateResult> response) {
        dialog.dismiss();

        GetCitiesByIdStateResult result = response.body();

        if (result.getStatus() == 0) {

          ArrayList<String> temp = new ArrayList<String>();

          id_cities.clear();
          for (City i : result.getCities()) {
            id_cities.add(i.getId());
            temp.add(i.getName());
          }

          cities_name.clear();
          cities_name.addAll(temp);
          ((ArrayAdapter) spCity.getAdapter()).notifyDataSetChanged();

          if (citySearch != null) {
            int indexCity = cities_name.indexOf(citySearch);
            spCity.setSelection(indexCity);
            citySearch = null;
          } else if (si != null && id_cities.indexOf(si.getId_city().intValue()) > -1) {
            Log.d("CITY", "---> " + si.getId_city() + " : " + id_cities.get(id_cities.indexOf(si.getId_city().intValue())));
            spCity.setSelection(id_cities.indexOf(si.getId_city().intValue()));
          }

        } else {
          Toast.makeText(AdminCustomersActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<GetCitiesByIdStateResult> call, Throwable t) {
        dialog.dismiss();
        Log.e(TAG, "Error: onFailure", t);
        Toast.makeText(AdminCustomersActivity.this, "Sin acceso a internet", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void addCliente() {

    if (TextUtils.isEmpty(etName.getText())) {
      Toast.makeText(this, "Ingrese el nombre de la tienda", Toast.LENGTH_SHORT).show();
      return;
    }

    if (TextUtils.isEmpty(etCode.getText())) {
      Toast.makeText(this, "Ingrese el código de la tienda", Toast.LENGTH_SHORT).show();
      return;
    }

    if (!TextUtils.isEmpty(etEmail.getText()) && !Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
      Toast.makeText(this, "Correo electrónico incorrecto", Toast.LENGTH_SHORT).show();
      return;
    }

    AddNewCustomerParam param = new AddNewCustomerParam();
    param.setActive(cbActive.isChecked() ? "S" : "N");
    param.setUid(login.getId());
    param.setAddress1(etAddress1.getText().toString().trim());
    param.setAddress2(etAddress2.getText().toString().trim());
    param.setCode(etCode.getText().toString().trim());
    param.setCountry(1l);
    param.setEmail(etEmail.getText().toString().trim());
    param.setId_category(id_categories.get(spCategory.getSelectedItemPosition()).longValue());
    param.setId_city(id_cities.get(spCity.getSelectedItemPosition()).longValue());
    param.setId_state(id_states.get(spState.getSelectedItemPosition()).longValue());
    this.getLocation();
    if (mLocation == null) {
      param.setLat(0d);
      param.setLng(0d);
    } else {
      param.setLat(mLocation.getLatitude());
      param.setLng(mLocation.getLongitude());
    }
    param.setName(etName.getText().toString().trim());
    param.setOrderby(TextUtils.isEmpty(etOrden.getText()) ? 10 : new Integer(etOrden.getText().toString()));
    param.setPhone(etPhone.getText().toString().trim());
    param.setPostal(TextUtils.isEmpty(etPostal.getText()) ? 0 : new Integer(etPostal.getText().toString()));
    if (!getIntent().getBooleanExtra("nor", false)) {
      param.setRid(getIntent().getLongExtra("rid", 0l));
    } else {
      param.setRid(id_retails.get(spRetail.getSelectedItemPosition()).longValue());
    }
    param.setShelf(cbPromo.isChecked() ? "S" : "N");
    param.setToken(new BigInteger(token));

    Call<Result> call = Utils.buildRetrofit(this)
        .create(WSInterface.class)
        .addNewCustomer(param);

    final SweetAlertDialog dialog = new SweetAlertDialog(AdminCustomersActivity.this, SweetAlertDialog.PROGRESS_TYPE);
    dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
    dialog.setTitleText("Espere")
        .setContentText("Creando cliente")
        .setCancelable(false);
    dialog.show();

    call.enqueue(new Callback<Result>() {
      @Override
      public void onResponse(Call<Result> call, Response<Result> response) {
        dialog.dismiss();
        Result result = response.body();
        if (result.getStatus() == 0) {
          dialog.setTitleText("Éxito")
              .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
          dialog.setContentText("Se creó el cliente con éxito");
        } else {
          dialog.setTitleText("Error")
              .changeAlertType(SweetAlertDialog.ERROR_TYPE);
          dialog.setContentText("Error al crear el nuevo cliente\nIntente más tarde");
          Toast.makeText(AdminCustomersActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
        }
        dialog.setConfirmText("Aceptar")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
              @Override
              public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                finish();
              }
            }).show();
      }

      @Override
      public void onFailure(Call<Result> call, Throwable t) {
        Toast.makeText(AdminCustomersActivity.this, "Sin acceso a internet", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        Log.e(TAG, "Error: onFailure", t);
      }
    });

  }

  public void updCliente() {

    if (TextUtils.isEmpty(etName.getText())) {
      Toast.makeText(this, "Ingrese el nombre de la tienda", Toast.LENGTH_SHORT).show();
      return;
    }

    if (TextUtils.isEmpty(etCode.getText())) {
      Toast.makeText(this, "Ingrese el código de la tienda", Toast.LENGTH_SHORT).show();
      return;
    }

    if (!TextUtils.isEmpty(etEmail.getText()) && !Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
      Toast.makeText(this, "Correo electrónico incorrecto", Toast.LENGTH_SHORT).show();
      return;
    }

    UpdateCustomerParam param = new UpdateCustomerParam();
    param.setActive(cbActive.isChecked() ? "S" : "N");
    param.setUid(login.getId());
    param.setAddress1(etAddress1.getText().toString().trim());
    param.setAddress2(etAddress2.getText().toString().trim());
    param.setCode(etCode.getText().toString().trim());
    param.setCountry(1l);
    param.setEmail(etEmail.getText().toString().trim());
    param.setId_category(id_categories.get(spCategory.getSelectedItemPosition()).longValue());
    param.setId_city(id_cities.get(spCity.getSelectedItemPosition()).longValue());
    param.setId_state(id_states.get(spState.getSelectedItemPosition()).longValue());

    this.getLocation();

    if (mLocation != null) {
      param.setLat(mLocation.getLatitude());
      param.setLng(mLocation.getLongitude());
    } else if (si != null) {
      param.setLat(si.getLat());
      param.setLng(si.getLng());
    }
    param.setName(etName.getText().toString().trim());
    param.setOrderby(new Integer(etOrden.getText().toString()));
    param.setPhone(etPhone.getText().toString().trim());
    param.setPostal(TextUtils.isEmpty(etPostal.getText()) ? 0 : new Integer(etPostal.getText().toString()));
    param.setShelf(cbPromo.isChecked() ? "S" : "N");
    param.setToken(new BigInteger(token));
    Integer id = getIntent().getIntExtra("sid", 0);
    param.setId(id.longValue());
    param.setRid(getIntent().getLongExtra("rid", 0l));

    Call<Result> call = Utils.buildRetrofit(this)
        .create(WSInterface.class)
        .updateCustomer(param);

    final SweetAlertDialog dialog = new SweetAlertDialog(AdminCustomersActivity.this, SweetAlertDialog.PROGRESS_TYPE);
    dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
    dialog.setTitleText("Espere")
        .setContentText("Actualizando cliente")
        .setCancelable(false);
    dialog.show();

    call.enqueue(new Callback<Result>() {
      @Override
      public void onResponse(Call<Result> call, Response<Result> response) {
        dialog.dismiss();
        Result result = response.body();
        if (result.getStatus() == 0) {
          dialog.setTitleText("Éxito")
              .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
          dialog.setContentText("Actualizó cliente \ncon éxito");
        } else {
          dialog.setTitleText("Error")
              .changeAlertType(SweetAlertDialog.ERROR_TYPE);
          dialog.setContentText("Error al actualizar el cliente\nIntente más tarde");
          Toast.makeText(AdminCustomersActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
        }
        dialog.setConfirmText("Aceptar")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
              @Override
              public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
              }
            }).show();
      }

      @Override
      public void onFailure(Call<Result> call, Throwable t) {
        dialog.dismiss();
        Toast.makeText(AdminCustomersActivity.this, "Sin acceso a internet", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error: onFailure", t);
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  void getLocation() {
    if (mGApi == null) return;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
          PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
          Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGApi);
      }
    } else {
      mLocation = LocationServices.FusedLocationApi.getLastLocation(mGApi);
    }
  }
}
