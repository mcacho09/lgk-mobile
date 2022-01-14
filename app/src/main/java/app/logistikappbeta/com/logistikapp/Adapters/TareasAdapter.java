package app.logistikappbeta.com.logistikapp.Adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Task;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danie on 17/07/2016.
 */
public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.VHTarea> {

    private ArrayList<Task> list;

    public TareasAdapter(ArrayList<Task> list) {
        this.list = list;
    }

    @Override
    public VHTarea onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_task, parent, false);
        return new VHTarea(v);
    }

    @Override
    public void onBindViewHolder(VHTarea holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class VHTarea extends RecyclerView.ViewHolder{

        @BindView(R.id.imgCheck) ImageView imgCheck;
        @BindView(R.id.tvTareaDesc) TextView tvTareaDesc;
        @BindView(R.id.ibtnDelete) ImageButton ibtnDelete;
        @BindView(R.id.tvIdTask) TextView tvIdTask;

        public VHTarea(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.imgCheck)
        public void onClick(View v){
            imgCheck.setImageResource(R.drawable.ic_check_box);
        }

    }
}
