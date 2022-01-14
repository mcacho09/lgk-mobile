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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import app.logistikappbeta.com.logistikapp.Adapters.AdmGeneralStockAdapter;
import app.logistikappbeta.com.logistikapp.POJOs.Almacen;
import app.logistikappbeta.com.logistikapp.POJOs.AlmacenAndProducts;
import app.logistikappbeta.com.logistikapp.POJOs.ProductsAlmacenInfo;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdmGeneralStockFragment extends Fragment {

  @BindView(R.id.tvCode)
  TextView tvCode;
  @BindView(R.id.tvNoProducts)
  TextView tvNoProducts;
  @BindView(R.id.tvCost)
  TextView tvCost;
  @BindView(R.id.tvSale)
  TextView tvSale;
  @BindView(R.id.rvList)
  RecyclerView rvList;

  @BindView(R.id.lay_info)
  LinearLayout lay_info;
  @BindView(R.id.cvNoData)
  CardView cvNoData;

  private AlmacenAndProducts mAlmacenAndProducts;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_adm_general_stock, container, false);
    ButterKnife.bind(this, v);

    rvList.setHasFixedSize(true);
    rvList.setItemAnimator(new DefaultItemAnimator());
    rvList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

    return v;
  }

  public void setData(AlmacenAndProducts data) {
    this.mAlmacenAndProducts = data;
    if (data.getAlmacen() == null) {
      cvNoData.setVisibility(View.VISIBLE);
      lay_info.setVisibility(View.GONE);
    } else {
      cvNoData.setVisibility(View.GONE);
      lay_info.setVisibility(View.VISIBLE);
      this.initAlmacen(this.mAlmacenAndProducts.getAlmacen());
      this.initProducts(this.mAlmacenAndProducts.getProducts());
    }
  }

  public void initAlmacen(Almacen almacen) {
    this.tvCode.setText(almacen.getCode());
  }

  public void initProducts(List<ProductsAlmacenInfo> list) {

    int noProducts = 0;
    int noPkg = 0;
    double cost = 0d;
    double sale = 0d;

    for (ProductsAlmacenInfo p : list) {

      if (p.getType().contentEquals("PCS")) {
        noProducts++;
        cost += p.getQty() * p.getPrice_cost();
        sale += p.getQty() * p.getPrice_sale();

      } else {
        noPkg++;
        cost += p.getQty() * p.getPrice_cost() * p.getPiece_in_box();
        sale += p.getQty() * p.getPrice_cost_box();
      }
    }

    tvNoProducts.setText(getString(R.string.text_stock_tv_no_products, noProducts, noPkg));
    tvCost.setText(getString(R.string.text_stock_tv_cost, cost));
    tvSale.setText(getString(R.string.text_stock_tv_sale, sale));

    rvList.setAdapter(new AdmGeneralStockAdapter(getContext(), list, false));

  }

}
