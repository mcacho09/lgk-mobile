package app.logistikappbeta.com.logistikapp.Adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Conversacion;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danie on 18/07/2016.
 */
public class ConversacionAdapter extends RecyclerView.Adapter<ConversacionAdapter.VHConversacion> {

    private ArrayList<Conversacion> list;

    public ConversacionAdapter(ArrayList<Conversacion> list) {
        this.list = list;
    }

    @Override
    public VHConversacion onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_conversacion,parent,false);
        return new VHConversacion(v);
    }

    @Override
    public void onBindViewHolder(VHConversacion holder, int position) {
        Conversacion c = list.get(position);
        holder.setData(c);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class VHConversacion extends RecyclerView.ViewHolder{

        @BindView(R.id.card)
        CardView card;
        @BindView(R.id.tvRemitente)
        TextView tvRemitente;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvMensaje)
        TextView tvMensaje;
        @BindView(R.id.vLeft)
        View vLeft;
        @BindView(R.id.vRight)
        View vRight;

        public VHConversacion(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Conversacion c) {

            if (c.getSend().equals("S")) {
                vLeft.setVisibility(View.VISIBLE);
                vRight.setVisibility(View.GONE);
                card.setCardBackgroundColor(itemView.getResources().getColor(R.color.bgCardsMessage));
                tvRemitente.setTextColor(itemView.getResources().getColor(android.R.color.white));
                tvMensaje.setTextColor(itemView.getResources().getColor(android.R.color.white));
            } else {
                vRight.setVisibility(View.VISIBLE);
                vLeft.setVisibility(View.GONE);
                card.setCardBackgroundColor(itemView.getResources().getColor(R.color.bgCards));
                tvRemitente.setTextColor(itemView.getResources().getColor(android.R.color.black));
                tvMensaje.setTextColor(itemView.getResources().getColor(android.R.color.black));
            }

            tvRemitente.setText(c.getUsername());
            tvMensaje.setText(c.getMessage());
            tvDate.setText(c.getCreated());
        }
    }
}
