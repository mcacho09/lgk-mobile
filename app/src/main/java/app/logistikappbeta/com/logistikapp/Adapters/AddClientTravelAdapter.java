package app.logistikappbeta.com.logistikapp.Adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.logistikappbeta.com.logistikapp.POJOs.AddClient;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danie on 25/08/2016.
 */
public class AddClientTravelAdapter extends RecyclerView.Adapter<AddClientTravelAdapter.VHAddClientTravel> {

    private List<AddClient> list;

    public AddClientTravelAdapter(List<AddClient> list) {
        this.list = list;
    }

    @Override
    public VHAddClientTravel onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_add_client_travel, parent, false);
        return new VHAddClientTravel(v);
    }

    @Override
    public void onBindViewHolder(VHAddClientTravel holder, int position) {
        AddClient addClient = list.get(position);
        holder.setData(addClient);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    protected class VHAddClientTravel extends RecyclerView.ViewHolder {

        @BindView(R.id.chAdd)
        CheckBox chAdd;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.ivShelf)
        ImageView ivShelf;

        public VHAddClientTravel(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(AddClient ac){
            chAdd.setChecked(ac.getSelected());
            tvName.setText(ac.getName());
            if (ac.getShelf() != null && ac.getShelf().equals("S")){
                ivShelf.setVisibility(View.VISIBLE);
            }else {
                ivShelf.setVisibility(View.GONE);
            }
        }

        @OnClick(R.id.chAdd)
        public void OnClick(View v){
            Log.d("AQUI", "Es " + chAdd.isChecked());
            AddClient addClient = list.get(getLayoutPosition());
            addClient.setSelected(chAdd.isChecked());
            list.remove(getLayoutPosition());
            list.add(getLayoutPosition(), addClient);
        }

    }

}
