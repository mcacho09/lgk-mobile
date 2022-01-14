package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.AdminCustomersActivity;
import app.logistikappbeta.com.logistikapp.MapsCRMActivity;
import app.logistikappbeta.com.logistikapp.POJOs.Store;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danie on 17/07/2016.
 */
public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.VHCustomers> {

  private ArrayList<Store> list;
  private Context context;

  public CustomersAdapter(ArrayList<Store> list, Context context) {
    this.list = list;
    this.context = context;
  }

  @Override
  public VHCustomers onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_customers, parent, false);
    return new VHCustomers(v);
  }

  @Override
  public void onBindViewHolder(VHCustomers holder, int position) {
    Store store = list.get(position);
    holder.setStore(store);
  }

  @Override
  public int getItemCount() {
    return this.list.size();
  }

  public class VHCustomers extends RecyclerView.ViewHolder implements View.OnClickListener {

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

    public VHCustomers(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @OnClick(R.id.ivBtn)
    public void click(View v) {

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

    public void setStore(Store s) {
      tvNombre.setText(s.getName());
      tvCalle.setText(s.getAddres1() + ", " + s.getAddres2());
      tvCodigo.setText(s.getCode());
      lyPromo.setVisibility(s.getShelf() != null && s.getShelf().equals("S") ? View.VISIBLE : View.GONE);
      tvPlaza.setText(s.getRetail());
      tvCategoria.setText(s.getCategory());
      tvLat.setText("" + s.getLatitude());
      tvLon.setText("" + s.getLongitude());

      if (s.getVisible()) {
        itemView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      } else {
        itemView.getLayoutParams().height = 0;
      }
    }

    @Override
    public void onClick(final View view) {
      final String[] options = {"Editar"};
      AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
      alert.create();
      alert.setTitle("Opciones")
          .setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
              Intent intent = new Intent(context, AdminCustomersActivity.class);
              intent.putExtra("sid", list.get(getLayoutPosition()).getId());
              context.startActivity(intent);
            }
          }).show();

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

}
