package app.logistikappbeta.com.logistikapp.Adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Notification;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danie on 17/07/2016.
 */
public class NotificacionesAdapter  extends RecyclerView.Adapter<NotificacionesAdapter.VHNotificacion> {

    private ArrayList<Notification> list;

    public NotificacionesAdapter(ArrayList<Notification> list) {
        this.list = list;
    }

    @Override
    public VHNotificacion onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_notificacion, parent, false);
        return new VHNotificacion(v);
    }

    @Override
    public void onBindViewHolder(VHNotificacion holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class VHNotificacion extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDesc)
        TextView tvDesc;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvResp)
        TextView tvResp;

        public VHNotificacion(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Notification n){
            tvDate.setText(n.getCreated());
            tvDesc.setText(Html.fromHtml(n.getMessage()));
            tvResp.setText(n.getUsername());
        }

    }
}
