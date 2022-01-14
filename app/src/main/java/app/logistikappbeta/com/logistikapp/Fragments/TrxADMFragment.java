package app.logistikappbeta.com.logistikapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.logistikappbeta.com.logistikapp.Adapters.SaleByDriAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.CustomersToSaleActivity;
import app.logistikappbeta.com.logistikapp.Interfaces.MainActivityInterface;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.ReportByDri;
import app.logistikappbeta.com.logistikapp.Params.GetReportByDriParam;
import app.logistikappbeta.com.logistikapp.PrintTicketActivity;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.GetReportByDriResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrxADMFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  private static final String TAG = "LOGISTIKAPP";
  StringBuilder report = new StringBuilder();
  int d, m, y;
  final Calendar calendar = Calendar.getInstance();

  @BindView(R.id.btnCalendar)
  Button btnCalendar;
  @BindView(R.id.btnSale)
  Button btnSale;
  @BindView(R.id.cbNoData)
  CardView cvNoData;
  @BindView(R.id.sRefresh)
  SwipeRefreshLayout sRefresh;
  @BindView(R.id.rvList)
  RecyclerView rvList;

  SimpleDateFormat sdf;
  ArrayList<ReportByDri> datos;
  Login login;
  String token;

  MainActivityInterface mainActivityInterface;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_trx_adm, container, false);
    ButterKnife.bind(this, view);

    sdf = new SimpleDateFormat("dd/MM/yyyy");

    login = Utils.getSessionInfo(getContext());
    token = Utils.getToken();

    sRefresh.setOnRefreshListener(this);

    y = calendar.get(Calendar.YEAR);
    m = calendar.get(Calendar.MONTH);
    d = calendar.get(Calendar.DAY_OF_MONTH);
    btnCalendar.setText(sdf.format(new Date()));

    datos = new ArrayList<>();

    rvList.setHasFixedSize(true);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new SaleByDriAdapter(datos, getContext(), this));

    btnSale.setVisibility(login.getProfile().startsWith("SUP") ? View.VISIBLE : View.GONE);

    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mainActivityInterface = (MainActivityInterface)context;
  }

  @Override
  public void onRefresh() {
    GetReportByDriParam param = new GetReportByDriParam();
    param.setUid(login.getId());
    param.setToken(new BigInteger(token));
    param.setProfile(login.getProfile());
    param.setDate(((d < 10 ? new Integer("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y));

    sendData(param);
  }

  @OnClick({R.id.btnCalendar, R.id.btnSale, R.id.btnPrint})
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnCalendar: {
        onCreateDialog(0).show();
      }
      break;
      case R.id.btnSale: {
        getActivity().startActivity(new Intent(getContext(), CustomersToSaleActivity.class));
      }
      break;
      case R.id.btnPrint: {
        Double total_sale = 0.0;
        int total_products = 0;
        if(datos!=null && datos.size()>0){
          for (ReportByDri transaction: datos) {
            report.append("Trx: " + transaction.getNo_trx() + "\n");
            report.append("Vendedor: " + transaction.getUsername() + "\n");
            report.append("Productos: " + Math.round(transaction.getTotal_products()) + "\n");
            report.append("Venta: $" + transaction.getSales() + "\n");
            total_sale += transaction.getSales();
            total_products += transaction.getTotal_products();
          }
          report.append("Venta total: $" + total_sale + "\n");
          report.append("Productos total: " + total_products + "\n");

          Intent intent = new Intent(getContext(), PrintTicketActivity.class);
          intent.putExtra("diario_adm", "reporte_adm");
          intent.putExtra("ticket", report.toString());

          report.setLength(0);
          startActivity(intent);
        } else{
          Toast.makeText(getContext(), "No hay transacciones por imprimir" +
                  "", Toast.LENGTH_LONG).show();
          return;
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

        GetReportByDriParam param = new GetReportByDriParam();
        param.setUid(login.getId());
        param.setToken(new BigInteger(token));
        param.setProfile(login.getProfile());
        param.setDate(((d < 10 ? new Integer("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y));

        sendData(param);
      }
    }, y, m, d);
  }

  @Override
  public void onResume() {
    super.onResume();
    GetReportByDriParam param = new GetReportByDriParam();
    param.setUid(login.getId());
    param.setToken(new BigInteger(token));
    param.setProfile(login.getProfile());
    param.setDate(((d < 10 ? new Integer("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y));

    sendData(param);
    sRefresh.setRefreshing(false);
  }

  public void sendData(GetReportByDriParam param) {
    final SweetAlertDialog progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
    progress.getProgressHelper().setBarColor(getContext().getResources().getColor(R.color.colorGreen));
    progress.setTitleText("Espere")
        .setContentText("Descargando transacciones")
        .setCancelable(false);
    progress.show();

    Call<GetReportByDriResult> call = Utils.buildRetrofit(getContext())
        .create(WSInterface.class)
        .getReportByDri(param);

    call.enqueue(new Callback<GetReportByDriResult>() {
      @Override
      public void onResponse(Call<GetReportByDriResult> call, Response<GetReportByDriResult> response) {
        progress.dismiss();
        GetReportByDriResult result = response.body();
        datos.clear();

        if (result.getStatus() == 0) {
          int size = result.getTransactions().size();

          if (size == 0) {
            cvNoData.setVisibility(View.VISIBLE);
          } else {
            cvNoData.setVisibility(View.GONE);
          }

          datos.addAll(result.getTransactions());
        } else {
          Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
        }

        rvList.getAdapter().notifyDataSetChanged();
        sRefresh.setRefreshing(false);
      }

      @Override
      public void onFailure(Call<GetReportByDriResult> call, Throwable t) {
        cvNoData.setVisibility(View.VISIBLE);
        sRefresh.setRefreshing(false);
        progress.dismiss();
        Toast.makeText(getContext(), "No se pudo obtener la lista de transacciones\nVerifique su acceso a internet", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error: onFailure", t);
      }
    });

  }

  public void changeToSaleList(Long uid) {
    mainActivityInterface.changeToSaleFragment(uid, y, m, d);
  }

}
