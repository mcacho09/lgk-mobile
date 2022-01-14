package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.TransactionList;
import app.logistikappbeta.com.logistikapp.Params.GetTransactionParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.TransactionResult;
import app.logistikappbeta.com.logistikapp.TransactionDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by danie on 09/08/2016.
 */
public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.VHTransactionList> {

  private ArrayList<TransactionList> list;
  private Context context;
  private static TransactionResult transaction;

  public TransactionListAdapter(ArrayList<TransactionList> list, Context context) {
    this.list = list;
    this.context = context;
  }

  public static TransactionResult getTransaction() {
    return transaction;
  }

  @Override
  public VHTransactionList onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_transaction_list, parent, false);
    return new VHTransactionList(v);
  }

  @Override
  public void onBindViewHolder(VHTransactionList holder, int position) {
    holder.setData(list.get(position));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  protected class VHTransactionList extends RecyclerView.ViewHolder {

    DecimalFormat df = new DecimalFormat("0.00");

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvNombre)
    TextView tvNombre;
    @BindView(R.id.tvVenta)
    TextView tvVenta;
    @BindView(R.id.imgDev)
    ImageView imgDev;
    @BindView(R.id.tvDev)
    TextView tvDev;
    @BindView(R.id.imgChange)
    ImageView imgChange;
    @BindView(R.id.tvChange)
    TextView tvChange;
    @BindView(R.id.imgState)
    ImageView imgState;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvDri)
    TextView tvDri;

    public VHTransactionList(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void setData(TransactionList t) {

      tvDate.setText(t.getInvoice());
      tvNombre.setText(t.getStore());

      tvVenta.setText(df.format(t.getSale()));

      if (t.getDevolutions() == 0) {
        imgDev.setColorFilter(context.getResources().getColor(R.color.colorGray));
        tvDev.setTextColor(context.getResources().getColor(R.color.colorGray));
        tvDev.setText("0");
      } else {
        imgDev.setColorFilter(context.getResources().getColor(R.color.colorOrange));
        tvDev.setTextColor(context.getResources().getColor(R.color.colorOrange));
        tvDev.setText("" + t.getDevolutions());
      }

      if (t.getChanges() == 0) {
        imgChange.setColorFilter(context.getResources().getColor(R.color.colorGray));
        tvChange.setTextColor(context.getResources().getColor(R.color.colorGray));
        tvChange.setText("0");
      } else {
        imgChange.setColorFilter(context.getResources().getColor(R.color.colorOrange));
        tvChange.setTextColor(context.getResources().getColor(R.color.colorOrange));
        tvChange.setText("" + t.getChanges());
      }

      if (t.getState().contentEquals("APR")) {
        imgState.setColorFilter(context.getResources().getColor(R.color.colorBlue));
        imgState.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_thumb_up));
        tvState.setTextColor(context.getResources().getColor(R.color.colorBlue));
        tvState.setText("Pagada");
      } else if (t.getState().contentEquals("NP")) {
        imgState.setColorFilter(context.getResources().getColor(R.color.colorYellow));
        imgState.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_thumb_up));
        tvState.setTextColor(context.getResources().getColor(R.color.colorYellow));
        tvState.setText("No pagada");
      } else if (t.getState().contentEquals("CAN")) {
        imgState.setColorFilter(context.getResources().getColor(R.color.colorRed));
        imgState.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_thumb_down));
        tvState.setTextColor(context.getResources().getColor(R.color.colorRed));
        tvState.setText("Cancelada");
      }

      tvDri.setText(t.getDriver());
    }

    private void returnVtaProducts(Gson gson, TransactionResult result) {
        String transaction = gson.toJson(result.getTransaction());
        Log.d("TRANSACCION", transaction);
        String vtalist = gson.toJson(result.getVtalist());
        result.setVtalist(null);
        vtalist = "";
        String devlist = gson.toJson(result.getDevlist());
        String chglist = gson.toJson(result.getChglist());
        Intent intent = new Intent(context, TransactionDetailActivity.class);
        intent.putExtra("total_price", "0.0");
        intent.putExtra("transaction", transaction);
        intent.putExtra("vtalist", vtalist);
        intent.putExtra("devlist", devlist);
        intent.putExtra("chglist", chglist);
        context.startActivity(intent);
    }


    @OnClick({R.id.ivBtn})
    public void OnClick(View v) {
      Login login = Utils.getSessionInfo(context);
      String token = Utils.getSaveToken(context);
      TransactionList transactionList = list.get(getLayoutPosition());

      GetTransactionParam param = new GetTransactionParam();
      param.setToken(new BigInteger(token));
      param.setTid((long) transactionList.getId());
      param.setUid(login.getId());

      final SweetAlertDialog progress = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
      progress.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorGreen));
      progress.setTitleText("Espere")
              .setContentText("Obteniendo información de la transacción")
              .setCancelable(false);
      progress.show();

      Call<TransactionResult> call = Utils.buildRetrofit(context).create(WSInterface.class).getTransaction(param);
      call.enqueue(new Callback<TransactionResult>() {
        @Override
        public void onResponse(Call<TransactionResult> call, Response<TransactionResult> response) {
          progress.dismiss();

          TransactionResult result = transaction = response.body();

          Gson gson = new Gson();

          if (result.getStatus() == 0) {
            if(tvState.getText().toString().equals("Cancelada")){
              returnVtaProducts(gson, result);
            }else {
              String transaction = gson.toJson(result.getTransaction());
              Log.d("TRANSACCION", transaction);
              String vtalist = gson.toJson(result.getVtalist());
              String devlist = gson.toJson(result.getDevlist());
              String chglist = gson.toJson(result.getChglist());
              Intent intent = new Intent(context, TransactionDetailActivity.class);
              intent.putExtra("total_price", "99");
              intent.putExtra("transaction", transaction);
              intent.putExtra("vtalist", vtalist);
              intent.putExtra("devlist", devlist);
              intent.putExtra("chglist", chglist);
              context.startActivity(intent);
            }
          } else {
            Toast.makeText(context, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
          }
        }

        @Override
        public void onFailure(Call<TransactionResult> call, Throwable t) {
          progress.dismiss();
          progress.changeAlertType(SweetAlertDialog.WARNING_TYPE);
          progress.setTitleText("Error")
                  .setContentText("No se pudo obtener información de la transacción\nIntente más tarde")
                  .show();
          t.printStackTrace();
        }
      });
    }
  }
}
