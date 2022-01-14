package app.logistikappbeta.com.logistikapp;

import android.content.Intent;
import android.graphics.PorterDuff;
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
import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.LinkedList;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Fragments.TransactionDetailTabs.TDDos;
import app.logistikappbeta.com.logistikapp.Fragments.TransactionDetailTabs.TDUno;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Transaction;
import app.logistikappbeta.com.logistikapp.Params.SendTicketParam;
import app.logistikappbeta.com.logistikapp.Results.SendTicketResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionDetailActivity extends AppCompatActivity implements View.OnClickListener,
    TabLayout.OnTabSelectedListener {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.vPager)
  ViewPager vPager;
  @BindView(R.id.tabs)
  TabLayout tabLayout;

  Long id_order;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transaction_detail);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);
    setTitle("Transacciones");
    String price_sale = getIntent().getStringExtra("total_price");
    String transaction = getIntent().getStringExtra("transaction");
    Transaction trx = new Gson().fromJson(transaction, Transaction.class);

    id_order = (long) trx.getId();
    /*String content = getIntent().getStringExtra("transaction");
    Gson gson = new Gson();
    TransactionList transaction = gson.fromJson(content, TransactionList.class);*/

    MyPageAdapter pageAdapter = new MyPageAdapter(getSupportFragmentManager());
    TDUno uno = new TDUno();
    Bundle bundle = new Bundle();
    bundle.putString("transaction", transaction);
    if(!price_sale.equals("0.0")) price_sale = Double.toString(trx.getSale());
    bundle.putDouble("sale", Double.parseDouble(price_sale));
    uno.setArguments(bundle);
    pageAdapter.addFragment(uno, "UNO");

    TDDos dos = new TDDos();
    bundle = new Bundle();
    bundle.putDouble("sale", Double.parseDouble(price_sale));
    bundle.putDouble("sale_dev", trx.getSale_dev());
    String saleCont = getIntent().getStringExtra("vtalist");
    bundle.putString("vtalist", saleCont);
    String devCont = getIntent().getStringExtra("devlist");
    bundle.putString("devlist", devCont);
    String chgCont = getIntent().getStringExtra("chglist");
    bundle.putString("chglist", chgCont);
    dos.setArguments(bundle);

    pageAdapter.addFragment(dos, "DOS");
    vPager.setAdapter(pageAdapter);
    vPager.setOffscreenPageLimit(2);

    tabLayout.setupWithViewPager(vPager);
    tabLayout.getTabAt(0).setIcon(R.drawable.ic_attach_money);
    tabLayout.getTabAt(1).setIcon(R.drawable.ic_library_books);

    tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_IN);
    tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_IN);

    tabLayout.addOnTabSelectedListener(this);

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

  protected class MyPageAdapter extends FragmentPagerAdapter {

    LinkedList<Fragment> list;
    LinkedList<String> listTitles;

    public MyPageAdapter(FragmentManager fm) {
      super(fm);
      list = new LinkedList<>();
      listTitles = new LinkedList<>();
    }

    @Override
    public Fragment getItem(int position) {
      return list.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
      list.add(fragment);
      listTitles.add(title);
    }

    @Override
    public int getCount() {
      return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return null;
    }

  }

  @OnClick({R.id.btnSendTicket, R.id.btnPrintTicket})
  public void click(View v) {

    switch (v.getId()) {
      case R.id.btnSendTicket: {
        final SweetAlertDialog alert = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        alert.setTitleText("Espere")
            .setContentText("Enviando ticket")
            .setCancelable(false);

        alert.show();

        SendTicketParam param = new SendTicketParam();
        param.setOid(id_order);
        param.setUid(Utils.getSessionInfo(this).getId());
        param.setToken(new BigInteger(Utils.getSaveToken(this)));

        Call<SendTicketResult> call = Utils.buildRetrofit(this).create(WSInterface.class).sendTicket(param);
        call.enqueue(new Callback<SendTicketResult>() {
          @Override
          public void onResponse(Call<SendTicketResult> call, Response<SendTicketResult> response) {
            SendTicketResult result = response.body();
            if (result.getStatus() == 0) {
              if (result.getResult() != null && !result.getResult().isEmpty()) {
                alert.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                alert.setTitleText("Alerta")
                    .setContentText(result.getResult())
                    .show();
              } else {
                alert.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                alert.setTitleText("Exito")
                    .setContentText("Ticket enviado")
                    .show();
              }
            } else {
              Toast.makeText(TransactionDetailActivity.this, "Error: " + result.getStatus() + "\n" +
                  result.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
          @Override
          public void onFailure(Call<SendTicketResult> call, Throwable t) {
            alert.dismiss();
            Toast.makeText(TransactionDetailActivity.this, "Error al enviar ticket\nCompruebe su conexión a internet", Toast.LENGTH_SHORT).show();
          }
        });

      }
      break;

      case R.id.btnPrintTicket: {

        Intent intent = new Intent(this, PrintTicketActivity.class);
        intent.putExtra("id_order", id_order);
        startActivity(intent);

      }
      break;
    }

  }

}
