package app.logistikappbeta.com.logistikapp;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.math.BigInteger;

import app.logistikappbeta.com.logistikapp.Broadcasts.InternetReceiver;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Fragments.CRMFragment;
import app.logistikappbeta.com.logistikapp.Fragments.ClientFragment;
import app.logistikappbeta.com.logistikapp.Fragments.HistoricoFragment;
import app.logistikappbeta.com.logistikapp.Fragments.InicioFragment;
import app.logistikappbeta.com.logistikapp.Fragments.MessageFragment;
import app.logistikappbeta.com.logistikapp.Fragments.MiPerfilFragment;
import app.logistikappbeta.com.logistikapp.Fragments.NotificacionesFragment;
import app.logistikappbeta.com.logistikapp.Fragments.TransactListFragment;
import app.logistikappbeta.com.logistikapp.Fragments.TrxADMFragment;
import app.logistikappbeta.com.logistikapp.Fragments.UsuariosFragment;
import app.logistikappbeta.com.logistikapp.Fragments.ViajesFragment;
import app.logistikappbeta.com.logistikapp.Interfaces.MainActivityInterface;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.Params.GetSupplierParam;
import app.logistikappbeta.com.logistikapp.Results.GetSupplierResult;
import app.logistikappbeta.com.logistikapp.Services.LocationRegisterService;
import app.logistikappbeta.com.logistikapp.Services.NotificationService;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainActivityInterface,
        Callback<GetSupplierResult> {

  private Menu menu;
  private Menu menuTop;
  private Login login;
  private BigInteger token;
  private Gson gson;

  private Boolean isMsg = false;
  private Boolean isNot = false;
  private Long noNots = 0l;
  private Long noMsg = 0l;

  private NotificationManager notificationManager;

  private Intent intentUserLocationService;
  private Intent intentNotificationsService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    intentNotificationsService = new Intent(this, NotificationService.class);
    intentUserLocationService = new Intent(this, LocationRegisterService.class);
    registerReceiver(new InternetReceiver(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    login = Utils.getSessionInfo(this);
    token = new BigInteger(Utils.getSaveToken(this));
    gson = new Gson();

    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

      if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
              android.Manifest.permission.ACCESS_FINE_LOCATION)) {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
      }

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    setTitle("Inicio");
    menu = navigationView.getMenu();

    isMsg = getIntent().getBooleanExtra("msg", false);
    isNot = getIntent().getBooleanExtra("not", false);

    if (isMsg) {
      setTitle("Mensajes");
      getSupportFragmentManager().beginTransaction().replace(R.id.lay_fragment, new MessageFragment()).commit();
      isMsg = true;
      noMsg = 0l;
    } else if (isNot) {
      setTitle("Notificaciones");
      getSupportFragmentManager().beginTransaction().replace(R.id.lay_fragment, new NotificacionesFragment()).commit();
      isNot = true;
      noNots = 0l;
    } else {
      if (!login.getProfile().contentEquals("SOP")) {
        onNavigationItemSelected(menu.findItem(R.id.iInicio));
      } else {
        onNavigationItemSelected(menu.findItem(R.id.iMensajes));
      }
    }

    menu.findItem(R.id.iCustomer).setVisible(login.getSuperuser().contentEquals("S"));
    String version = "0.0.0";
    version = Utils.getVersionApp();
    menu.findItem(R.id.iVersion).setTitle(getString(R.string.iVersion, version));
    if (!login.getSuperuser().contentEquals("S")) {
      menu.findItem(R.id.iCustomer).setVisible(false);
    }

    if (!login.getProfile().startsWith("SUP")) {
      menu.findItem(R.id.iConfiguracion).setVisible(false);
    }

    if (login.getProfile().contentEquals("SOP")) {
      menu.findItem(R.id.iInicio).setVisible(false);
      menu.findItem(R.id.iLogistica).setVisible(false);
      menu.findItem(R.id.iVentas).setVisible(false);
      menu.findItem(R.id.iCustomer).setVisible(false);
      menu.findItem(R.id.iConfiguracion).setVisible(false);
      menu.findItem(R.id.iNotificacion).setVisible(false);
    } else {
      startService(intentNotificationsService);
      if (login.getProfile().contains("DRI")) {
        startService(intentUserLocationService);
      }
    }

    /* 04/29/2020
    /* Erick Gtz
    //Start background Service */
    if (login.getProfile().contains("DRI") && !login.getUbi_time().isEmpty()) {
      startLocationService(login.getUbi_time());
    }
    super.onResume();
  }

  private void stopLocationService() {
    //stop background location service
    Intent intent = new Intent(this, MyBackgroundLocationService.class);
    stopService(intent);
    //Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
  }

  private void startLocationService(String ubi_time) {
    //start background location service
    Intent intent = new Intent(this, MyBackgroundLocationService.class);
    intent.putExtra("ubi_time", ubi_time);
    ContextCompat.startForegroundService(this, intent);
    //Toast.makeText(this, "Actualizando ubicación en segundo plano...",
     //       Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void onStart() {
    super.onStart();
    getSupplier();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
      dialog.setTitleText("Alerta");
      dialog.setContentText("¿Desea cerrar la sesión actual?")
              .setCancelText("No")
              .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                  sweetAlertDialog.dismiss();
                }
              })
              .setConfirmText("Sí")
              .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                  sweetAlertDialog.dismiss();
                  closeSession();
                }
              })
              .show();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);

    if (login.getProfile().contentEquals("SOP")) {
      menu.findItem(R.id.iCRM).setVisible(false);
      menu.findItem(R.id.iNotificacion).setVisible(false);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();
    isNot = false;
    isMsg = false;

    switch (id) {
      case R.id.iCRM: {
        getSupportFragmentManager().beginTransaction().replace(R.id.lay_fragment, new CRMFragment()).commit();
        setTitle(getTitle(R.string.iCRM));
      }
      break;
      case R.id.iNotificacion: {
        getSupportFragmentManager().beginTransaction().replace(R.id.lay_fragment, new NotificacionesFragment()).commit();
        setTitle(getTitle(R.string.iNotificaciones));
        isNot = true;
        noNots = 0l;
      }
      break;
      case R.id.iMensaje: {
        getSupportFragmentManager().beginTransaction().replace(R.id.lay_fragment, new MessageFragment()).commit();
        setTitle(getTitle(R.string.iMensajes));
        isMsg = true;
        noMsg = 0l;
      }
      break;
      case R.id.iMiPerfil: {
        getSupportFragmentManager().beginTransaction().replace(R.id.lay_fragment, new MiPerfilFragment()).commit();
        setTitle(getTitle(R.string.iMiPerfil));
      }
      break;
            /*case R.id.iTareas:{
                getSupportFragmentManager().beginTransaction().replace(R.id.lay_fragment, new TareasFragment()).commit();
                setTitle(getTitle(R.string.iTareas));
            }break;*/
      case R.id.iCerrarSesion: {
        closeSession();
        stopLocationService();
      }
      break;
    }
    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.

    int id = item.getItemId();

    int idString = 0;
    Fragment fragment = null;

    isMsg = false;
    isNot = false;

    switch (id) {
      case R.id.iInicio: {
        fragment = new InicioFragment();
        idString = R.string.iInicio;
      }
      break;
      case R.id.iCustomer: {
        fragment = new ClientFragment();
        idString = R.string.iCustomers;
      }
      break;
      case R.id.iViajes: {
        fragment = new ViajesFragment();
        idString = R.string.iViajes;
      }
      break;
      case R.id.iHistorico: {
        fragment = new HistoricoFragment();
        idString = R.string.iHistorico;
      }
      break;

      case R.id.iMensajes: {
        fragment = new MessageFragment();
        idString = R.string.iMensajes;
        isMsg = true;
        noMsg = 0l;
      }
      break;

      case R.id.iNotificacion: {
        fragment = new NotificacionesFragment();
        idString = R.string.iNotificaciones;
        isNot = true;
        noNots = 0l;
      }
      break;
      case R.id.iTransacciones: {
        if (login.getProfile().startsWith("SUP")) {
          fragment = new TrxADMFragment();
        } else {
          fragment = new TransactListFragment();
        }
        idString = R.string.iTransacciones;
      }
      break;
      case R.id.iUsuarios: {
        fragment = new UsuariosFragment();
        idString = R.string.iUsuarios;
      }
      break;
      case R.id.iAlmacen: {
        if (this.login.getProfile().contains("SUP")) {
          startActivity(new Intent(this, AdminStockActivity.class));
        } else {
          Intent intent = new Intent(this, StockActivity.class);
          intent.putExtra("uId", login.getId());
          startActivity(intent);
        }
      }
      break;
      case R.id.iOffline: {
        startActivity(new Intent(this, OfflineActivity.class));
      }
      break;

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);

    if (fragment != null) {
      getSupportFragmentManager().beginTransaction().replace(R.id.lay_fragment, fragment).commit();
      setTitle(getTitle(idString));
    }
    return true;
  }

  public String getTitle(int string) {
    return MainActivity.this.getResources().getString(string);
  }

  @Override
  public void clickItemNavigationDrawable(int item, int y, int m, int d) {
    Fragment fragment = null;
    switch (item) {
      case R.id.iViajes: {
        fragment = new ViajesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("y", y);
        bundle.putInt("m", m);
        bundle.putInt("d", d);
        fragment.setArguments(bundle);
        setTitle(getTitle(R.string.iViajes));
      }
    }
    if (fragment != null) {
      getSupportFragmentManager().beginTransaction().replace(R.id.lay_fragment, fragment).commit();
    }
  }

  @Override
  public void changeToSaleFragment(Long uid, int y, int m, int d) {
    Fragment fragment = new TransactListFragment();
    Bundle bundle = new Bundle();
    bundle.putLong("uid", uid);
    bundle.putInt("d", d);
    bundle.putInt("m", m);
    bundle.putInt("y", y);
    fragment.setArguments(bundle);
    getSupportFragmentManager().beginTransaction().replace(R.id.lay_fragment, fragment).commit();
  }

  @Override
  protected void onDestroy() {
    stopService(intentNotificationsService);
    stopService(intentUserLocationService);
    notificationManager.cancelAll();
    super.onDestroy();
  }

  void closeSession() {
    if (Utils.finishSession(MainActivity.this)) {

      Utils.deleteToken(MainActivity.this);
      Utils.deleteSupplier(MainActivity.this);

      startActivity(new Intent(MainActivity.this, LoginActivity.class));
      notificationManager.cancelAll();
      stopService(intentNotificationsService);
      stopService(intentUserLocationService);
      finish();

    } else {
      Toast.makeText(MainActivity.this, "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
    }
  }

  void getSupplier() {
    String supplier = Utils.getSupplierPreferences(this);

    //if (supplier == null || supplier.isEmpty()) {

    GetSupplierParam param = new GetSupplierParam();
    param.setUid(login.getId().intValue());
    param.setToken(token);

    Call<GetSupplierResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getSupplier(param);
    call.enqueue(this);

    //}
  }

  @Override
  public void onResponse(Call<GetSupplierResult> call, Response<GetSupplierResult> response) {
    if (response.isSuccessful()) {
      GetSupplierResult result = response.body();
      if (result.getStatus() == 0) {
        if (Utils.saveSupplier(this, result.getSupplier())) {
          Log.d("LGK", "Supplier saved: " + result.getSupplier());
        } else {
          Log.e("LGK", "Supplier not saved: " + result.getSupplier());
        }
      } else {
        Log.w("LGK", "Error: " + result.getStatus() + " - " + result.getMessage());
      }
    } else {
      Log.w("LGK", "No se pudo obtener respuesta: Response.isSuccessful() igual a false");
    }
  }

  @Override
  public void onFailure(Call<GetSupplierResult> call, Throwable t) {
    Log.e("LGK", "onFailure_GetSupplierResult", t);
  }

}