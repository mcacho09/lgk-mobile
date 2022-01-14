package app.logistikappbeta.com.logistikapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.RetailsAdapter;
import app.logistikappbeta.com.logistikapp.AdminCustomersActivity;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Retail;
import app.logistikappbeta.com.logistikapp.Params.GetRetailsParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.GetRetailsResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  private static final String TAG = "Logistikapp";

  @BindView(R.id.sRefresh)
  SwipeRefreshLayout sRefresh;
  @BindView(R.id.rvList)
  RecyclerView rvList;
  @BindView(R.id.cbNoData)
  CardView cvNoData;
  @BindView(R.id.btnAddClient)
  Button btnAddClient;

  ArrayList<Retail> data;
  Login login;
  String token;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_client, container, false);
    ButterKnife.bind(this, v);

    login = Utils.getSessionInfo(getContext());
    token = Utils.getToken();

    btnAddClient.setVisibility(login.getSuperuser().contentEquals("S")?View.VISIBLE : View.GONE);

    data = new ArrayList<>();

    sRefresh.setOnRefreshListener(this);
    sRefresh.setRefreshing(true);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new RetailsAdapter(data, getContext()));

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

  @Override
  public void onRefresh() {
    fillList();
  }

  @Override
  public void onResume() {
    super.onResume();
    fillList();
  }

  public void fillList() {
    GetRetailsParam param = new GetRetailsParam();
    param.setToken(new BigInteger(token));
    param.setUid(login.getId().intValue());

    sRefresh.setRefreshing(true);
    cvNoData.setVisibility(View.GONE);

    Call<GetRetailsResult> call = Utils.buildRetrofit(getContext())
        .create(WSInterface.class)
        .getRetails(param);

    call.enqueue(new Callback<GetRetailsResult>() {
      @Override
      public void onResponse(Call<GetRetailsResult> call, Response<GetRetailsResult> response) {
        sRefresh.setRefreshing(false);
        GetRetailsResult result = response.body();
        if (result == null) return;
        if (result.getStatus() == 0) {
          int size = result.getRetails().size();

          if (size == 0 ){
            cvNoData.setVisibility(View.VISIBLE);
          }
          else if(size > 0 ){
            cvNoData.setVisibility(View.GONE);
          }
          data.clear();

          //Only SUP1, SUP & ADM that are SUPERUSER could see the list of clients.
          if((    login.getProfile().equals("ADM") ||
                  login.getProfile().equals("SUP") ||
                  login.getProfile().equals("SUP1")) &&
                  login.getSuperuser().contentEquals("S")){
            data.addAll(result.getRetails());
          }
          rvList.getAdapter().notifyDataSetChanged();
        } else {
          Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<GetRetailsResult> call, Throwable t) {
        Log.e(TAG, "Error: onFailure", t);
        sRefresh.setRefreshing(false);
        cvNoData.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Verifique su acceso a internet", Toast.LENGTH_SHORT).show();
      }
    });

  }

  @OnClick({R.id.btnAddClient})
  public void click(View v) {
    if (v.getId() == R.id.btnAddClient) {
      Intent intent = new Intent(getContext(), AdminCustomersActivity.class);
      intent.putExtra("new", true);
      intent.putExtra("nor", true);
      startActivity(intent);
    }
  }
}
