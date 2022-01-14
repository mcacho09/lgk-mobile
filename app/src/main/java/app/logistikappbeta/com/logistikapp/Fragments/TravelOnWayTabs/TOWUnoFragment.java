package app.logistikappbeta.com.logistikapp.Fragments.TravelOnWayTabs;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Travel;
import app.logistikappbeta.com.logistikapp.Params.TravelEndParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TOWUnoFragment extends Fragment {

  @BindView(R.id.tvNombre)
  TextView tvNombre;
  @BindView(R.id.tvAsignado)
  TextView tvAsignado;
  @BindView(R.id.tvProgramado)
  TextView tvProgramado;
  @BindView(R.id.tvIniciado)
  TextView tvIniciado;
  @BindView(R.id.tvCheckins)
  TextView tvCheckins;
  @BindView(R.id.tvClientes)
  TextView tvClientes;
  @BindView(R.id.imgAvance)
  ImageView imgAvance;
  @BindView(R.id.tvTAvance)
  TextView tvTAvance;
  @BindView(R.id.tvAvance)
  TextView tvAvance;
  @BindView(R.id.tvCheckinsok)
  TextView tvCheckinsok;
  @BindView(R.id.tvFueraRango)
  TextView tvFueraRango;
  @BindView(R.id.imgComentarios)
  ImageView imgComentarios;
  @BindView(R.id.tvTComentarios)
  TextView tvTComentarios;
  @BindView(R.id.tvComentarios)
  TextView tvComentarios;
  @BindView(R.id.imgImagenes)
  ImageView imgImagenes;
  @BindView(R.id.tvTImagenes)
  TextView tvTImagenes;
  @BindView(R.id.tvImagenes)
  TextView tvImagenes;

  Login login;
  Travel travel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_towuno, container, false);
    ButterKnife.bind(this, v);
    return v;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @OnClick({R.id.btnCancelar, R.id.btnFinalizar})
  public void OnClick(View v) {

    SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
    dialog.setTitleText("Alerta")
        .setCancelText("No")
        .setConfirmText("Si");

    switch (v.getId()) {
      case R.id.btnCancelar: {
        dialog.setContentText("¿En verdad desea cancelar el viaje actual?")
            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
              @Override
              public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
              }
            })
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
              @Override
              public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                swal.setTitleText("Exito")
                    .setContentText("Viaje cancelado")
                    .show();
                getActivity().finish();
              }
            })
            .show();
      }
      break;
      case R.id.btnFinalizar: {
        dialog.setContentText("¿En verdad desea finalizar el viaje actual?")
            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
              @Override
              public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
              }
            })
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
              @Override
              public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                TravelEndParam param = new TravelEndParam();
                param.setUid(login.getId().intValue());
                param.setToken(new BigInteger(Utils.getSaveToken(getContext())));
                param.setTid((int) travel.getId());
                param.setDate(sdf.format(new Date()));

                final SweetAlertDialog progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                progress.getProgressHelper().setBarColor(getContext().getResources().getColor(R.color.colorGreen));
                progress.setTitleText("Finalizando viaje");
                progress.setContentText("Espere por favor");
                progress.setCancelable(false);
                progress.show();

                Call<Result> call = Utils.buildRetrofit(getContext()).create(WSInterface.class).travelEnd(param);
                call.enqueue(new Callback<Result>() {
                  @Override
                  public void onResponse(Call<Result> call, Response<Result> response) {
                    Result result = response.body();

                    if (result.getStatus() == 0) {
                      SweetAlertDialog alert = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                      alert.setTitleText("Exito")
                          .setContentText("El viaje se finalizo con exito")
                          .show();
                      getActivity().finish();
                    } else {
                      SweetAlertDialog alert = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                      alert.setTitleText("Alerta")
                          .setContentText(result.getMessage())
                          .show();
                    }

                    progress.dismiss();
                  }

                  @Override
                  public void onFailure(Call<Result> call, Throwable t) {
                    t.printStackTrace();
                    progress.dismiss();
                  }
                });
              }
            })
            .show();
      }
      break;
    }

  }

  public void updateTravel(Travel travel) {
    this.travel = travel;
    login = Utils.getSessionInfo(getContext());

    tvNombre.setText(travel.getName());
    tvAsignado.setText(travel.getDriver());
    tvProgramado.setText(travel.getScheduled());

    SimpleDateFormat sdfIn = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM HH:mm");
    try {
      tvIniciado.setText(sdfOut.format(sdfIn.parse(travel.getStarted())));
    } catch (Exception ex) {
      Log.e("Logistikapp", "Error parsear fecha");
      ex.printStackTrace();
    }

    tvClientes.setText("" + travel.getStores());

    if (travel.getProgress() < 79.99) {
      imgAvance.setColorFilter(getResources().getColor(R.color.colorRed));
      imgAvance.setImageResource(R.drawable.ic_sentiment_dissatisfied);
      tvTAvance.setTextColor(getResources().getColor(R.color.colorRed));
      tvAvance.setTextColor(getResources().getColor(R.color.colorRed));
    } else if (travel.getProgress() > 79.99 && travel.getProgress() < 89.99) {
      imgAvance.setColorFilter(getResources().getColor(R.color.colorOrange));
      imgAvance.setImageResource(R.drawable.ic_sentiment_neutral);
      tvTAvance.setTextColor(getResources().getColor(R.color.colorOrange));
      tvAvance.setTextColor(getResources().getColor(R.color.colorOrange));
    } else {
      imgAvance.setColorFilter(getResources().getColor(R.color.colorBlue));
      imgAvance.setImageResource(R.drawable.ic_sentiment_satisfied);
      tvTAvance.setTextColor(getResources().getColor(R.color.colorGreen));
      tvAvance.setTextColor(getResources().getColor(R.color.colorGreen));
    }

    DecimalFormat df = new DecimalFormat("0.00");
    tvAvance.setText(df.format(travel.getProgress()) + "%");

    tvCheckins.setText("" + travel.getCheckin());
    tvCheckinsok.setText("" + travel.getCheckinok());
    tvFueraRango.setText("" + travel.getCheckinerr());

    if (travel.getComments() > 0) {
      imgComentarios.setColorFilter(getResources().getColor(R.color.colorBlue));
      tvTComentarios.setTextColor(getResources().getColor(R.color.colorBlue));
      tvComentarios.setTextColor(getResources().getColor(R.color.colorBlue));
      tvComentarios.setText("" + travel.getComments());
    }

    if (travel.getImages() > 0) {
      imgImagenes.setColorFilter(getResources().getColor(R.color.colorBlue));
      tvTImagenes.setTextColor(getResources().getColor(R.color.colorBlue));
      tvImagenes.setTextColor(getResources().getColor(R.color.colorBlue));
      tvImagenes.setText("" + travel.getImages());
    }
  }

}
