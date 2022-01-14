package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Fragments.ViajesFragment;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Travel;
import app.logistikappbeta.com.logistikapp.Params.GetTravelStoresParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.TravelStoresResult;
import app.logistikappbeta.com.logistikapp.TravelOnWayActivity;
import app.logistikappbeta.com.logistikapp.TravelWayBillActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by danie on 19/07/2016.
 */
public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.VHTravel> {

  private ArrayList<Travel> list;
  private boolean isDRI;
  private View view;
  private Long userId;
  private String token;
  ViajesFragment context;

  private Intent intent;

  public TravelAdapter(ArrayList<Travel> list, boolean isDRI, Long userId, String token, ViajesFragment context) {
    this.list = list;
    this.isDRI = isDRI;
    this.userId = userId;
    this.token = token;
    this.context = context;
  }

  @Override
  public VHTravel onCreateViewHolder(ViewGroup parent, int viewType) {
    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_viajes, parent, false);
    return new VHTravel(view);
  }

  @Override
  public void onBindViewHolder(VHTravel holder, int position) {
    Travel travel = this.list.get(position);
    holder.tvIdTravel.setText("" + travel.getId());
    holder.tvNombre.setText("(" + (position + 1) + ") " + travel.getName());
    holder.tvResponsable.setText(travel.getDriver());
    holder.tvNoClientes.setText("" + travel.getStores() + (travel.getStores() != 1 ? " Clientes" : " Cliente"));
    if (travel.getStatus().equals("TRA")) {
      holder.tvEstatus.setText("EN TRÁNSITO");
      holder.tvEstatus.setTextColor(view.getContext().getResources().getColor(R.color.colorBlue));
      holder.lyProgramado.setVisibility(View.GONE);
      holder.lyIniciado.setVisibility(View.VISIBLE);
      holder.imgChecks.setVisibility(View.VISIBLE);
      holder.tvNoChecks.setVisibility(View.VISIBLE);
      holder.imgAvancce.setVisibility(View.VISIBLE);
      holder.tvAvance.setVisibility(View.VISIBLE);
      holder.imgNoImg.setVisibility(View.VISIBLE);
      holder.tvNoImg.setVisibility(View.VISIBLE);
      holder.imgNoComentarios.setVisibility(View.VISIBLE);
      holder.tvNoComentarios.setVisibility(View.VISIBLE);
      holder.tvProgramadoI.setText(travel.getScheduled());

      holder.tvNoChecks.setText("" + travel.getCheckin() + (travel.getCheckin() != 1 ? " Checkins" : " Checkin"));

      SimpleDateFormat sdfIn = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM HH:mm");
      try {
        holder.tvIniciadoI.setText(sdfOut.format(sdfIn.parse(travel.getStarted())));
      } catch (Exception ex) {
        Log.e("Logistikap", "Error parsear fecha inicio\n" + ex.getMessage());
        holder.tvIniciadoI.setText("00/00 00:00");
      }

      DecimalFormat df = new DecimalFormat("0.00");
      holder.tvAvance.setText(df.format(travel.getProgress()) + "%");

      if (travel.getProgress() < 79.99) {
        holder.imgAvancce.setImageResource(R.drawable.ic_sentiment_dissatisfied);
        holder.imgAvancce.setColorFilter(view.getResources().getColor(R.color.colorRed));
        holder.tvAvance.setTextColor(view.getResources().getColor(R.color.colorRed));
      } else if (travel.getProgress() > 79.99 && travel.getProgress() < 89.99) {
        holder.imgAvancce.setImageResource(R.drawable.ic_sentiment_neutral);
        holder.imgAvancce.setColorFilter(view.getResources().getColor(R.color.colorOrange));
        holder.tvAvance.setTextColor(view.getResources().getColor(R.color.colorOrange));
      } else {
        holder.imgAvancce.setImageResource(R.drawable.ic_sentiment_satisfied);
        holder.imgAvancce.setColorFilter(view.getResources().getColor(R.color.colorGreen));
        holder.tvAvance.setTextColor(view.getResources().getColor(R.color.colorGreen));
      }

      if (travel.getComments() > 0) {
        holder.imgNoComentarios.setColorFilter(view.getResources().getColor(R.color.colorBlue));
        holder.tvNoComentarios.setText("" + travel.getComments());
        holder.tvNoComentarios.setTextColor(Color.BLACK);
      }

      if (travel.getImages() > 0) {
        holder.imgNoImg.setColorFilter(view.getResources().getColor(R.color.colorBlue));
        holder.tvNoImg.setText("" + travel.getImages());
        holder.tvNoImg.setTextColor(Color.BLACK);
      }

    } else {
      holder.tvEstatus.setText("PROGRAMADO");
      holder.tvEstatus.setTextColor(view.getContext().getResources().getColor(R.color.colorOrange));
      holder.lyIniciado.setVisibility(View.GONE);
      holder.tvNoChecks.setVisibility(View.GONE);
      holder.tvAvance.setVisibility(View.GONE);
      holder.tvNoImg.setVisibility(View.GONE);
      holder.tvNoComentarios.setVisibility(View.GONE);
      holder.tvProgramado.setText(travel.getScheduled());
      holder.imgAvancce.setVisibility(View.GONE);
      holder.imgNoImg.setVisibility(View.GONE);
      holder.imgNoComentarios.setVisibility(View.GONE);
      holder.imgChecks.setVisibility(View.GONE);
    }
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  protected class VHTravel extends RecyclerView.ViewHolder {

    @BindView(R.id.tvIdTravel)
    TextView tvIdTravel;

    @BindView(R.id.lyIniciado)
    LinearLayout lyIniciado;
    @BindView(R.id.lyProgamado)
    LinearLayout lyProgramado;
    @BindView(R.id.tvNombre)
    TextView tvNombre;
    @BindView(R.id.tvEstatus)
    TextView tvEstatus;
    @BindView(R.id.tvProgramado)
    TextView tvProgramado;
    @BindView(R.id.tvProgamadoI)
    TextView tvProgramadoI;
    @BindView(R.id.tvIniciadoI)
    TextView tvIniciadoI;
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
    @BindView(R.id.tvNoImg)
    TextView tvNoImg;
    @BindView(R.id.imgNoImg)
    ImageView imgNoImg;
    @BindView(R.id.tvNoComentarios)
    TextView tvNoComentarios;
    @BindView(R.id.imgNoComentarios)
    ImageView imgNoComentarios;
    @BindView(R.id.imgChecks)
    ImageView imgChecks;

    Gson gson;

    public VHTravel(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      tvResponsable.setVisibility(isDRI ? View.GONE : View.VISIBLE);
      gson = new Gson();
    }

    @OnClick(R.id.ivBtn)
    public void onClikc(View v) {
      switch (v.getId()) {
        case R.id.ivBtn: {
          GetTravelStoresParam param = new GetTravelStoresParam();
          param.setTid(list.get(getLayoutPosition()).getId());
          param.setToken(new BigInteger(token));
          param.setUid(userId);

          Log.d("Estatus", list.get(this.getLayoutPosition()).getStatus());

          if (list.get(this.getLayoutPosition()).getStatus().equals("TRA")) {
            intent = new Intent(v.getContext(), TravelOnWayActivity.class);
            String params = gson.toJson(param);
            Log.i("PARAMS", params);
            intent.putExtra("params", params);
            context.getActivity().startActivityForResult(intent, 1);
          } else {
            intent = new Intent(v.getContext(), TravelWayBillActivity.class);


            final SweetAlertDialog progress = new SweetAlertDialog(view.getContext(), SweetAlertDialog.PROGRESS_TYPE);
            progress.getProgressHelper().setBarColor(view.getContext().getResources().getColor(R.color.colorGreen));
            progress.setTitleText("Espere")
                .setContentText("Obteniendo información del viaje")
                .setCancelable(false);
            progress.show();

            Call<TravelStoresResult> call = Utils.buildRetrofit(v.getContext()).create(WSInterface.class).getTravelStores(param);
            call.enqueue(new Callback<TravelStoresResult>() {
              @Override
              public void onResponse(Call<TravelStoresResult> call, Response<TravelStoresResult> response) {
                TravelStoresResult result = response.body();
                if (result.getStatus() == 0) {

                  Log.d("STORES", gson.toJson(result.getStores()));
                  Log.d("TRAVEL", gson.toJson(result.getTravel()));

                  intent.putExtra("travel", gson.toJson(result.getTravel()));
                  intent.putExtra("stores", gson.toJson(result.getStores()));

                  context.getActivity().startActivityForResult(intent, 1);
                } else {
                  Toast.makeText(context.getContext(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
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
        }
        break;
      }
    }

  }

}
