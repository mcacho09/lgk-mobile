package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.ConversacionActivity;
import app.logistikappbeta.com.logistikapp.POJOs.UserChat;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danie on 28/08/2016.
 */
public class NuevoMensajeAdapter extends RecyclerView.Adapter<NuevoMensajeAdapter.VHNuevoMensaje> {

    private ArrayList<UserChat> list;
    private AppCompatActivity context;

    public NuevoMensajeAdapter(ArrayList<UserChat> list, AppCompatActivity context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public VHNuevoMensaje onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_nuevo_mensaje, parent, false);
        return new VHNuevoMensaje(v);
    }

    @Override
    public void onBindViewHolder(VHNuevoMensaje holder, int position) {
        UserChat u = list.get(position);
        holder.setData(u);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class VHNuevoMensaje extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvUser)
        TextView tvUser;

        public VHNuevoMensaje(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void setData(UserChat u) {
            this.tvUser.setText(u.getUsername());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ConversacionActivity.class);
            intent.putExtra("name", list.get(getLayoutPosition()).getUsername());
            intent.putExtra("idUser", list.get(getLayoutPosition()).getId_user());
            view.getContext().startActivity(intent);
            context.finish();
        }
    }
}
