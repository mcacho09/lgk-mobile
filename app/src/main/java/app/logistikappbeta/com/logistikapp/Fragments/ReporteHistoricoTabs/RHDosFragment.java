package app.logistikappbeta.com.logistikapp.Fragments.ReporteHistoricoTabs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.RHClientesAdapter;
import app.logistikappbeta.com.logistikapp.POJOs.Waybill;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RHDosFragment extends Fragment {

  @BindView(R.id.rvList)
  RecyclerView rvList;

  ArrayList<Waybill> datos;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_rhdos, container, false);
    ButterKnife.bind(this, v);

    String content = getArguments().getString("stores");
    Gson gson = new Gson();
    datos = gson.fromJson(content, new TypeToken<ArrayList<Waybill>>() {
    }.getType());

    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new RHClientesAdapter(datos, getContext()));

    return v;
  }
}
