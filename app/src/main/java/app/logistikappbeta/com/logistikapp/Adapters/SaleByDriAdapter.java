package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Fragments.TrxADMFragment;
import app.logistikappbeta.com.logistikapp.POJOs.ReportByDri;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danie on 09/01/2017.
 */

public class SaleByDriAdapter extends RecyclerView.Adapter<SaleByDriAdapter.VHSaleByDri> {

  private ArrayList<ReportByDri> list;
  private Context context;
  private TrxADMFragment fragment;

  public SaleByDriAdapter(ArrayList<ReportByDri> list, Context context, TrxADMFragment fragment) {
    this.list = list;
    this.context = context;
    this.fragment = fragment;
  }

  @Override
  public VHSaleByDri onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_sale_by_dri, parent, false);
    return new VHSaleByDri(v);
  }

  @Override
  public void onBindViewHolder(VHSaleByDri holder, int position) {
    holder.setData(list.get(position));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  protected class VHSaleByDri extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvSaled)
    TextView tvSaled;
    @BindView(R.id.tvNoTrx)
    TextView tvNoTrx;
    @BindView(R.id.tvProductsQty)
    TextView tvProductsQty;

    public VHSaleByDri(View v) {
      super(v);
      ButterKnife.bind(this, v);
      v.findViewById(R.id.ivBtn).setOnClickListener(this);
    }

    public void setData(ReportByDri s) {
      tvName.setText(s.getUsername());
      tvSaled.setText(context.getString(R.string.amount, s.getSales()));
      tvNoTrx.setText("" + s.getNo_trx());
      tvProductsQty.setText("" + s.getTotal_products().intValue());
    }

    @Override
    public void onClick(View view) {
      fragment.changeToSaleList(list.get(getLayoutPosition()).getId_user());
    }
  }

}
