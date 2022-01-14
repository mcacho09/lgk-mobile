package app.logistikappbeta.com.logistikapp.Fragments.TravelWayBillTabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.TWBClientesAdapter;
import app.logistikappbeta.com.logistikapp.POJOs.Waybill;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TWBDosFragment extends Fragment {

  @BindView(R.id.rvList)
  RecyclerView rvList;

  ArrayList<Waybill> datos;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_twbdos, container, false);
    ButterKnife.bind(this, v);

    datos = new ArrayList<>();

    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new TWBClientesAdapter(datos));

    String content = getArguments().getString("stores");

      Gson gson = new Gson();

      ArrayList<Waybill> list = gson.fromJson(content, new TypeToken<ArrayList<Waybill>>() {
      }.getType());
      datos.addAll(list);

      rvList.getAdapter().notifyDataSetChanged();

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

}
