package app.logistikappbeta.com.logistikapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.ConversacionAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Conversacion;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.Params.GetMessageParam;
import app.logistikappbeta.com.logistikapp.Params.SendMessageParam;
import app.logistikappbeta.com.logistikapp.Results.MessageResult;
import app.logistikappbeta.com.logistikapp.Results.SendMessageResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversacionActivity extends AppCompatActivity implements View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.tvConversando)
  TextView tvConversando;
  @BindView(R.id.sRefresh)
  SwipeRefreshLayout sRefres;
  @BindView(R.id.rvList)
  RecyclerView rvList;
  @BindView(R.id.etMensaje)
  EditText etMensaje;
  @BindView(R.id.fab)
  FloatingActionButton fab;

  private ArrayList<Conversacion> datos;
  private Long id_conversation;
  private Long id_user_to;
  private Gson gson;
  private Login login;
  private BigInteger token;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    setContentView(R.layout.activity_conversacion);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);

    setTitle("Mensajes");

    gson = new Gson();

    Intent intent = getIntent();
    String name = intent.getStringExtra("name");
    id_conversation = intent.getLongExtra("cId", 0l);
    id_user_to = intent.getLongExtra("idUser", 0l);
    login = Utils.getSessionInfo(this);
    token = new BigInteger(Utils.getSaveToken(this));

    if (name != null) {
      tvConversando.setText(name);
    }
    datos = new ArrayList<Conversacion>();
    sRefres.setOnRefreshListener(this);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setHasFixedSize(true);
    rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new ConversacionAdapter(datos));

  }

  @Override
  protected void onResume() {
    super.onResume();
    getMessages();
  }

  @OnClick(R.id.fab)
  public void OnClick(View v) {
    if (v.getId() == R.id.fab) {
      sendMessage();
    }
  }

  @Override
  public void onClick(View view) {
    finish();
  }

  public void sendMessage() {
    if (!TextUtils.isEmpty(etMensaje.getText().toString().trim())) {
      fab.setEnabled(false);
      SendMessageParam param = new SendMessageParam();
      param.setRuid(id_user_to);
      param.setToken(token);
      param.setUid(login.getId());
      param.setMessage(etMensaje.getText().toString());
      sRefres.setRefreshing(true);
      Call<SendMessageResult> call = Utils.buildRetrofit(this).create(WSInterface.class).sendMessage(param);
      call.enqueue(new Callback<SendMessageResult>() {
        @Override
        public void onResponse(Call<SendMessageResult> call, Response<SendMessageResult> response) {

          SendMessageResult result = response.body();
          if (result.getStatus() == 0) {
            id_conversation = result.getId_conversation();
            etMensaje.setText("");
            getMessages();
          } else {
            Toast.makeText(ConversacionActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
          }
          sRefres.setRefreshing(false);
          fab.setEnabled(true);
        }

        @Override
        public void onFailure(Call<SendMessageResult> call, Throwable t) {
          t.printStackTrace();
          sRefres.setRefreshing(false);
          Toast.makeText(ConversacionActivity.this, "No se pudo obtener respuesta", Toast.LENGTH_SHORT).show();
          fab.setEnabled(true);
        }
      });
    } else {
      etMensaje.setError("El mensaje no puede estar vacio");
    }
  }

  public void getMessages() {
    if (id_conversation > 0l) {
      GetMessageParam param = new GetMessageParam();
      param.setUid(login.getId());
      param.setToken(token);
      param.setId_conversation(id_conversation);

      sRefres.setRefreshing(true);

      Call<MessageResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getMessage(param);
      call.enqueue(new Callback<MessageResult>() {
        @Override
        public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
          MessageResult result = response.body();
          datos.clear();
          if (result.getStatus() == 0) {
            if (result.getMessages() != null) {
              datos.addAll(result.getMessages());
              rvList.scrollToPosition(datos.size() == 0 ? 0 : datos.size() - 1);
            }
          } else {
            Toast.makeText(ConversacionActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
          }
          rvList.getAdapter().notifyDataSetChanged();
          sRefres.setRefreshing(false);
        }

        @Override
        public void onFailure(Call<MessageResult> call, Throwable t) {
          t.printStackTrace();
          sRefres.setRefreshing(false);
          Toast.makeText(ConversacionActivity.this, "No se pudo obtener respuesta", Toast.LENGTH_SHORT).show();
        }
      });

    } else {
      sRefres.setRefreshing(false);
    }
  }

  @Override
  public void onRefresh() {
    getMessages();
  }

}
