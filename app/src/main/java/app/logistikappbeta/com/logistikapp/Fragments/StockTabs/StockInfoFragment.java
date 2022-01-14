package app.logistikappbeta.com.logistikapp.Fragments.StockTabs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.ProductsAlmacenInfo;
import app.logistikappbeta.com.logistikapp.POJOs.SubAlmacen;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StockInfoFragment extends Fragment {

  @BindView(R.id.tvCode)
  TextView tvCode;
  @BindView(R.id.tvUser)
  TextView tvUser;
  @BindView(R.id.tvRetail)
  TextView tvRetail;
  @BindView(R.id.tvNoProducts)
  TextView tvNoProducts;
  @BindView(R.id.tvCost)
  TextView tvCost;
  @BindView(R.id.tvSale)
  TextView tvSale;
  @BindView(R.id.lay_cost)
  LinearLayout lay_cost;

  private Login login;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_stock_info, container, false);
    ButterKnife.bind(this, v);

    login = Utils.getSessionInfo(getContext());

    if (login.getProfile().startsWith("DRI")) {
      lay_cost.setVisibility(View.GONE);
    }

    tvCode.setText("...");
    tvUser.setText("...");
    tvRetail.setText("...");

    tvNoProducts.setText(getString(R.string.text_stock_tv_no_products, 0, 0));
    tvCost.setText(getString(R.string.text_stock_tv_cost, 0.0));
    tvSale.setText(getString(R.string.text_stock_tv_sale, 0.0));

    return v;
  }

  public void setData(SubAlmacen almacen) {
    this.tvCode.setText(almacen.getCode());
    this.tvUser.setText(almacen.getUsername());
    this.tvRetail.setText(almacen.getRetail());
  }

  public void setProducts(List<ProductsAlmacenInfo> list) {
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
        Log.e("estooo," , "esta");
      }
    }

    tvNoProducts.setText(getString(R.string.text_stock_tv_no_products, noProducts, noPkg));
    tvCost.setText(getString(R.string.text_stock_tv_cost, cost));
    tvSale.setText(getString(R.string.text_stock_tv_sale, sale));

  }

}
