package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Travel;
import app.logistikappbeta.com.logistikapp.Params.GetHistoricParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.ReporteHistoricoActivity;
import app.logistikappbeta.com.logistikapp.Results.TravelStoresResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by danie on 21/07/2016.
 */
public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.VHHistorico> {

  private ArrayList<Travel> list;
  private View view;
  private boolean isDRI;
  private Long userId;
  private String token;
  private Context context;

  public HistoricoAdapter(ArrayList<Travel> list, boolean isDRI, Long userId, String token) {
    this.list = list;
    this.isDRI = isDRI;
    this.userId = userId;
    this.token = token;
  }

  @Override
  public VHHistorico onCreateViewHolder(ViewGroup parent, int viewType) {
    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_historico, parent, false);
    this.context = view.getContext();
    return new VHHistorico(view);
  }

  @Override
  public void onBindViewHolder(VHHistorico holder, int position) {

    Travel travel = this.list.get(position);
    Log.d("Logistikapp", "Travel: " + travel.toString());
    holder.tvIdTravel.setText("" + travel.getId());
    holder.tvNombre.setText(travel.getName());
    if (travel.getStatus().equals("CAN")) {
      holder.tvEstatus.setText("CANCELADO");
      holder.tvEstatus.setTextColor(context.getResources().getColor(R.color.colorRed));
    }

    SimpleDateFormat sdfIn = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM HH:mm");

    try {
      holder.tvInicio.setText(sdfOut.format(sdfIn.parse(travel.getStarted())));
      holder.tvTermino.setText(sdfOut.format(sdfIn.parse(travel.getFinished())));
    } catch (Exception ex) {
      Log.e("Logistikapp", "Error parsear fechas\n" + ex.getMessage());
    }

    if (isDRI) {
      holder.tvResponsable.setVisibility(View.GONE);
    } else {
      holder.tvResponsable.setText(travel.getDriver());
    }

    holder.tvNoClientes.setText("" + travel.getStores());
    holder.tvNoChecks.setText("" + travel.getCheckin());
    DecimalFormat df = new DecimalFormat("0.00");
    holder.tvAvance.setText(df.format(travel.getProgress()) + "%");

    if (travel.getProgress() < 79.99) {
      holder.imgAvancce.setImageResource(R.drawable.ic_sentiment_dissatisfied);
      holder.imgAvancce.setColorFilter(context.getResources().getColor(R.color.colorRed));
      holder.tvAvance.setTextColor(context.getResources().getColor(R.color.colorRed));
    } else if (travel.getProgress() > 79.99 && travel.getProgress() < 89.99) {
      holder.imgAvancce.setImageResource(R.drawable.ic_sentiment_neutral);
      holder.imgAvancce.setColorFilter(context.getResources().getColor(R.color.colorOrange));
      holder.tvAvance.setTextColor(context.getResources().getColor(R.color.colorOrange));
    } else {
      holder.imgAvancce.setImageResource(R.drawable.ic_sentiment_satisfied);
      holder.imgAvancce.setColorFilter(context.getResources().getColor(R.color.colorBlue));
      holder.tvAvance.setTextColor(context.getResources().getColor(R.color.colorBlue));
    }

  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  protected class VHHistorico extends RecyclerView.ViewHolder {

    @BindView(R.id.tvId)
    TextView tvIdTravel;
    @BindView(R.id.tvNombre)
    TextView tvNombre;
    @BindView(R.id.tvEstatus)
    TextView tvEstatus;
    @BindView(R.id.tvInicio)
    TextView tvInicio;
    @BindView(R.id.tvTermino)
    TextView tvTermino;
    @BindView(R.id.tvResponsable)
    TextView tvResponsable;
    @BindView(R.id.tvNoClientes)
    TextView tvNoClientes;
    @BindView(R.id.tvNoChecks)
    TextView tvNoChecks;
    @BindView(R.id.tvAvance)
    TextView tvAvance;
    @BindView(R.id.imgAvance)
    ImageView imgAvancce;

    public VHHistorico(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.ivBtn)
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.ivBtn: {

          final SweetAlertDialog progress = new SweetAlertDialog(view.getContext(), SweetAlertDialog.PROGRESS_TYPE);
          progress.getProgressHelper().setBarColor(view.getContext().getResources().getColor(R.color.colorGreen));
          progress.setTitleText("Espere")
              .setContentText("Obteniendo información")
              .setCancelable(false);
          progress.show();

          GetHistoricParam param = new GetHistoricParam();
          param.setUid(userId);
          param.setToken(token);
          param.setTid(list.get(getLayoutPosition()).getId());

          Call<TravelStoresResult> call = Utils.buildRetrofit(v.getContext()).create(WSInterface.class).getTravelStoresHistorico(param);
          call.enqueue(new Callback<TravelStoresResult>() {
            @Override
            public void onResponse(Call<TravelStoresResult> call, Response<TravelStoresResult> response) {
              TravelStoresResult result = response.body();
              if (result.getStatus() == 0) {
                Intent intent = new Intent(context, ReporteHistoricoActivity.class);
                Gson gson = new Gson();
                intent.putExtra("travel", gson.toJson(result.getTravel()));
                intent.putExtra("stores", gson.toJson(result.getStores()));
                context.startActivity(intent);
              } else {
                Toast.makeText(context, "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
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
        break;
      }
    }
  }

}
