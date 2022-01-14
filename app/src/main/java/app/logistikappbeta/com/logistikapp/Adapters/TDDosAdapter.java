package app.logistikappbeta.com.logistikapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.ProductDetail;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danie on 12/08/2016.
 */
public class TDDosAdapter extends RecyclerView.Adapter<TDDosAdapter.VHTDDos> {

    private Boolean isSale;
    private ArrayList<ProductDetail> list;

    public TDDosAdapter(Boolean isSale, ArrayList<ProductDetail> list) {
        this.isSale = isSale;
        this.list = list;
    }

    @Override
    public VHTDDos onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_tddos, parent, false);
        return new VHTDDos(v);
    }

    @Override
    public void onBindViewHolder(VHTDDos holder, int position) {
        ProductDetail p = list.get(position);
        holder.setData(p);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class VHTDDos extends RecyclerView.ViewHolder {

        @BindView(R.id.tvQty)
        TextView tvQty;
        @BindView(R.id.tvCode)
        TextView tvCode;
        @BindView(R.id.tvDescriptionS)
        TextView tvDescriptionS;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvSoled)
        TextView tvSoled;

        public VHTDDos(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(ProductDetail p){
            if (isSale){
                tvQty.setBackgroundResource(R.drawable.circle_shape_aprove);
            }else{
                tvSoled.setVisibility(View.GONE);
                tvQty.setBackgroundResource(R.drawable.circle_shape_no_aprove);
            }

            tvQty.setText("" + p.getQtyproducts());
            tvCode.setText(p.getCode());
            tvDescriptionS.setText(p.getName_short());
            tvDescription.setText(p.getName_short());
            tvSoled.setText("" + p.getSale());

        }

    }

}
