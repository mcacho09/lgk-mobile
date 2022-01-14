package app.logistikappbeta.com.logistikapp.Adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Waybill;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danie on 19/07/2016.
 */
public class TWBClientesAdapter extends RecyclerView.Adapter<TWBClientesAdapter.VHTWBClientes> {

    private ArrayList<Waybill> list;

    public TWBClientesAdapter(ArrayList<Waybill> list) {
        this.list = list;
    }

    @Override
    public VHTWBClientes onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_twb_cliente, parent ,false);
        return new VHTWBClientes(v);
    }

    @Override
    public void onBindViewHolder(VHTWBClientes holder, int position) {
        Waybill waybill = this.list.get(position);
        holder.imgStar.setVisibility(waybill.getShelf() != null && waybill.getShelf().equals("S")?View.VISIBLE:View.GONE);
        holder.tvNombre.setText(waybill.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class VHTWBClientes extends RecyclerView.ViewHolder{

        @BindView(R.id.tvNombre) TextView tvNombre;
        @BindView(R.id.imgStar) ImageView imgStar;

        public VHTWBClientes(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
