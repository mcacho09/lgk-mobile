package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.ConversacionActivity;
import app.logistikappbeta.com.logistikapp.POJOs.MessageList;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danie on 18/07/2016.
 */
public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.VHMensajes> {

    private ArrayList<MessageList> list;

    public MensajesAdapter(ArrayList<MessageList> list) {
        this.list = list;
    }

    @Override
    public VHMensajes onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_mensajes, parent, false);
        return new VHMensajes(v);
    }

    @Override
    public void onBindViewHolder(VHMensajes holder, int position) {
        MessageList m = list.get(position);
        holder.setData(m);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class VHMensajes extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvRemitente)
        TextView tvRemitente;
        @BindView(R.id.tvMensaje)
        TextView tvMensaje;
        @BindView(R.id.ivReaded)
        ImageView ivReaded;

        public VHMensajes(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(), ConversacionActivity.class);
            i.putExtra("name", list.get(getLayoutPosition()).getUsername());
            i.putExtra("cId", list.get(getLayoutPosition()).getId_user_message());
            i.putExtra("idUser", list.get(getLayoutPosition()).getId_user_to());
            view.getContext().startActivity(i);
        }

        public void setData(MessageList m) {
            tvDate.setText(m.getSince());
            tvRemitente.setText(m.getUsername());
            tvMensaje.setText(m.getMessage());
            if (m.getRead().equals("S")) {
                ivReaded.setVisibility(View.GONE);
            } else {
                ivReaded.setVisibility(View.VISIBLE);
            }
        }
    }

}
