package app.logistikappbeta.com.logistikapp.Fragments.TravelWayBillTabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Travel;
import app.logistikappbeta.com.logistikapp.Params.GetTravelStoresParam;
import app.logistikappbeta.com.logistikapp.Params.TravelStartParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.Result;
import app.logistikappbeta.com.logistikapp.TravelOnWayActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TWBUnoFragment extends Fragment {

  @BindView(R.id.tvNombre)
  TextView tvNombre;
  @BindView(R.id.tvAsignado)
  TextView tvAsignado;
  @BindView(R.id.tvProgramado)
  TextView tvProgramado;
  @BindView(R.id.tvClientes)
  TextView tvClientes;
  @BindView(R.id.tvKms)
  TextView tvKms;
  @BindView(R.id.tvTiempo)
  TextView tvTiempo;

  Travel travel;
  Login login;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_twbuno, container, false);
    ButterKnife.bind(this, v);

    login = Utils.getSessionInfo(getContext());

    String content = getArguments().getString("travel");
    travel = new Gson().fromJson(content, Travel.class);
    Log.d("Logistikapp", "---: " + travel.toString());
    tvNombre.setText(travel.getName());
    tvAsignado.setText(travel.getDriver());
    tvProgramado.setText(travel.getScheduled());
    tvClientes.setText("" + travel.getStores());

    DecimalFormat df = new DecimalFormat("0.00");
    tvKms.setText("" + df.format((travel.getKm() / 1000)) + " KM");

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date((long) travel.getTime());
    tvTiempo.setText("" + sdf.format(date) + " HRS");

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

  @OnClick({R.id.btnCancelar, R.id.btnIniciar})
  public void OnClick(View view) {
    switch (view.getId()) {
      case R.id.btnIniciar: {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        TravelStartParam param = new TravelStartParam();
        param.setUid(login.getId().intValue());
        param.setToken(new BigInteger(Utils.getSaveToken(getContext())));
        param.setTid((int) travel.getId());
        param.setDate(sdf.format(new Date()));

        final SweetAlertDialog progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progress.getProgressHelper().setBarColor(getContext().getResources().getColor(R.color.colorGreen));
        progress.setTitleText("Espere")
            .setContentText("Iniciando viaje")
            .setCancelable(false);
        progress.show();

        Call<Result> call = Utils.buildRetrofit(getContext()).create(WSInterface.class).travelStart(param);
        call.enqueue(new Callback<Result>() {
          @Override
          public void onResponse(Call<Result> call, Response<Result> response) {
            Result result = response.body();
            if (result.getStatus() == 0) {
              Intent intent = new Intent(getContext(), TravelOnWayActivity.class);
              intent.putExtra("travel", getArguments().getString("travel"));
              intent.putExtra("stores", getArguments().getString("stores"));
              GetTravelStoresParam param = new GetTravelStoresParam();
              param.setTid(travel.getId());
              param.setToken(new BigInteger(Utils.getSaveToken(getContext())));
              param.setUid(login.getId());
              intent.putExtra("params", new Gson().toJson(param));
              startActivity(intent);
              getActivity().finish();
            } else {
              SweetAlertDialog alert = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
              alert.setTitleText("Alerta")
                  .setContentText("Error: " + result.getMessage() + "\nCodigo: " + result.getStatus())
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
      break;
      case R.id.btnCancelar: {
        getActivity().finish();
      }
      break;
    }
  }
}
