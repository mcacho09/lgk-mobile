package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.logistikappbeta.com.logistikapp.POJOs.AlmacenInfoList;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.StockActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danielmarin on 18/09/17.
 */

public class AdmGeneralSubStockAdapter extends RecyclerView.Adapter<AdmGeneralSubStockAdapter.VHAdmGeneralStock> {

  private Context context;
  private List<AlmacenInfoList> subalmacens;

  public AdmGeneralSubStockAdapter(Context context, List<AlmacenInfoList> subalmacens) {
    this.context = context;
    this.subalmacens = subalmacens;
  }

  @Override
  public VHAdmGeneralStock onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_adm_general_sub_stock, parent, false);
    return new VHAdmGeneralStock(view);
  }

  @Override
  public void onBindViewHolder(VHAdmGeneralStock holder, int position) {
    holder.setProduct(subalmacens.get(position));
  }

  @Override
  public int getItemCount() {
    return subalmacens.size();
  }


  protected class VHAdmGeneralStock extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvUser)
    TextView tvUser;
    @BindView(R.id.tvRetail)
    TextView tvRetail;
    @BindView(R.id.tvPcs)
    TextView tvPcs;
    @BindView(R.id.tvPkg)
    TextView tvPkg;

    public VHAdmGeneralStock(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }


    public void setProduct(AlmacenInfoList info) {
      tvCode.setText(info.getCode());
      tvUser.setText(context.getString(R.string.tv_repsponsable, info.getUsername()));
      tvRetail.setText(context.getString(R.string.tv_retail, info.getRetail()));
      tvPcs.setText(context.getString(R.string.tv_pcs_sub, "" + info.getQty_pieces()));
      tvPkg.setText(context.getString(R.string.tv_pkg_sub, "" + info.getQty_package()));
    }


    @Override
    public void onClick(View view) {
      Intent intent = new Intent(context, StockActivity.class);
      intent.putExtra("aId", subalmacens.get(getLayoutPosition()).getId_almacen());
      context.startActivity(intent);
    }
  }

}
