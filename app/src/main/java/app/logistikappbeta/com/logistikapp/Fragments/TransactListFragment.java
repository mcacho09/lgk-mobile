package app.logistikappbeta.com.logistikapp.Fragments;

import android.app.DatePickerDialog;
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

import app.logistikappbeta.com.logistikapp.Adapters.TransactionListAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.CustomersToSaleActivity;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.TransactionList;
import app.logistikappbeta.com.logistikapp.Params.GetSaleByDriParam;
import app.logistikappbeta.com.logistikapp.PrintTicketActivity;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.TransactionListResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

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

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    ArrayList<TransactionList> datos;
    Login login;
    String token;
    StringBuilder report = new StringBuilder();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transact_list, container, false);
        ButterKnife.bind(this, view);

        login = Utils.getSessionInfo(getContext());
        token = Utils.getToken();

        sRefresh.setOnRefreshListener(this);

        if (login.getProfile().startsWith("DRI")) {
            y = calendar.get(Calendar.YEAR);
            m = calendar.get(Calendar.MONTH);
            d = calendar.get(Calendar.DAY_OF_MONTH);
            btnCalendar.setText(sdf.format(new Date()));
        } else {
            Bundle bundle = getArguments();
            y = bundle.getInt("y");
            m = bundle.getInt("m");
            d = bundle.getInt("d");
            btnCalendar.setText(((d < 10 ? new Integer("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y));
        }

        datos = new ArrayList<>();

        rvList.setHasFixedSize(true);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvList.setAdapter(new TransactionListAdapter(datos, getContext()));

        btnSale.setVisibility(login.getProfile().startsWith("SUP") ? View.VISIBLE : View.GONE);

        return view;
    }

    @Override
    public void onRefresh() {
        getData();
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
                if(datos!=null && datos.size()>0){
                    Double total = 0.0;
                    Double pagado = 0.0;
                    Double no_pagado = 0.0;
                    int x = 0;
                    Log.e("datos: ", String.valueOf(datos));
                    Log.e("datos: ", "text");
                    for (TransactionList transaction: datos) {
                        if(x==0){
                            report.append("Vendedor: " + transaction.getDriver() + "\n");
                            report.append("Fecha: " + transaction.getInvoice() + "\n");
                        }
                        report.append(transaction.getStore() + "\n");
                        report.append("$" + transaction.getSale() + "\n");

                        if(transaction.getState().equals("APR")) pagado += transaction.getSale();
                        else if(transaction.getState().equals("NP")) no_pagado += transaction.getSale();

                        if(!transaction.getState().equals("CAN")) total += transaction.getSale();
                        x++;
                    }
                    report.append("Pagado: $" + pagado + "\n");
                    report.append("No pagado: $" + no_pagado + "\n");
                    report.append("Venta Total: $" + total + "\n");

                    Intent intent = new Intent(getContext(), PrintTicketActivity.class);
                    intent.putExtra("diario", "reporte");
                    intent.putExtra("ticket", report.toString());
                    startActivity(intent);
                    report.setLength(0);
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
                getData();
            }
        }, y, m, d);
    }

    public void getData() {
        GetSaleByDriParam param = new GetSaleByDriParam();
        if (login.getProfile().startsWith("DRI")) {
            param.setUid(login.getId().intValue());
        } else {
            Bundle bundle = getArguments();
            Long uid = bundle.getLong("uid");
            param.setUid(uid.intValue());
        }
        param.setToken(new BigInteger(token));
        param.setProfile(login.getProfile());
        param.setDate(((d < 10 ? new Integer("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y));

        final SweetAlertDialog progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progress.getProgressHelper().setBarColor(getContext().getResources().getColor(R.color.colorGreen));
        progress.setTitleText("Espere")
                .setContentText("Descargando transacciones")
                .setCancelable(false);
        progress.show();

        Call<TransactionListResult> call = Utils.buildRetrofit(getContext()).create(WSInterface.class).getTransactions(param);
        call.enqueue(new Callback<TransactionListResult>() {
            @Override
            public void onResponse(Call<TransactionListResult> call, Response<TransactionListResult> response) {
                datos.clear();

                TransactionListResult result = response.body();
                if (result.getStatus() == 0) {
                    Log.i("Logistikapp", "ESTATUS 0");

                    if (result.getTransactions().size() == 0) {
                        cvNoData.setVisibility(View.VISIBLE);
                    } else {
                        cvNoData.setVisibility(View.GONE);
                    }

                    datos.addAll(result.getTransactions());
                    rvList.getAdapter().notifyDataSetChanged();
                    sRefresh.setRefreshing(false);

                } else {
                    Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
                }

                progress.dismiss();
            }

            @Override
            public void onFailure(Call<TransactionListResult> call, Throwable t) {
                t.printStackTrace();
                cvNoData.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "No se pudo obtener la lista de transacciones", Toast.LENGTH_SHORT).show();
                rvList.getAdapter().notifyDataSetChanged();
                sRefresh.setRefreshing(false);
                progress.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

}
