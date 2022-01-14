package app.logistikappbeta.com.logistikapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.NuevoMensajeAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.UserChat;
import app.logistikappbeta.com.logistikapp.Params.GetUserListParam;
import app.logistikappbeta.com.logistikapp.Results.UserListResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoMensajeActivity extends AppCompatActivity implements View.OnClickListener {

  @BindView(R.id.rvList)
  RecyclerView rvList;

  private ArrayList<UserChat> data;
  private Gson gson;
  private Login login;
  private BigInteger token;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nuevo_mensaje);
    ButterKnife.bind(this);
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    setTitle("Mensajes");

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);

    setTitle("Nuevo Mensaje");

    gson = new Gson();
    login = Utils.getSessionInfo(this);
    token = new BigInteger(Utils.getSaveToken(this));
    data = new ArrayList<>();

    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new NuevoMensajeAdapter(data, this));

  }

  @Override
  protected void onResume() {
    super.onResume();
    getUsers();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  @Override
  public void onClick(View view) {
    finish();
  }

  public void getUsers() {
    GetUserListParam param = new GetUserListParam();
    param.setToken(token);
    param.setUid(login.getId());
    Call<UserListResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getUserList(param);
    call.enqueue(new Callback<UserListResult>() {
      @Override
      public void onResponse(Call<UserListResult> call, Response<UserListResult> response) {
        UserListResult result = response.body();
        if (result.getStatus() == 0) {
          if (result.getUsers() != null) {
            data.clear();
            data.addAll(result.getUsers());
            rvList.getAdapter().notifyDataSetChanged();
          }
        } else {
          Toast.makeText(NuevoMensajeActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<UserListResult> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(NuevoMensajeActivity.this, "No se pudieron obtener nuevos mensajes", Toast.LENGTH_LONG).show();
      }
    });
  }

}
