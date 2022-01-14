package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Store;
import app.logistikappbeta.com.logistikapp.Params.GetProductsParam;
import app.logistikappbeta.com.logistikapp.PuntoVentaActivity;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danie on 17/07/2016.
 */
public class CustomersToSaleAdapter extends RecyclerView.Adapter<CustomersToSaleAdapter.VHCustomersToSale> {

  private ArrayList<Store> list;
  private Context context;

  public CustomersToSaleAdapter(ArrayList<Store> list, Context context) {
    this.list = list;
    this.context = context;
  }

  @Override
  public VHCustomersToSale onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_customers, parent, false);
    return new VHCustomersToSale(v);
  }

  @Override
  public void onBindViewHolder(VHCustomersToSale holder, int position) {
    Store store = list.get(position);
    holder.setStore(store);
  }

  @Override
  public int getItemCount() {
    return getNoVisiblesItems();
  }

  public class VHCustomersToSale extends RecyclerView.ViewHolder {

    @BindView(R.id.tvNombre)
    TextView tvNombre;
    @BindView(R.id.tvCalle)
    TextView tvCalle;
    @BindView(R.id.tvCodigo)
    TextView tvCodigo;
    @BindView(R.id.lyPromo)
    LinearLayout lyPromo;
    @BindView(R.id.tvPlaza)
    TextView tvPlaza;
    @BindView(R.id.tvCategoria)
    TextView tvCategoria;
    @BindView(R.id.tvLat)
    TextView tvLat;
    @BindView(R.id.tvLng)
    TextView tvLon;

    public VHCustomersToSale(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.ivBtn)
    public void click(View v) {

      Intent intent = new Intent(v.getContext(), PuntoVentaActivity.class);
      intent.putExtra("id", list.get(getLayoutPosition()).getId());
      intent.putExtra("store", list.get(getLayoutPosition()).getName());
      intent.putExtra("email", list.get(getLayoutPosition()).getEmail());
      intent.putExtra("retail", list.get(getLayoutPosition()).getRetail());

      GetProductsParam param = new GetProductsParam();
      Login login = Utils.getSessionInfo(context);
      param.setToken(new BigInteger(Utils.getSaveToken(context)));
      param.setUid(login.getId());
      param.setSid((long)list.get(getLayoutPosition()).getId());
      String params = new Gson().toJson(param);
      intent.putExtra("params", params);
      context.startActivity(intent);
    }

    public void setStore(Store s) {
      tvNombre.setText(s.getName());
      tvCalle.setText(s.getAddres1() + ", " + s.getAddres2());
      tvCodigo.setText(s.getCode());
      lyPromo.setVisibility(s.getShelf() != null && s.getShelf().equals("S") ? View.VISIBLE : View.GONE);
      tvPlaza.setText(s.getRetail());
      tvCategoria.setText(s.getCategory());
      tvLat.setText("" + s.getLatitude());
      tvLon.setText("" + s.getLongitude());
      itemView.setVisibility(s.getVisible() ? View.VISIBLE : View.GONE);
    }

  }

  public void reset() {
    Log.d("BUSQUEDA", "RESET");
    for (Store s : this.list) {
      s.setVisible(true);
    }
  }

  public void search(String search) {
    this.reset();
    for (Store s : this.list) {
      Log.d("BUSQUEDA", "--> index code " + s.getCode().toLowerCase().indexOf(search.toLowerCase()) + " ---> index name " + s.getName().toLowerCase().indexOf(search.toLowerCase()));
      if (s.getCode().toLowerCase().indexOf(search.toLowerCase()) > -1 || s.getName().toLowerCase().indexOf(search.toLowerCase()) > -1) {
        Log.d("BUSQUEDA", s.getCode() + " : " + s.getName() + " : " + true);
        s.setVisible(true);
      } else {
        Log.d("BUSQUEDA", s.getCode() + " : " + s.getName() + " : " + false);
        s.setVisible(false);
      }
    }
  }

  public int getNoVisiblesItems() {
    int i = 0;
    for (Store s : this.list) {
      i += s.getVisible() ? 1 : 0;
    }
    return i;
  }

}
