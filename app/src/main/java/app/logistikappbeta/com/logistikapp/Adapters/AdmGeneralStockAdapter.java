package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import app.logistikappbeta.com.logistikapp.POJOs.ProductsAlmacenInfo;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danielmarin on 18/09/17.
 */

public class AdmGeneralStockAdapter extends RecyclerView.Adapter<AdmGeneralStockAdapter.VHAdmGeneralStock> {

  private Context context;
  private List<ProductsAlmacenInfo> products;
  private Boolean hiddeMinAndMax;

  public AdmGeneralStockAdapter(Context context, List<ProductsAlmacenInfo> products, Boolean hiddeMinAndMax) {
    this.context = context;
    this.products = products;
    this.hiddeMinAndMax = hiddeMinAndMax;
  }

  @Override
  public VHAdmGeneralStock onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_adm_general_stock, parent, false);
    return new VHAdmGeneralStock(view);
  }

  @Override
  public void onBindViewHolder(VHAdmGeneralStock holder, int position) {
    holder.setProduct(products.get(position));
  }

  @Override
  public int getItemCount() {
    return products.size();
  }


  protected class VHAdmGeneralStock extends RecyclerView.ViewHolder {

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.lay_min_max)
    LinearLayout lay_min_max;

    @BindView(R.id.tvMin)
    TextView tvMin;

    @BindView(R.id.tvMax)
    TextView tvMax;

    @BindView(R.id.tvQtyPcs)
    TextView tvQtyPcs;

    @BindView(R.id.tvQtyPkg)
    TextView tvQtyPkg;

    @BindView(R.id.tvPcsTotal)
    TextView tvPcsTotal;

    DecimalFormat df;

    public VHAdmGeneralStock(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      df = new DecimalFormat("#");
    }


    public void setProduct(ProductsAlmacenInfo p) {
      tvName.setText(p.getName_short());
      tvDescription.setText(p.getName_long());
      if (!hiddeMinAndMax) {
        tvMin.setText(df.format(p.getMin()));
        tvMax.setText(df.format(p.getMax()));
        lay_min_max.setVisibility(View.VISIBLE);
      } else {
        lay_min_max.setVisibility(View.GONE);
      }

      Long pkg = 0l;

      Long pcs;

      if (p.getPiece_in_box() > 0) {
        pkg = p.getQty() / p.getPiece_in_box().intValue();
        pcs = p.getQty() - (pkg) * p.getPiece_in_box();
      } else {
        pcs = p.getQty();
      }

      tvQtyPkg.setText("" + pkg);
      tvQtyPcs.setText("" + pcs);
      tvPcsTotal.setText("" + p.getQty());

    }

  }

}
