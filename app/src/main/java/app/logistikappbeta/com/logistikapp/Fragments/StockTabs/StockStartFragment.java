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
import app.logistikappbeta.com.logistikapp.POJOs.SubAlmacen;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StockStartFragment extends Fragment {

  @BindView(R.id.tvCode)
  TextView tvCode;

  @BindView(R.id.tvUser)
  TextView tvUser;

  @BindView(R.id.tvRetail)
  TextView tvRetail;

  @BindView(R.id.rvList)
  RecyclerView rvList;

  @BindView(R.id.cvNoData)
  CardView cvNoData;

  @BindView(R.id.tvTotalPackages)
  TextView tvTotalPackages;

  @BindView(R.id.tvTotalProducts)
  TextView tvTotalProducts;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_stock_start, container, false);
    ButterKnife.bind(this, v);

    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setHasFixedSize(true);
    rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    return v;
  }

  public void setData(SubAlmacen almacen, List<ProductsAlmacenInfo> productsTotal) {
    this.tvCode.setText(almacen.getCode());
    this.tvUser.setText(almacen.getUsername());
    this.tvRetail.setText(almacen.getRetail());
    Long qtyPackages = 0l;
    Long qtyProducts = 0l;
    for(ProductsAlmacenInfo x: productsTotal) {
      if (x.getPiece_in_box() > 0) {
        qtyPackages += (x.getQty() / x.getPiece_in_box());
        qtyProducts += x.getQty() - (x.getQty() / x.getPiece_in_box()) * x.getPiece_in_box();
      } else {
        qtyProducts += x.getQty();
      }
    }
    this.tvTotalPackages.append("" + qtyPackages);
    this.tvTotalProducts.append("" + qtyProducts);
    //this.tvTotalPackages.append("" + almacen.getQty_package());
    //this.tvTotalProducts.append("" + almacen.getQty_pieces());
    cvNoData.setVisibility(productsTotal.size() == 0 ? View.VISIBLE : View.GONE);
    rvList.setAdapter(new AdmGeneralStockAdapter(getContext(), productsTotal, false));
  }

}
