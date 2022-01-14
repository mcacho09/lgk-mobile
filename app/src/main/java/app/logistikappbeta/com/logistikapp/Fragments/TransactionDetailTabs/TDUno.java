package app.logistikappbeta.com.logistikapp.Fragments.TransactionDetailTabs;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.text.DecimalFormat;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Transaction;
import app.logistikappbeta.com.logistikapp.Params.UpdOrdenStatusParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TDUno extends Fragment {

  private static final String TAG = "LOGISTIKAPP";

  @BindView(R.id.tvDate)
  TextView tvDate;
  @BindView(R.id.tvStore)
  TextView tvStore;
  @BindView(R.id.tvDri)
  TextView tvDri;
  @BindView(R.id.tvSale)
  TextView tvSale;
  @BindView(R.id.imgChange)
  ImageView imgChange;
  @BindView(R.id.tvTChange)
  TextView tvTChange;
  @BindView(R.id.tvChange)
  TextView tvChange;
  @BindView(R.id.imgDev)
  ImageView imgDev;
  @BindView(R.id.tvTDev)
  TextView tvTDev;
  @BindView(R.id.tvDev)
  TextView tvDev;
  @BindView(R.id.tvNoTrx)
  TextView tvNoTrx;
  @BindView(R.id.imgApr)
  ImageView imgApr;
  @BindView(R.id.tvTApr)
  TextView tvTApr;
  @BindView(R.id.tvApr)
  TextView tvApr;

  Gson gson;
  Transaction trx;
  int indexStatus = 0;

  private Login login;
  private String token;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_tduno, container, false);
    ButterKnife.bind(this, v);

    Bundle bundle = getArguments();
    String transaction = bundle.getString("transaction");
    Double price_sale = bundle.getDouble("sale");
    gson = new Gson();
    trx = gson.fromJson(transaction, Transaction.class);

    login = Utils.getSessionInfo(getContext());
    token = Utils.getSaveToken(getContext());

    tvDate.setText(trx.getInvoice());
    tvStore.setText(trx.getStore());
    tvDri.setText(trx.getDriver());
    DecimalFormat df = new DecimalFormat("0.00");
    tvSale.setText(df.format(price_sale));

    if (trx.getChanges() > 0) {
      imgChange.setColorFilter(getResources().getColor(R.color.colorOrange), PorterDuff.Mode.SRC_IN);
      tvTChange.setTextColor(getResources().getColor(R.color.colorOrange));
      tvChange.setTextColor(getResources().getColor(R.color.colorOrange));
      tvChange.setText("" + trx.getChanges());
    } else {
      imgChange.setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_IN);
      tvTChange.setTextColor(getResources().getColor(R.color.colorGray));
      tvChange.setTextColor(getResources().getColor(R.color.colorGray));
      tvChange.setText("0");
    }

    if (trx.getDevolutions() > 0) {
      imgDev.setColorFilter(getResources().getColor(R.color.colorOrange), PorterDuff.Mode.SRC_IN);
      tvTDev.setTextColor(getResources().getColor(R.color.colorOrange));
      tvDev.setTextColor(getResources().getColor(R.color.colorOrange));
      tvDev.setText("" + trx.getDevolutions());
    } else {
      imgDev.setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_IN);
      tvTDev.setTextColor(getResources().getColor(R.color.colorGray));
      tvDev.setTextColor(getResources().getColor(R.color.colorGray));
      tvDev.setText("0");
    }

    tvNoTrx.setText("" + trx.getTrxnumber());

    setStatusConfig(trx.getState());

    String[] status = getResources().getStringArray(R.array.order_status_val_detail);
    int size = status.length;
    for (int i = 0; i < size; i++) {
      if (status[i].contentEquals(trx.getState())) {
        indexStatus = i;
        break;
      }
    }

    Log.d("Transaction" , " - "+trx);
    return v;
  }

  //public void getData() {
  //  List<ProductsAlmacenInfo> list = this.mAlmacenAndProducts.getProducts();
  //}

  @OnClick({R.id.cvChgStatus})
  public void click(View v) {
    switch (v.getId()) {
      case R.id.cvChgStatus: {
        final String[] options = getResources().getStringArray(R.array.order_status_detail);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Estado").setSingleChoiceItems(options, indexStatus, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            String status = getResources().getStringArray(R.array.order_status_val_detail)[i];
            indexStatus = i;
            updOrden(status);
            dialogInterface.dismiss();
          }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
          }
        }).create().show();
      }
      break;
    }
  }

  public String lastState;
  public void updOrden(String status) {
    lastState = getResources().getStringArray(R.array.order_status_val_detail)[indexStatus];
    UpdOrdenStatusParam param = new UpdOrdenStatusParam();
    param.setToken(new BigInteger(Utils.getSaveToken(getContext())));
    param.setUid(Utils.getSessionInfo(getContext()).getId());
    param.setOid((long) trx.getId());
    param.setStatus(status);

    final SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
    dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
    dialog.setTitleText("Espere")
        .setContentText("Actualizando estado")
        .setCancelable(false);
    dialog.show();

    Call<Result> call = Utils.buildRetrofit(getContext()).create(WSInterface.class)
        .updateOrdenStatus(param);
    call.enqueue(new Callback<Result>() {
      @Override
      public void onResponse(Call<Result> call, Response<Result> response) {
        Result result = response.body();
        dialog.dismiss();

        if (result.getStatus() == 0) {
          dialog.setTitleText("Éxito")
              .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
          dialog.setContentText("Actualizó la orden \ncon éxito");
          setStatusConfig(getResources().getStringArray(R.array.order_status_val_detail)[indexStatus]);
        } else {
          dialog.setTitleText("Error")
              .changeAlertType(SweetAlertDialog.ERROR_TYPE);
          dialog.setContentText("Error al actualizar el cliente\nIntente más tarde");
          Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
        }
        dialog.setConfirmText("Aceptar")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
              @Override
              public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                getActivity().onBackPressed();
              }
            }).show();

      }

      @Override
      public void onFailure(Call<Result> call, Throwable t) {
        Log.e(TAG, "onFailure TDUno", t);
        Toast.makeText(getContext(), "Verifique su conexión a internet", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void setStatusConfig(String status) {
    switch (status) {
      case "APR": {
        imgApr.setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_IN);
        imgApr.setImageResource(R.drawable.ic_thumb_up);
        tvTApr.setTextColor(getResources().getColor(R.color.colorBlue));
        tvApr.setText("Pagada");
      }
      break;
      case "NP": {
        imgApr.setColorFilter(getResources().getColor(R.color.colorYellow), PorterDuff.Mode.SRC_IN);
        imgApr.setImageResource(R.drawable.ic_thumb_up);
        tvTApr.setTextColor(getResources().getColor(R.color.colorYellow));
        tvApr.setText("No Pagada");
      }
      break;
      case "CAN": {
        imgApr.setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_IN);
        imgApr.setImageResource(R.drawable.ic_thumb_down);
        tvTApr.setTextColor(getResources().getColor(R.color.colorRed));
        tvApr.setText("Denegada");
      }
      break;
    }
  }

}
