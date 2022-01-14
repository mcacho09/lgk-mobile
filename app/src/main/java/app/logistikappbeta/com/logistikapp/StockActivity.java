package app.logistikappbeta.com.logistikapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Fragments.StockTabs.StockChangeFragment;
import app.logistikappbeta.com.logistikapp.Fragments.StockTabs.StockCurrentFragment;
import app.logistikappbeta.com.logistikapp.Fragments.StockTabs.StockDevolutionsFragment;
import app.logistikappbeta.com.logistikapp.Fragments.StockTabs.StockSaleFragment;
import app.logistikappbeta.com.logistikapp.Fragments.StockTabs.StockStartFragment;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.ProductsAlmacenInfo;
import app.logistikappbeta.com.logistikapp.Params.GetSubAlmacenInfoParam;
import app.logistikappbeta.com.logistikapp.Results.GetSubAlmacenInfoResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockActivity extends AppCompatActivity
    implements View.OnClickListener, TabLayout.OnTabSelectedListener,
    Callback<GetSubAlmacenInfoResult> {

  private SectionsPagerAdapter mSectionsPagerAdapter;

  @BindView(R.id.container)
  ViewPager mViewPager;

  @BindView(R.id.lay_tags)
  RelativeLayout lay_tags;

  @BindView(R.id.lay_min_max)
  LinearLayout lay_min_max;

  private Login login;
  private BigInteger token;
  private SweetAlertDialog dialog;
  //private StockInfoFragment f0;
  private StockStartFragment f1;
  private StockCurrentFragment f2;
  private StockSaleFragment f3;
  private StockChangeFragment f4;
  private StockDevolutionsFragment f5;
  private GetSubAlmacenInfoResult result;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stock);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);

    ArrayList<Fragment> fragments = new ArrayList<>();

    //f0 = new StockInfoFragment();
    f1 = new StockStartFragment();
    f2 = new StockCurrentFragment();
    f3 = new StockSaleFragment();
    f4 = new StockChangeFragment();
    f5 = new StockDevolutionsFragment();
    //fragments.add(f0);
    fragments.add(f1);
    fragments.add(f2);
    fragments.add(f3);
    fragments.add(f4);
    fragments.add(f5);

    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);

    mViewPager.setOffscreenPageLimit(mSectionsPagerAdapter.getCount());
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(mViewPager);
    tabLayout.setOnTabSelectedListener(this);
    setTitle("Carga Abordo");

    login = Utils.getSessionInfo(getApplicationContext());
    token = new BigInteger(Utils.getSaveToken(getApplicationContext()));

    dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
    dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
    dialog.setTitleText("Espere")
        .setContentText("Obteniendo información de Stock")
        .setCancelable(false);

    getInfo();

    mViewPager.setCurrentItem(1);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_stock_activity, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.iPrinter: {
        if (result != null) {
          /*
          SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
          dialog.getProgressHelper().setBarColor(R.color.colorGreen);
          dialog.setTitleText("Espere por favor");
          dialog.show();
*/

          final StringBuilder print = new StringBuilder("             ALMACÉN" +
                  "\n" +
                  "Código: " + result.getSubAlmacenInfo().getAlmacen().getCode() +
                  "\n" +
                  "Responsable: " + result.getSubAlmacenInfo().getAlmacen().getUsername() +
                  "\n" +
                  "Plaza: " + result.getSubAlmacenInfo().getAlmacen().getRetail() +
                  "\n" +
                  "Total Paquetes: " + result.getSubAlmacenInfo().getAlmacen().getQty_package() +
                  "\n" +
                  "Total Piezas: " + result.getSubAlmacenInfo().getAlmacen().getQty_pieces());

          /*
          SweetAlertDialog dialog2 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
          dialog2.getProgressHelper().setBarColor(R.color.colorGreen);
          dialog2.setTitleText("Espere por favor");
          dialog2.show();
*/

          AlertDialog.Builder dialog = new AlertDialog.Builder(StockActivity.this);

          dialog.setTitle("Imprimir ticket:")
                  .setItems(new String[]{"Stock", "Vendido", "Cambios", "Mermas", "Resumen detallado"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      switch (i) {
                        case 0: {
                          print.append(getTicketProducts("STOCK", getProductsByType("VTA", result.getSubAlmacenInfo().getProducts())));
                        }
                        break;
                        case 1: {
                          print.append(getTicketProducts("VENDIDO", getProductsByType("VDO", result.getSubAlmacenInfo().getProducts())));
                        }
                        break;
                        case 2: {
                          print.append(getTicketProducts("CAMBIOS", getProductsByType("CHG", result.getSubAlmacenInfo().getProducts())));
                        }
                        break;
                        case 3: {
                          print.append(getTicketProducts("MERMAS", getProductsByType("DEV", result.getSubAlmacenInfo().getProducts())));
                        }
                        break;
                        case 4: {
                          print.append(
                            getTicketProducts("STOCK", getProductsByType("VTA", result.getSubAlmacenInfo().getProducts())) +
                            getTicketProducts("VENDIDO", getProductsByType("VDO", result.getSubAlmacenInfo().getProducts())) +
                            getTicketProducts("CAMBIOS", getProductsByType("CHG", result.getSubAlmacenInfo().getProducts())) +
                            getTicketProducts("MERMAS", getProductsByType("DEV", result.getSubAlmacenInfo().getProducts())));
                        }
                        break;
                      }
                      print.append("\nPZS = Piezas\tC = Cajas\tT = Total" +
                              "\n\n\n");
                      Intent intent = new Intent(getApplicationContext(), PrintTicketActivity.class);
                      intent.putExtra("ticket", print.toString());
                      startActivity(intent);

                      Log.i("TICKET", print.toString());
                    }
                  }).setCancelable(true).create().show();

          //dialog2.dismiss();

        }

      }
      break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  @Override
  public void onClick(View view) {
    finish();
  }

  public void getInfo() {
    dialog.show();

    GetSubAlmacenInfoParam info = new GetSubAlmacenInfoParam();
    info.setUid(login.getId());
    info.setToken(token);
    Bundle bundle = getIntent().getExtras();
    if (bundle.containsKey("uId")) {
      info.setrId(bundle.getLong("uId"));
    }
    if (bundle.containsKey("aId")) {
      info.setaId(bundle.getLong("aId"));
    }

    Call<GetSubAlmacenInfoResult> call = Utils.buildRetrofit(getApplicationContext())
        .create(WSInterface.class)
        .getSubAlmacenInfo(info);

    call.enqueue(this);

  }

  public List<ProductsAlmacenInfo> getProductsByType(String type, List<ProductsAlmacenInfo> list) {
    List<ProductsAlmacenInfo> temp = new ArrayList<>();
    for (ProductsAlmacenInfo p : list) {
      if (p.getStock_type().contentEquals(type)) {
        temp.add(p);
      }
    }
    return temp;
  }


  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  public void onResponse(Call<GetSubAlmacenInfoResult> call, Response<GetSubAlmacenInfoResult> response) {
    if (response.body() != null) {
      result = response.body();
      if (result.getStatus() == 0) {
        //f0.setProducts(result.getSubAlmacenInfo().getProductsTotal());
        //f0.setData(result.getSubAlmacenInfo().getAlmacen());
        try {
          f1.setData(result.getSubAlmacenInfo().getAlmacen(), result.getSubAlmacenInfo().getProductsInitialAlmacen());
        } catch (CloneNotSupportedException e) {
          e.printStackTrace();
        }
        f2.setData(getProductsByType("VTA", result.getSubAlmacenInfo().getProducts()));
        f3.setData(getProductsByType("VDO", result.getSubAlmacenInfo().getProducts()));
        f4.setData(getProductsByType("CHG", result.getSubAlmacenInfo().getProducts()));
        f5.setData(getProductsByType("DEV", result.getSubAlmacenInfo().getProducts()));
        dialog.dismiss();
      } else {
        dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        dialog.setTitleText("Error");
        dialog.setContentText("Verifique que tenga asignada una carga abordo");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
          @Override
          public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog.dismiss();
          }
        });
        Toast.makeText(getApplicationContext(), "Código: " + result.getStatus() + "\n" + result.getMessage(), Toast.LENGTH_SHORT).show();
      }

    } else {
      dialog.dismiss();
      Toast.makeText(getApplicationContext(), "Información no obtenida\nCompruebe su conexión a internet", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onFailure(Call<GetSubAlmacenInfoResult> call, Throwable t) {
    dialog.dismiss();
    Toast.makeText(getApplicationContext(), "Información no obtenida\nCompruebe su conexión a internet", Toast.LENGTH_SHORT).show();
    Log.e("LOGISTIKAPP", t.getLocalizedMessage());
  }

  @Override
  public void onTabSelected(TabLayout.Tab tab) {
    lay_min_max.setVisibility(tab.getPosition() == 0 ? View.VISIBLE : View.GONE);
  }

  @Override
  public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override
  public void onTabReselected(TabLayout.Tab tab) {

  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;

    public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
      super(fm);
      this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override
    public int getCount() {
      return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        /*case 0:
          return "Información";*/
        case 0:
          return "Inicial";
        case 1:
          return "Stock";
        case 2:
          return "Vendido";
        case 3:
          return "Cambios";
        case 4:
          return "Mermas";
      }
      return null;
    }

  }

  public String getTicketProducts(String title, List<ProductsAlmacenInfo> products) {

    String print = "";

    if (title.isEmpty()) return print;

    if (result.getSubAlmacenInfo().getProductsTotal() == null || products.isEmpty())
      return print;

    int space = Math.round((34 - title.length()) / 2);

    String spaces = "";

    for (int i = 0; i < space; i++) {
      spaces += " ";
    }

    print += "\n\n" + spaces + title +
        "\n";

    for (ProductsAlmacenInfo p : products) {
      Long pkg = 0l;

      Long pcs;

      if (p.getPiece_in_box() > 0) {
        pkg = p.getQty() / p.getPiece_in_box().intValue();
        pcs = p.getQty() - (pkg) * p.getPiece_in_box();
      } else {
        pcs = p.getQty();
      }

      print += p.getName_short() +
          "\nPZS: " + pcs + "\tC: " + pkg + "\tT: " + p.getQty() + "\n";
    }


    return print;
  }
}
