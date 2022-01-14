package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.ProductPV;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danie on 19/08/2016.
 */
public class PVDosAdapter extends RecyclerView.Adapter<PVDosAdapter.VHPVDos> {

  private ArrayList<ProductPV> list;
  private String search;

  public PVDosAdapter(ArrayList<ProductPV> list) {
    this.list = list;
    this.search = "";
  }

  @Override
  public VHPVDos onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_pv_dev_chg, parent, false);
    return new VHPVDos(v);
  }

  @Override
  public void onBindViewHolder(VHPVDos holder, int position) {
    ProductPV p = list.get(position);
    holder.setData(p);
  }

  @Override
  public int getItemCount() {
    return this.list.size();
  }

  public void setSearch(String search) {
    this.search = search;
    this.reset();
    this.setVisible();
  }

  public void reset() {
    Log.d("BUSQUEDA", "REINICIANDO");
    for (ProductPV p : this.list) {
      p.setVisible(true);
    }
  }

  public void setVisible() {
    for (ProductPV p : this.list) {
      p.setVisible(isVisible(p, search));
    }
  }

  public int getNoVisiblesItems() {
    int i = 0;

    for (ProductPV p : this.list) {
      i += isVisible(p, search) ? 1 : 0;
    }

    return i;
  }

  boolean isVisible(ProductPV p, String search) {
    return (p.getCode().toLowerCase().contains(search.toLowerCase()) || p.getName_short().toLowerCase().contains(search.toLowerCase()));
  }

  protected class VHPVDos extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    @BindView(R.id.tvQty)
    TextView tvQty;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvShortName)
    TextView tvShortName;
    @BindView(R.id.tvLongName)
    TextView tvLongName;
    @BindView(R.id.imgProductLess)
    ImageView imgProductLess;
    @BindView(R.id.tvType)
    TextView tvType;

    public VHPVDos(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnLongClickListener(this);
    }

    public void setData(ProductPV p) {

      if (p.getQtyproducts() == 0) {
        tvQty.setBackgroundResource(R.drawable.circle_shape_disabled);
        imgProductLess.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorGray));
      } else {
        tvQty.setBackgroundResource(R.drawable.circle_shape_no_aprove);
        imgProductLess.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorRed));
      }
      tvQty.setText("" + p.getQtyproducts());
      tvCode.setText(p.getCode());
      tvShortName.setText(p.getName_short());
      tvLongName.setText(p.getName_long());
      if (p.getVisible()) {
        Log.d("BUSQUEDA", "Visible " + p.getName_short());
        itemView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      } else {
        Log.d("BUSQUEDA", "No Visible " + p.getName_short());
        itemView.getLayoutParams().height = 0;
      }

      tvType.setText(p.getProduct_type_temp().contentEquals("PCS") ? "Piezas" : "Cajas");

    }

    @OnClick({R.id.tvQty, R.id.imgProductLess})
    public void OnClick(View v) {
      switch (v.getId()) {
        case R.id.tvQty: {
          ProductPV pv = list.get(getLayoutPosition());

          pv.setQtyproducts(pv.getQtyproducts() + 1);
          tvQty.setText("" + pv.getQtyproducts());
          list.remove(getLayoutPosition());
          list.add(getLayoutPosition(), pv);
          imgProductLess.setColorFilter(v.getContext().getResources().getColor(R.color.colorRed));
          tvQty.setBackgroundResource(R.drawable.circle_shape_aprove);
          pv.setSale(pv.getQtyproducts() * pv.getPrice_sale());

          notifyDataSetChanged();
        }
        break;
        case R.id.imgProductLess: {
          ProductPV pv = list.get(getLayoutPosition());
          if (pv.getQtyproducts() > 0) {
            pv.setQtyproducts(pv.getQtyproducts() - 1);
            tvQty.setText("" + pv.getQtyproducts());
            list.remove(getLayoutPosition());
            list.add(getLayoutPosition(), pv);
            pv.setSale(pv.getQtyproducts() * pv.getPrice_sale());
          }

          if (list.get(getLayoutPosition()).getQtyproducts() == 0) {
            tvQty.setBackgroundResource(R.drawable.circle_shape_disabled);
            imgProductLess.setColorFilter(v.getContext().getResources().getColor(R.color.colorGray));
            pv.setSale(0d);
          }
          notifyDataSetChanged();
        }
        break;
      }
    }

    @Override
    public boolean onLongClick(final View view) {

      final ProductPV p = list.get(getLayoutPosition());

      if (p.getProduct_type().contentEquals("PKG")) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
        dialog
            .setTitle("Selecciona el tipo de cambio")
            .setSingleChoiceItems(new String[]{"Pieza", "Caja"}, (p.getProduct_type_temp().contentEquals("PCS") ? 0 : 1), new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                list.get(getLayoutPosition()).setProduct_type_temp(i == 0 ? "PCS" : "PKG");
                list.get(getLayoutPosition()).setPrice_sug(i == 0 ? p.getPrice_sale_piece() : p.getPrice_sale());
                notifyDataSetChanged();
              }
            })
            .setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            })
            .create()
            .show();

      }

      return true;
    }

  }

}
