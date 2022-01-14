package app.logistikappbeta.com.logistikapp.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.logistikappbeta.com.logistikapp.Adapters.HistoricoAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Travel;
import app.logistikappbeta.com.logistikapp.Params.GetHistoricListParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.TravelResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

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
    View v = inflater.inflate(R.layout.fragment_historico, container, false);
    ButterKnife.bind(this, v);

    login = Utils.getSessionInfo(getContext());
    token = Utils.getToken();

    y = calendar.get(Calendar.YEAR);
    m = calendar.get(Calendar.MONTH);
    d = calendar.get(Calendar.DAY_OF_MONTH);
    btnCalendar.setText(sdf.format(new Date()));

    sRefresh.setOnRefreshListener(this);

    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setHasFixedSize(true);
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    datos = new ArrayList<>();
    rvList.setAdapter(new HistoricoAdapter(datos, login.getProfile().startsWith("DRI"), login.getId(), token));

    getData();

    return v;
  }

  @Override
  public void onRefresh() {
    getData();
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
        btnCalendar.setText((d < 10 ? ("0" + d) : d) + "/" + ((m + 1) < 10 ? ("0" + (m + 1)) : (m + 1)) + "/" + y);

        getData();
      }
    }, y, m, d);
  }

  public void getData() {
    GetHistoricListParam param = new GetHistoricListParam();
    param.setUid(login.getId().intValue());
    param.setToken(token);
    param.setProfile(login.getProfile());
    param.setDate(((d < 10 ? ("0" + d) : d) + "/" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y));

    sRefresh.setRefreshing(true);
    cvNoData.setVisibility(View.GONE);

    Call<TravelResult> call = Utils.buildRetrofit(getContext()).create(WSInterface.class).getTravelHistory(param);
    call.enqueue(new Callback<TravelResult>() {
      @Override
      public void onResponse(Call<TravelResult> call, Response<TravelResult> response) {
        datos.clear();

        TravelResult result = response.body();
        if (result.getStatus() == 0) {
          if (result.getTravels().size() == 0) {
            cvNoData.setVisibility(View.VISIBLE);
          } else {
            cvNoData.setVisibility(View.GONE);
          }

          datos.addAll(result.getTravels());
        } else {
          Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
        }

        rvList.getAdapter().notifyDataSetChanged();
        rvList.scrollToPosition(0);
        sRefresh.setRefreshing(false);
      }

      @Override
      public void onFailure(Call<TravelResult> call, Throwable t) {
        t.printStackTrace();
      }
    });
  }

}
