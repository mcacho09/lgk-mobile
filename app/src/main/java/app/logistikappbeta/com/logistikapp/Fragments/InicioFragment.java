package app.logistikappbeta.com.logistikapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.MainActivityInterface;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Metric;
import app.logistikappbeta.com.logistikapp.Params.GetMetricsParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.MetricResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioFragment extends Fragment {

  int d, m, y;
  final Calendar calendar = Calendar.getInstance();

  @BindView(R.id.btnCalendar)
  Button btnCalendar;
  @BindView(R.id.tvTravels)
  TextView tvTravels;
  @BindView(R.id.tvStores)
  TextView tvStores;
  @BindView(R.id.tvCheckins)
  TextView tvCheckins;
  @BindView(R.id.tvNotCheckins)
  TextView tvNotCheckins;
  @BindView(R.id.tvSales)
  TextView tvSales;
  @BindView(R.id.lay_sales_details)
  LinearLayout lay_sales_details;
  @BindView(R.id.lay_products)
  RelativeLayout lay_products;
  @BindView(R.id.lay_decrease)
  RelativeLayout lay_decrease;
  @BindView(R.id.lay_utility)
  RelativeLayout lay_utility;
  @BindView(R.id.tvProductsQty)
  TextView tvProductsQty;
  @BindView(R.id.tvDecrease)
  TextView tvDecrease;
  @BindView(R.id.tvUtility)
  TextView tvUtility;

  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  Login login;
  String token;

  MainActivityInterface maInterface;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_inicio, container, false);
    ButterKnife.bind(this, v);

    login = Utils.getSessionInfo(getContext());
    token = Utils.getSaveToken(getContext());

    if (login.getProfile().contentEquals("DRI")) {
      lay_sales_details.setVisibility(View.GONE);
    }

    if (login.getProfile().startsWith("SUP") && !login.getSuperuser().contentEquals("S")) {
      lay_decrease.setVisibility(View.GONE);
      lay_utility.setVisibility(View.GONE);
    }

    y = calendar.get(Calendar.YEAR);
    m = calendar.get(Calendar.MONTH);
    d = calendar.get(Calendar.DAY_OF_MONTH);
    btnCalendar.setText(sdf.format(new Date()));

    sendData(sdf.format(new Date()));

    return v;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    maInterface = (MainActivityInterface) context;
  }

  @OnClick({R.id.btnCalendar, R.id.lay_travels})
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnCalendar: {
        onCreateDialog(0).show();
      }
      break;
      case R.id.lay_travels: {
        if (maInterface != null) {
          maInterface.clickItemNavigationDrawable(R.id.iViajes, y, m, d);
        }
      }
      break;
    }
  }

  protected DatePickerDialog onCreateDialog(int id) {

    return new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        y = i;
        m = i1;
        d = i2;
        btnCalendar.setText((d < 10 ? ("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y);

        sendData((d < 10 ? ("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y);
      }
    }, y, m, d);

  }

  public void sendData(String date) {
    GetMetricsParam param = new GetMetricsParam();
    param.setUid(login.getId().intValue());
    param.setProfile(login.getProfile());
    param.setToken(new BigInteger(token));
    param.setDate(date);

    final SweetAlertDialog progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
    progress.getProgressHelper().setBarColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
    progress.setTitleText("Espere")
        .setContentText("Descargando métricas")
        .setCancelable(false);
    progress.show();

    Utils.buildRetrofit(getContext()).create(WSInterface.class).getMetrics(param)
        .enqueue(new Callback<MetricResult>() {
          @Override
          public void onResponse(Call<MetricResult> call, Response<MetricResult> response) {
            MetricResult result = response.body();
            if (result == null) {
              progress.dismiss();
            } else if (result.getStatus() == 0) {
              Metric metric = result.getMetrics();
              tvTravels.setText("" + metric.getTravels());
              tvStores.setText("" + metric.getStores());
              tvCheckins.setText("" + metric.getCheckin());
              tvNotCheckins.setText("" + metric.getNotcheckin());
              tvSales.setText("" + metric.getSales());
              tvProductsQty.setText("" + metric.getProductsQty());
              tvDecrease.setText("" + metric.getDecrease());
              tvUtility.setText("" + metric.getUtility());
            } else {
              Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
          }

          @Override
          public void onFailure(Call<MetricResult> call, Throwable t) {
            tvTravels.setText("0");
            tvStores.setText("0");
            tvCheckins.setText("0");
            tvNotCheckins.setText("0");
            tvProductsQty.setText("0");
            tvSales.setText("0");
            tvDecrease.setText("0");
            tvUtility.setText("0");
            Toast.makeText(getContext(), "No se pudo obtener las metricas", Toast.LENGTH_SHORT).show();
            t.printStackTrace();
            progress.dismiss();
          }
        });

  }

}
