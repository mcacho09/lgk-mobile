package app.logistikappbeta.com.logistikapp.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.NotificacionesAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Notification;
import app.logistikappbeta.com.logistikapp.Params.GetNotificationsParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.GetNotificationsResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacionesFragment extends Fragment implements OnRefreshListener {

  @BindView(R.id.sRefresh)
  SwipeRefreshLayout sRefresh;
  @BindView(R.id.rvList)
  RecyclerView rvList;

  private ArrayList<Notification> data;
  private Gson gson;
  private Login login;
  private BigInteger token;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_notificaciones, container, false);
    ButterKnife.bind(this, v);

    data = new ArrayList<>();
    gson = new Gson();

    token = new BigInteger(Utils.getSaveToken(getContext()));
    login = Utils.getSessionInfo(getContext());

    sRefresh.setOnRefreshListener(this);
    rvList.setHasFixedSize(true);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new NotificacionesAdapter(data));

    return v;
  }

  @Override
  public void onResume() {
    super.onResume();
    getNotifications();
  }

  @Override
  public void onRefresh() {
    getNotifications();
  }

  public void getNotifications() {

    GetNotificationsParam param = new GetNotificationsParam();
    param.setUid(login.getId());
    param.setToken(token);

    sRefresh.setRefreshing(true);

    Call<GetNotificationsResult> call = Utils.buildRetrofit(getContext())
        .create(WSInterface.class)
        .getNotifications(param);

    call.enqueue(new Callback<GetNotificationsResult>() {
      @Override
      public void onResponse(Call<GetNotificationsResult> call, Response<GetNotificationsResult> response) {
        GetNotificationsResult result = response.body();

        if (result.getStatus() == 0) {
          data.clear();
          data.addAll(result.getNotifications());
          rvList.getAdapter().notifyDataSetChanged();
        } else {
          Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
        }

        sRefresh.setRefreshing(false);

      }

      @Override
      public void onFailure(Call<GetNotificationsResult> call, Throwable t) {
        Log.e(getString(R.string.app_name), "Error: onFailure", t);
        sRefresh.setRefreshing(false);
        Toast.makeText(getContext(), "No se obtuvieron las notificaciones\nVerifique su acceso a internet", Toast.LENGTH_SHORT).show();
      }
    });

  }


}
