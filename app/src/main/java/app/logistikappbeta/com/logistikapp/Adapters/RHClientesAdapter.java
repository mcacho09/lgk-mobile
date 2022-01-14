package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Waybill;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danie on 20/07/2016.
 */
public class RHClientesAdapter extends RecyclerView.Adapter<RHClientesAdapter.VHRHClientes> {

  private ArrayList<Waybill> list;
  private Context context;

  public RHClientesAdapter(ArrayList<Waybill> list, Context context) {
    this.list = list;
    this.context = context;
  }

  @Override
  public VHRHClientes onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_rhclientes, parent, false);
    return new VHRHClientes(view);
  }

  @Override
  public void onBindViewHolder(VHRHClientes holder, int position) {
    Waybill waybill = list.get(position);

    holder.setDataStore(waybill);

  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  protected class VHRHClientes extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.imgStar)
    ImageView imgStar;
    @BindView(R.id.tvNombre)
    TextView tvNombre;
    @BindView(R.id.imgVisitado)
    ImageView imgVisitado;
    @BindView(R.id.tvVisitado)
    TextView tvVisitado;
    @BindView(R.id.tvFueraRango)
    TextView tvFueraRango;
    @BindView(R.id.imgIco)
    ImageView imgIco;
    @BindView(R.id.tvNoImg)
    TextView tvNoImg;
    @BindView(R.id.imgComentarios)
    ImageView imgComentarios;
    @BindView(R.id.tvNoComentarios)
    TextView tvNoComentarios;
    @BindView(R.id.imgCheck)
    CircularImageView imgCheck;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvId)
    TextView tvId;

    public VHRHClientes(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      String[] options = new String[]{"Comentarios e imágenes"};
    }

    public void setDataStore(Waybill s) {

      imgStar.setVisibility(s.getShelf() != null && s.getShelf().equals("S") ? View.VISIBLE : View.GONE);
      tvNombre.setText(s.getName());
      if (s.getStatus().equals("S")) {
        imgVisitado.setColorFilter(context.getResources().getColor(R.color.colorGreen));
        tvVisitado.setText("VISITADO");

        if (s.getCheckin() != null) {
          SimpleDateFormat sdfIn = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
          SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM HH:mm");
          try {
            tvVisitado.append(" " + sdfOut.format(sdfIn.parse(s.getCheckin())));
          } catch (ParseException ex) {
            Log.e("Logistikapp", "ParseException:\n" + ex.getMessage());
          }
        }

        imgCheck.setImageResource(R.drawable.ic_check_circle);
        if (s.getOutrange() != null && s.getOutrange().equals("S")) {
          tvFueraRango.setVisibility(View.VISIBLE);
          imgCheck.setBorderColor(context.getResources().getColor(R.color.colorOrange));
        } else {
          imgCheck.setBorderColor(context.getResources().getColor(R.color.colorGreen));
          tvFueraRango.setVisibility(View.GONE);
        }

        imgCheck.setColorFilter(context.getResources().getColor(R.color.bgCards));

      } else {
        imgVisitado.setColorFilter(context.getResources().getColor(R.color.colorGray));
        tvVisitado.setText("NO VISITADO");

        imgCheck.setBorderColor(context.getResources().getColor(R.color.colorGray));
        imgCheck.setImageResource(R.drawable.ic_place);
        imgCheck.setColorFilter(context.getResources().getColor(android.R.color.white));

        tvFueraRango.setVisibility(View.GONE);
      }

      if (s.getImages() > 0) {
        imgIco.setColorFilter(context.getResources().getColor(R.color.colorBlue));
        tvNoImg.setText("" + s.getImages());
      } else {
        imgIco.setColorFilter(context.getResources().getColor(R.color.colorGray));
        tvNoImg.setText("0");
      }

      if (s.getComments() > 0) {
        imgComentarios.setColorFilter(context.getResources().getColor(R.color.colorBlue));
        tvNoComentarios.setText("" + s.getComments());
      } else {
        imgComentarios.setColorFilter(context.getResources().getColor(R.color.colorGray));
        tvNoComentarios.setText("0");
      }

      tvStatus.setText(s.getStatus());
      tvId.setText("" + s.getWid());

    }
  }

}
