package app.logistikappbeta.com.logistikapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.MensajesAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.NuevoMensajeActivity;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.MessageList;
import app.logistikappbeta.com.logistikapp.Params.GetMessageListParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.MessageListResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.sRefresh)
  SwipeRefreshLayout sRefres;
  @BindView(R.id.rvList)
  RecyclerView rvList;

  private ArrayList<MessageList> data;
  private Login login;
  private BigInteger token;
  private Gson gson;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_message, container, false);
    ButterKnife.bind(this, v);

    data = new ArrayList<>();

    login = Utils.getSessionInfo(getContext());
    token = new BigInteger(Utils.getSaveToken(getContext()));
    gson = new Gson();

    sRefres.setOnRefreshListener(this);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new MensajesAdapter(data));

    return v;
  }

  @Override
  public void onResume() {
    super.onResume();
    getMsg();
  }

  @OnClick(R.id.btnNuevo)
  public void onClick(View v) {
    startActivity(new Intent(getActivity(), NuevoMensajeActivity.class));
  }

  @Override
  public void onRefresh() {
    getMsg();
  }

  public void getMsg() {

    GetMessageListParam param = new GetMessageListParam();
    param.setUid(login.getId());
    param.setToken(token);

    Call<MessageListResult> call = Utils.buildRetrofit(getContext()).create(WSInterface.class).getMessageList(param);
    call.enqueue(new Callback<MessageListResult>() {
      @Override
      public void onResponse(Call<MessageListResult> call, Response<MessageListResult> response) {
        sRefres.setRefreshing(false);

        MessageListResult result = response.body();

        if (result.getStatus() == 0) {
          if (result.getChats() != null) {
            data.clear();
            data.addAll(result.getChats());
            rvList.getAdapter().notifyDataSetChanged();
          }
        } else {
          Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<MessageListResult> call, Throwable t) {
        t.printStackTrace();
        sRefres.setRefreshing(false);
        Toast.makeText(getContext(), "No se pudieron obtener nuevos mensajes", Toast.LENGTH_LONG).show();
      }
    });

  }

}
