package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.MapsCRMActivity;
import app.logistikappbeta.com.logistikapp.POJOs.Store;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danie on 17/07/2016.
 */
public class CRMAdapter extends RecyclerView.Adapter<CRMAdapter.VHCRM> {

    private ArrayList<Store> list;

    public CRMAdapter(ArrayList<Store> list) {
        this.list = list;
    }

    @Override
    public VHCRM onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_crm, parent, false);
        return new VHCRM(v);
    }

    @Override
    public void onBindViewHolder(VHCRM holder, int position) {
        Store store = list.get(position);
        holder.setStore(store);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VHCRM extends RecyclerView.ViewHolder{

        @BindView(R.id.tvNombre) TextView tvNombre;
        @BindView(R.id.tvCalle) TextView tvCalle;
        @BindView(R.id.tvCodigo) TextView tvCodigo;
        @BindView(R.id.lyPromo) LinearLayout lyPromo;
        @BindView(R.id.tvPlaza) TextView tvPlaza;
        @BindView(R.id.tvCategoria) TextView tvCategoria;
        @BindView(R.id.tvLat) TextView tvLat;
        @BindView(R.id.tvLng) TextView tvLon;

        public VHCRM(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ivBtn)
        public void onClick(View v){

            double lat = list.get(getLayoutPosition()).getLatitude();
            double lon = list.get(getLayoutPosition()).getLongitude();

            Intent intent = new Intent(v.getContext(), MapsCRMActivity.class);
            intent.putExtra("name", tvNombre.getText());
            intent.putExtra("street", tvCalle.getText());
            intent.putExtra("promo", lyPromo.getVisibility() != View.GONE);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            v.getContext().startActivity(intent);
        }

        public void setStore(Store s){
            tvNombre.setText(s.getName());
            tvCalle.setText(s.getAddres1() + ", " + s.getAddres2());
            tvCodigo.setText(s.getCode());
            lyPromo.setVisibility(s.getShelf() != null && s.getShelf().equals("S")? View. VISIBLE:View.GONE);
            tvPlaza.setText(s.getRetail());
            tvCategoria.setText(s.getCategory());
            tvLat.setText("" + s.getLatitude());
            tvLon.setText("" + s.getLongitude());
        }

    }

}
