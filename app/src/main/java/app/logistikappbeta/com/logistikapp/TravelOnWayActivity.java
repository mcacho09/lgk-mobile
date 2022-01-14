package app.logistikappbeta.com.logistikapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.LinkedList;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Fragments.TravelOnWayTabs.TOWDosFragment;
import app.logistikappbeta.com.logistikapp.Fragments.TravelOnWayTabs.TOWTresFragment;
import app.logistikappbeta.com.logistikapp.Fragments.TravelOnWayTabs.TOWUnoFragment;
import app.logistikappbeta.com.logistikapp.Interfaces.TOWInterface;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.Params.GetTravelStoresParam;
import app.logistikappbeta.com.logistikapp.Results.TravelStoresResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelOnWayActivity extends AppCompatActivity implements View.OnClickListener,
    TabLayout.OnTabSelectedListener, TOWInterface {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.vPager)
  ViewPager vPager;
  @BindView(R.id.tabs)
  TabLayout tabLayout;

  TOWUnoFragment f1;
  TOWDosFragment f2;
  TOWTresFragment f3;

  GetTravelStoresParam param;
  Gson gson;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_travel_way_bill);
    ButterKnife.bind(this);
    setTitle("Viaje");
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    gson = new Gson();
    String params = getIntent().getStringExtra("params");
    Log.i("PARAMS", params);
    param = gson.fromJson(params, GetTravelStoresParam.class);

    VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
    f1 = new TOWUnoFragment();

    Bundle bundle = new Bundle();
    bundle.putLong("idTravel", param.getTid());
    f2 = new TOWDosFragment();
    f2.setArguments(bundle);
    f3 = new TOWTresFragment();

    adapter.addFragment(f1, "UNO");
    adapter.addFragment(f2, "DOS");
    adapter.addFragment(f3, "TRES");
    vPager.setAdapter(adapter);
    vPager.setOffscreenPageLimit(3);

    tabLayout.setupWithViewPager(vPager);
    tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_shipping_white);
    tabLayout.getTabAt(1).setIcon(R.drawable.ic_place_white);
    tabLayout.getTabAt(2).setIcon(R.drawable.ic_public);

    tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_IN);
    tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_IN);
    tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_IN);

    tabLayout.addOnTabSelectedListener(this);

    toolbar.setNavigationOnClickListener(this);



    this.updateInfoTravel();
  }

  @Override
  protected void onResume() {
    super.onResume();
    this.updateInfoTravel();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  @Override
  public void onClick(View view) {
    finish();
  }

  @Override
  public void onTabSelected(TabLayout.Tab tab) {
    tab.getIcon().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_IN);
  }

  @Override
  public void onTabUnselected(TabLayout.Tab tab) {
    tab.getIcon().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_IN);
  }

  @Override
  public void onTabReselected(TabLayout.Tab tab) {

  }

  @Override
  public void changeMarkerState(int position, int status) {
    f3.changeMarkerState(position, status);
  }

  @Override
  public void moveToStoreListPosition(int positionMarker) {
    f2.moveToStoreListPosition(positionMarker);
  }

  @Override
  public void updateInfoTravel() {
    updateTravelAndStoresInfo();
  }

  protected class VPAdapter extends FragmentPagerAdapter {

    private LinkedList<Fragment> listFragment = new LinkedList<>();
    private LinkedList<String> listTitles = new LinkedList<>();

    public VPAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return listFragment.get(position);
    }

    @Override
    public int getCount() {
      return listFragment.size();
    }

    public void addFragment(Fragment fragment, String title) {
      listFragment.add(fragment);
      listTitles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return null;
    }

  }

  public void updateTravelAndStoresInfo() {
    final SweetAlertDialog progress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
    progress.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorGreen));
    progress.setTitleText("Espere")
        .setContentText("Obteniendo información del viaje")
        .setCancelable(false);
    progress.show();

    Call<TravelStoresResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getTravelStores(param);
    call.enqueue(new Callback<TravelStoresResult>() {
      @Override
      public void onResponse(Call<TravelStoresResult> call, Response<TravelStoresResult> response) {

        if (!response.isSuccessful()) {
          Toast.makeText(TravelOnWayActivity.this, "Viaje no obtenido, comprueba tu internet", Toast.LENGTH_SHORT).show();
          return;
        }

        TravelStoresResult result = response.body();
        if (result.getStatus() == 0) {
          f1.updateTravel(result.getTravel());
          f2.updateStores(result.getStores(), result.getTravel());
          f3.updateStores(result.getTravel().getDriver(), gson.toJson(result.getStores()));
        } else {
          Toast.makeText(TravelOnWayActivity.this, "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
        }
        progress.dismiss();
      }

      @Override
      public void onFailure(Call<TravelStoresResult> call, Throwable t) {
        t.printStackTrace();
        progress.dismiss();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == 2 && Utils.isOnline(this)) {
        updateTravelAndStoresInfo();
      }
    }
  }
}
