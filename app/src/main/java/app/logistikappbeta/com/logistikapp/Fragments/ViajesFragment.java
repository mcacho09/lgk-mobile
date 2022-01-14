package app.logistikappbeta.com.logistikapp.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

import app.logistikappbeta.com.logistikapp.Adapters.TravelAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Travel;
import app.logistikappbeta.com.logistikapp.Params.GetTravelParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.TravelResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViajesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  private static final String TAG = "Logistikapp";
  int d, m, y;
  final Calendar calendar = Calendar.getInstance();

  @BindView(R.id.btnCalendar)
  Button btnCalendar;
  @BindView(R.id.sRefresh)
  SwipeRefreshLayout sRefresh;
  @BindView(R.id.rvList)
  RecyclerView rvList;
  @BindView(R.id.cbNoData)
  CardView cvNoData;

  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  ArrayList<Travel> datos;
  Login login;
  String token;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_viajes, container, false);
    ButterKnife.bind(this, v);

    login = Utils.getSessionInfo(getContext());
    token = Utils.getToken();

    Bundle bundle = getArguments();
    if (bundle != null && bundle.containsKey("y")) {
      y = bundle.getInt("y");
      m = bundle.getInt("m");
      d = bundle.getInt("d");
      btnCalendar.setText(((d < 10 ? new Integer("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y));
    } else {
      y = calendar.get(Calendar.YEAR);
      m = calendar.get(Calendar.MONTH);
      d = calendar.get(Calendar.DAY_OF_MONTH);
      btnCalendar.setText(sdf.format(new Date()));

    }

    sRefresh.setOnRefreshListener(this);
    sRefresh.setRefreshing(true);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setHasFixedSize(true);
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    datos = new ArrayList<>();
    rvList.setAdapter(new TravelAdapter(datos, login.getProfile().startsWith("DRI"), login.getId(), token, this));

    return v;
  }

  @OnClick({R.id.btnCalendar})
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnCalendar: {
        onCreateDialog(0).show();
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

        getData(((d < 10 ? new Integer("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y));
      }
    }, y, m, d);
  }

  @Override
  public void onRefresh() {
    getData(((d < 10 ? new Integer("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y));
  }

  public void getData(final String date) {
    if (Utils.isOnline(getContext())) {
      sRefresh.setRefreshing(true);
      cvNoData.setVisibility(View.GONE);
      datos.clear();
      rvList.getAdapter().notifyDataSetChanged();

      GetTravelParam param = new GetTravelParam();
      param.setUid(login.getId().intValue());
      param.setToken(new BigInteger(token));
      param.setProfile(login.getProfile());
      param.setDate(date);

      Call<TravelResult> call = Utils.buildRetrofit(getContext()).create(WSInterface.class).getTravel(param);
      call.enqueue(new Callback<TravelResult>() {
        @Override
        public void onResponse(Call<TravelResult> call, Response<TravelResult> response) {
          if (response.isSuccessful()) {
            TravelResult result = response.body();
            if (result.getStatus() == 0) {
              if (result.getTravels().size() == 0) {
                cvNoData.setVisibility(View.VISIBLE);
              } else {
                cvNoData.setVisibility(View.GONE);
              }

              datos.addAll(result.getTravels());

              rvList.getAdapter().notifyDataSetChanged();

              rvList.scrollToPosition(0);

              sRefresh.setRefreshing(false);
            } else {
              Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
            }
          } else {
            sRefresh.setRefreshing(false);
            Toast.makeText(getContext(), "Sin acceso a internet", Toast.LENGTH_SHORT).show();
            cvNoData.setVisibility(View.VISIBLE);
          }
        }

        @Override
        public void onFailure(Call<TravelResult> call, Throwable t) {
          t.printStackTrace();
          cvNoData.setVisibility(View.VISIBLE);
          sRefresh.setRefreshing(false);
        }
      });
    } else {
      sRefresh.setRefreshing(false);
      Toast.makeText(getContext(), "Sin acceso a internet", Toast.LENGTH_SHORT).show();
      cvNoData.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    getData(((d < 10 ? new Integer("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y));
  }
}
