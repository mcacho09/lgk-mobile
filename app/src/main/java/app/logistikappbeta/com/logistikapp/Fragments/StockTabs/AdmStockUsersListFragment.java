package app.logistikappbeta.com.logistikapp.Fragments.StockTabs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.logistikappbeta.com.logistikapp.Adapters.AdmGeneralSubStockAdapter;
import app.logistikappbeta.com.logistikapp.POJOs.AlmacenInfoList;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdmStockUsersListFragment extends Fragment {

  @BindView(R.id.rvList)
  RecyclerView rvList;

  private List<AlmacenInfoList> subalmacens;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_adm_stock_users_list, container, false);
    ButterKnife.bind(this, v);

    subalmacens = new ArrayList<>();

    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setHasFixedSize(true);
    rvList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setAdapter(new AdmGeneralSubStockAdapter(getContext(), subalmacens));

    return v;
  }

  public void setData(List<AlmacenInfoList> subalmacens) {
    this.subalmacens.clear();
    this.subalmacens.addAll(subalmacens);
    rvList.getAdapter().notifyDataSetChanged();
  }

}
