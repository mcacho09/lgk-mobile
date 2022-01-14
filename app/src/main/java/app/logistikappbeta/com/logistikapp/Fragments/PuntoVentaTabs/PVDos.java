package app.logistikappbeta.com.logistikapp.Fragments.PuntoVentaTabs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.PVDosAdapter;
import app.logistikappbeta.com.logistikapp.POJOs.ProductPV;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PVDos extends Fragment {

  @BindView(R.id.rvList)
  RecyclerView rvList;
  @BindView(R.id.lyLoad)
  RelativeLayout lyLoad;

  private ArrayList<ProductPV> mData;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_pvdos, container, false);
    ButterKnife.bind(this, v);

    mData = new ArrayList<>();
    lyLoad.setVisibility(View.VISIBLE);

    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setHasFixedSize(true);
    rvList.setAdapter(new PVDosAdapter(mData));

    return v;
  }

  public void reset(ArrayList<ProductPV> data) {
    if (data != null) {

      for (ProductPV p : data) {
        p.setProduct_type_temp(p.getProduct_type() == null ? "PCS" :  p.getProduct_type());
      }

      this.mData.clear();
      this.mData.addAll(data);
      this.rvList.getAdapter().notifyDataSetChanged();
    }
    lyLoad.setVisibility(View.GONE);
  }

  public void search(String search) {
    Log.d("BUSQUEDA", "Buscando " + search);
    ((PVDosAdapter) rvList.getAdapter()).setSearch(search);
    rvList.getAdapter().notifyDataSetChanged();
    rvList.scrollToPosition(0);
  }

  public ArrayList<ProductPV> getData() {
    return mData;
  }

}
