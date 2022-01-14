package app.logistikappbeta.com.logistikapp.Fragments.StockTabs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.logistikappbeta.com.logistikapp.Adapters.AdmGeneralStockAdapter;
import app.logistikappbeta.com.logistikapp.POJOs.ProductsAlmacenInfo;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StockSaleFragment extends Fragment {

  @BindView(R.id.rvList)
  RecyclerView rvList;

  @BindView(R.id.cvNoData)
  CardView cvNoData;

  @BindView(R.id.tvTotalProducts)
  TextView tvTotalProducts;

  @BindView(R.id.tvTotalPackages)
  TextView tvTotalPackages;

  @BindView(R.id.tvTotalPieces)
  TextView tvTotalPieces;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_stock_sale, container, false);
    ButterKnife.bind(this, v);

    rvList.setHasFixedSize(true);
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvList.setItemAnimator(new DefaultItemAnimator());

    return v;
  }

  public void setData(List<ProductsAlmacenInfo> list) {

    cvNoData.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);

    rvList.setAdapter(new AdmGeneralStockAdapter(getContext(), list, true));

    Long total = 0l;
    Long products = 0l;
    Long packages = 0l;

    for (ProductsAlmacenInfo i : list) {
      if (i.getPiece_in_box() > 0) {
        packages += (i.getQty() / i.getPiece_in_box());
        products += i.getQty() - (i.getQty() / i.getPiece_in_box()) * i.getPiece_in_box();
      } else {
        products += i.getQty();
      }
      total += i.getQty();
    }

    this.tvTotalPackages.append("" + packages);
    this.tvTotalProducts.append("" + products);
    this.tvTotalPieces.append("" + total);
  }

}
