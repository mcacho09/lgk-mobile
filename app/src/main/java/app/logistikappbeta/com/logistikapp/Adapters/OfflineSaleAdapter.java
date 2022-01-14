package app.logistikappbeta.com.logistikapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import app.logistikappbeta.com.logistikapp.PrintTicketActivity;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.RealmObjects.SaleRO;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by danielmarin on 18/12/17.
 */

public class OfflineSaleAdapter extends RecyclerView.Adapter<OfflineSaleAdapter.OfflineSaleVH> {
  private int position;
  private static RealmResults<SaleRO> list;
  private LayoutInflater mInflater;
  private Realm realm;

  public OfflineSaleAdapter(Context context, Realm realm, RealmResults<SaleRO> results) {
    mInflater = LayoutInflater.from(context);
    this.realm = realm;
    update(results);
  }

  public void update(RealmResults<SaleRO> results){
    list = results;
    notifyDataSetChanged();
  }

  @Override
  public OfflineSaleVH onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = mInflater.inflate(R.layout.item_sale_offline,parent,false);
    OfflineSaleVH holder = new OfflineSaleVH(view);
    return holder;
   // return new OfflineSaleVH(LayoutInflater.from(parent.getContext())
    //    .inflate(R.layout.item_sale_offline, parent, false));
  }

  @Override
  public void onBindViewHolder(OfflineSaleVH holder, final int position) {
    SaleRO saleRO = list.get(position);
    holder.init(saleRO);
    this.position = position;
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  @Override
  public int getItemViewType(int position) {
    return TimelineView.getTimeLineViewType(position, getItemCount());
  }

  public RealmResults<SaleRO> getList() {
    return list;
  }

  public void setList(RealmResults<SaleRO> list) {
    this.list = list;
  }

  protected class OfflineSaleVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.time_marker)
    TimelineView timelineView;
    @BindView(R.id.tvStore)
    TextView tvStore;
    @BindView(R.id.tvSale)
    TextView tvSale;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.img_delete)
    ImageView img_delete;
    @BindView(R.id.cvContent)
    CardView cvContent;

    SimpleDateFormat sdf;
    DecimalFormat df;

    public OfflineSaleVH(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      timelineView.initLine(getItemViewType());

      sdf = new SimpleDateFormat("dd/MM/yyyy");
      df = new DecimalFormat("0.00");
      df.setMaximumIntegerDigits(999999999);

      cvContent.setOnClickListener(this);
      img_delete.setOnClickListener(this);
    }

    public void init(SaleRO saleRO) {
      tvStore.setText(saleRO.getStore());
      tvSale.setText(itemView.getContext().getString(R.string.tv_sale_and_dev,
          df.format(saleRO.getTotal()), df.format(saleRO.getDevSale())));
      tvDate.setText(sdf.format(saleRO.getCreated()));

      Drawable icon = null;

      switch (saleRO.getType()) {
        case "SYNC": {
          icon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_sync);
          DrawableCompat.setTint(icon, ContextCompat.getColor(itemView.getContext(), R.color.colorOrange));
        }
        break;
        case "SYNCED": {
          icon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_check_circle);
          DrawableCompat.setTint(icon, ContextCompat.getColor(itemView.getContext(), R.color.colorGreen));
        }
        break;
        case "NOT_SYNC": {
          icon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_remove_circle_outline_black_24dp);
          DrawableCompat.setTint(icon, ContextCompat.getColor(itemView.getContext(), R.color.colorRed));

        }
        break;
      }
      timelineView.setMarker(icon);
    }

    void removeTransaction(){
      SweetAlertDialog dialog = new SweetAlertDialog( itemView.getContext(), SweetAlertDialog.WARNING_TYPE);
      dialog.setTitleText("Alerta")
              .setContentText("Se eliminará la transacción\n¿Desea continuar?")
              .setConfirmText("Sí")
              .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    //Toast.makeText(view.getContext(),"Position: " + list.get(position),Toast.LENGTH_SHORT).show();
                  //list.remove(position);
/*                  notifyItemRemoved(position);
                  notifyItemRangeRemoved(position, list.size());
                  notifyDataSetChanged();
 */
                  realm.beginTransaction();
                  SaleRO infoData = list.get(getAdapterPosition());
                  infoData.deleteFromRealm();
                  realm.commitTransaction();
                  //notifyItemRemoved(getAdapterPosition());
                  notifyItemRemoved(position);
                  notifyItemRangeRemoved(position, list.size());
                  notifyDataSetChanged();
                  sweetAlertDialog.dismiss();
                }
              })
              .setCancelText("No")
              .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                  sweetAlertDialog.dismiss();
                }
              }).show();
    }

    @Override
    public void onClick(final View view) {
      switch (view.getId()) {
        case R.id.cvContent:
          String[] options = {"Imprimir ticket"};
          AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext())
                  .setTitle("Opciones")
                  .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      switch (i) {
                        case 0: {
                          Intent intent = new Intent(view.getContext(), PrintTicketActivity.class);
                          intent.putExtra("ticket", list.get(getLayoutPosition()).getTicket());
                          view.getContext().startActivity(intent);
                        }
                        break;
                      }

                      dialogInterface.dismiss();
                    }
                  })
                  .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      dialogInterface.dismiss();
                    }
                  });

          dialog.create()
                  .show();
          break;


        case R.id.img_delete: {
            removeTransaction();
        }
        break;
      }
    }
  }
}