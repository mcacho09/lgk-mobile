package app.logistikappbeta.com.logistikapp.Fragments.PuntoVentaTabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.TransactionListAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.TransactionList;
import app.logistikappbeta.com.logistikapp.Params.GetSaleByDriParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.TransactionListResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PVCuatro extends Fragment {

  @BindView(R.id.rvList)
  RecyclerView rvList;

  private ArrayList<TransactionList> datos;
  private Login login;
  private String token;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_pvcuatro, container, false);
    ButterKnife.bind(this, v);

    datos = new ArrayList<>();

    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setHasFixedSize(true);
    rvList.setAdapter(new TransactionListAdapter(datos, getContext()));

    login = Utils.getSessionInfo(getContext());
    token = Utils.getSaveToken(getContext());

    updateTrxList();

    return v;
  }

  public void reset() {
    updateTrxList();
  }

  public void updateTrxList() {
    Integer id_store = getArguments().getInt("idS");

    GetSaleByDriParam param = new GetSaleByDriParam();
    param.setUid(login.getId().intValue());
    param.setToken(new BigInteger(token));
    param.setId_store(id_store.longValue());
    param.setLimit(5l);
    param.setProfile(login.getProfile());

    Call<TransactionListResult> call = Utils.buildRetrofit(getContext()).create(WSInterface.class).getTransactions(param);
    call.enqueue(new Callback<TransactionListResult>() {
      @Override
      public void onResponse(Call<TransactionListResult> call, Response<TransactionListResult> response) {
        datos.clear();

        if (response.isSuccessful()) {

          TransactionListResult result = response.body();
          if (result.getStatus() == 0) {
            Log.i("Logistikapp", "ESTATUS 0");

            datos.addAll(result.getTransactions());

          } else {
            Toast.makeText(getContext(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
          }

        } else {
          Toast.makeText(getContext(), "No se pudo obtener la lista de transacciones", Toast.LENGTH_SHORT).show();
        }
        rvList.getAdapter().notifyDataSetChanged();
      }

      @Override
      public void onFailure(Call<TransactionListResult> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(getContext(), "No se pudo obtener la lista de transacciones", Toast.LENGTH_SHORT).show();
      }
    });
  }

}
