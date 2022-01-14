package app.logistikappbeta.com.logistikapp.Fragments.TransactionDetailTabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.TDDosAdapter;
import app.logistikappbeta.com.logistikapp.POJOs.ProductDetail;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TDDos extends Fragment {

  @BindView(R.id.tvQty)
  TextView tvQty;
  @BindView(R.id.rvListSale)
  RecyclerView rvListSale;
  @BindView(R.id.rvListChange)
  RecyclerView rvListChange;
  @BindView(R.id.rvListDevolution)
  RecyclerView rvListDevolution;
  @BindView(R.id.cvSale)
  CardView cvSale;
  @BindView(R.id.cvChg)
  CardView cvChg;
  @BindView(R.id.cvDev)
  CardView cvDev;
  @BindView(R.id.tvQtyDev)
  TextView tvQtyDev;
  @BindView(R.id.lay_tot_ticket)
  LinearLayout layTotTicket;
  @BindView(R.id.tvTotVta)
  TextView tvTotVta;

  ArrayList<ProductDetail> sale;
  ArrayList<ProductDetail> dev;
  ArrayList<ProductDetail> chg;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_tddos, container, false);
    ButterKnife.bind(this, v);

    DecimalFormat df = new DecimalFormat("0.00");

    Bundle bundle = getArguments();
    tvQty.setText(df.format(bundle.getDouble("sale")));
    tvQtyDev.setText(df.format(bundle.getDouble("sale_dev")));

    if (bundle.getDouble("sale_dev") == 0d || bundle.getDouble("sale") == 0d) {
      layTotTicket.setVisibility(View.GONE);
    } else {
      tvTotVta.append(df.format(bundle.getDouble("sale") - bundle.getDouble("sale_dev")));
    }

    rvListSale.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvListChange.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvListDevolution.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    rvListSale.setItemAnimator(new DefaultItemAnimator());
    rvListChange.setItemAnimator(new DefaultItemAnimator());
    rvListDevolution.setItemAnimator(new DefaultItemAnimator());

    rvListSale.setHasFixedSize(true);
    rvListSale.setHasFixedSize(true);
    rvListDevolution.setHasFixedSize(true);

    sale = new ArrayList<>();
    chg = new ArrayList<>();
    dev = new ArrayList<>();

    rvListSale.setAdapter(new TDDosAdapter(true, sale));
    rvListChange.setAdapter(new TDDosAdapter(false, chg));
    rvListDevolution.setAdapter(new TDDosAdapter(false, dev));

    String saleCont = getArguments().getString("vtalist");
    String devCont = getArguments().getString("devlist");
    String chgCont = getArguments().getString("chglist");

    Log.d("PVDOSCHG", chgCont);


    new LoadListAsyncTask(sale, rvListSale, cvSale).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, saleCont);
    new LoadListAsyncTask(dev, rvListDevolution, cvDev).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, devCont);
    new LoadListAsyncTask(chg, rvListChange, cvChg).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, chgCont);

    return v;
  }

  protected class LoadListAsyncTask extends AsyncTask<String, Integer, ArrayList<ProductDetail>> {

    private ArrayList<ProductDetail> data;
    private RecyclerView rvList;
    private CardView cvRes;

    public LoadListAsyncTask(ArrayList<ProductDetail> data, RecyclerView rvList, CardView cvRes) {
      this.data = data;
      this.rvList = rvList;
      this.cvRes = cvRes;
    }

    @Override
    protected ArrayList<ProductDetail> doInBackground(String... strings) {

      if (strings.length > 0) {
        String content = strings[0];
        if (content != null) {

          Gson gson = new Gson();
          ArrayList<ProductDetail> list = gson.fromJson(content, new TypeToken<ArrayList<ProductDetail>>() {
          }.getType());

          return list;

        }
      }
      return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ProductDetail> productDetails) {
      super.onPostExecute(productDetails);
      if (productDetails != null) {
        if (productDetails.size() > 0) {
          cvRes.setVisibility(View.GONE);
        }
        data.addAll(productDetails);
        rvList.getAdapter().notifyDataSetChanged();

      }
    }
  }
}
