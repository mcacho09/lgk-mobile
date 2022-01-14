package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.CustomersActivity;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Retail;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danie on 21/12/2016.
 */

public class RetailsAdapter extends RecyclerView.Adapter<RetailsAdapter.VHRoutesAdapter> {

  private ArrayList<Retail> list;
  private Context context;
  private Login login;

  public RetailsAdapter(ArrayList<Retail> list, Context context) {
    this.list = list;
    this.context = context;
    this.login = Utils.getSessionInfo(context);
  }

  @Override
  public VHRoutesAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_retails, parent, false);
    return new VHRoutesAdapter(v);
  }

  @Override
  public void onBindViewHolder(VHRoutesAdapter holder, int position) {
    holder.setRoute(list.get(position));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  protected class VHRoutesAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvNoCustomers)
    TextView tvNoCustomers;

    public VHRoutesAdapter(View v) {
      super(v);
      ButterKnife.bind(this, v);
      if (login.getProfile().startsWith("SUP") || login.getSuperuser().contentEquals("S")) {
        v.setOnClickListener(this);
      }
    }

    public void setRoute(Retail r) {
      tvName.setText(r.getName());
      tvNoCustomers.setVisibility(login.getProfile().startsWith("SUP")?View.VISIBLE:View.GONE);
      tvNoCustomers.setText(r.getNo_clients().toString());
    }

    @Override
    public void onClick(View view) {
      Long idr = list.get(getLayoutPosition()).getId();
      Intent intent = new Intent(context, CustomersActivity.class);
      intent.putExtra("idr", idr);
      context.startActivity(intent);
    }
  }

}
