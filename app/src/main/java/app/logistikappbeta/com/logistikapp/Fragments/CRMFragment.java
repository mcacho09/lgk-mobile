package app.logistikappbeta.com.logistikapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.CRMAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Store;
import app.logistikappbeta.com.logistikapp.Params.GetCRMParams;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.CRMResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CRMFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.rvList)
  RecyclerView rvList;
  @BindView(R.id.rbNombre)
  RadioButton rbNombre;
  @BindView(R.id.rbCodigo)
  RadioButton rbCodigo;
  @BindView(R.id.etBusqueda)
  EditText etBusqueda;
  @BindView(R.id.tvNoResultados)
  TextView tvNoResultados;
  @BindView(R.id.tvBusqueda)
  TextView tvBusqueda;
  @BindView(R.id.sRefresh)
  SwipeRefreshLayout sRefresh;
  @BindView(R.id.btnBusqueda)
  ImageButton btnBusqueda;

  ArrayList<Store> datos;

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
    View v = inflater.inflate(R.layout.fragment_crm, container, false);
    ButterKnife.bind(this, v);

    login = Utils.getSessionInfo(getContext());
    token = Utils.getSaveToken(getContext());

    sRefresh.setOnRefreshListener(this);
    rvList.setHasFixedSize(true);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
    datos = new ArrayList<Store>();
    rvList.setAdapter(new CRMAdapter(datos));

    etBusqueda.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
          btnBusqueda.performClick();
          return true;
        }
        return false;
      }
    });

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

    if (!TextUtils.isEmpty(etBusqueda.getText()) && etBusqueda.getText().length() >= 2) {

      btnBusqueda.performClick();

    } else {
      etBusqueda.setError("Ingresar mínimo dos caracteres");
      sRefresh.setRefreshing(false);
    }
  }

  @OnClick(R.id.btnBusqueda)
  public void onClick(View v) {
    if (v.getId() == R.id.btnBusqueda) {

      if (!etBusqueda.getText().toString().trim().isEmpty() && etBusqueda.getText().length() >= 2) {

        GetCRMParams param = new GetCRMParams();
        param.setUid(login.getId().intValue());
        param.setProfile(login.getProfile());
        param.setToken(new BigInteger(token));
        param.setSearch(etBusqueda.getText().toString());
        param.setOption((rbNombre.isChecked() ? 1 : 2));

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
          inputMethodManager.hideSoftInputFromWindow(etBusqueda.getWindowToken(), 0);
        }

        sRefresh.setRefreshing(true);

        Call<CRMResult> call = Utils.buildRetrofit(getContext()).create(WSInterface.class).getCRM(param);
        call.enqueue(new Callback<CRMResult>() {
          @Override
          public void onResponse(Call<CRMResult> call, Response<CRMResult> response) {
            datos.clear();
            CRMResult result = response.body();
            if (result.getStatus() == 0) {

              tvNoResultados.setText("" + result.getStores().size());
              tvBusqueda.setText(etBusqueda.getText());

              datos.addAll(result.getStores());

            } else {
              Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
            }
            rvList.getAdapter().notifyDataSetChanged();

            sRefresh.setRefreshing(false);
          }

          @Override
          public void onFailure(Call<CRMResult> call, Throwable t) {
            t.printStackTrace();
            rvList.getAdapter().notifyDataSetChanged();
            sRefresh.setRefreshing(false);
          }
        });

      } else {
        etBusqueda.setError("Ingresar mínimo un carácter");
      }
    }
  }

}
