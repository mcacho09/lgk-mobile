package app.logistikappbeta.com.logistikapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Fragments.StockTabs.AdmGeneralStockFragment;
import app.logistikappbeta.com.logistikapp.Fragments.StockTabs.AdmStockUsersListFragment;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.Params.GetAlmacenInfoParam;
import app.logistikappbeta.com.logistikapp.Results.GetAlmacenInfoResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminStockActivity
    extends AppCompatActivity
    implements View.OnClickListener, Callback<GetAlmacenInfoResult> {

  @BindView(R.id.container)
  ViewPager mViewPager;

  private AdmGeneralStockFragment f1;
  private AdmStockUsersListFragment f2;
  private SectionsPagerAdapter mSectionsPagerAdapter;
  private BigInteger token;
  private Login login;
  private SweetAlertDialog dialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_stock);
    ButterKnife.bind(this);

    token = new BigInteger(Utils.getSaveToken(getApplicationContext()));
    login = Utils.getSessionInfo(getApplicationContext());

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(mViewPager);
    mViewPager.setOffscreenPageLimit(2);

    setTitle(getString(R.string.iAlmacen));

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);
    f1 = new AdmGeneralStockFragment();
    f2 = new AdmStockUsersListFragment();

    ArrayList<Fragment> fragments = new ArrayList<>();
    fragments.add(f1);
    fragments.add(f2);
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
    dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
    dialog.setTitleText("Espere")
        .setContentText("Obteniendo información")
        .setCancelable(false);

    getInfo();

  }

  @Override
  public void onClick(View view) {
    finish();
  }

  public void getInfo() {
    GetAlmacenInfoParam param = new GetAlmacenInfoParam();
    param.setToken(token);
    param.setUid(login.getId());

    Call<GetAlmacenInfoResult> call = Utils.buildRetrofit(getApplicationContext())
        .create(WSInterface.class)
        .getAlmacenInfo(param);
    dialog.show();
    call.enqueue(this);
  }

  @Override
  public void onResponse(Call<GetAlmacenInfoResult> call, Response<GetAlmacenInfoResult> response) {
    if (response.body() != null) {
      GetAlmacenInfoResult result = response.body();
      if (result.getStatus() == 0) {
        f1.setData(result.getInfo().getAlmacenInfo());
        f2.setData(result.getInfo().getSubalmacens());
        dialog.dismiss();
      } else {
        dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        dialog.setCancelText("Información no obtenida").setTitleText("Error");
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
  public void onFailure(Call<GetAlmacenInfoResult> call, Throwable t) {
    dialog.dismiss();
    Toast.makeText(getApplicationContext(), "Información no obtenida\nCompruebe su conexión a internet", Toast.LENGTH_SHORT).show();
    t.printStackTrace();
  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

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
      return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return "Almacén general";
        case 1:
          return "Carga Abordo";
      }
      return null;
    }
  }
}
