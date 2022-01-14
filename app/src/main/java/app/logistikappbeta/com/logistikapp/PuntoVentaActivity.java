package app.logistikappbeta.com.logistikapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Fragments.PuntoVentaTabs.PVCuatro;
import app.logistikappbeta.com.logistikapp.Fragments.PuntoVentaTabs.PVDos;
import app.logistikappbeta.com.logistikapp.Fragments.PuntoVentaTabs.PVTres;
import app.logistikappbeta.com.logistikapp.Fragments.PuntoVentaTabs.PVUno;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.OrderDetail;
import app.logistikappbeta.com.logistikapp.POJOs.ProductPV;
import app.logistikappbeta.com.logistikapp.Params.GetProductsParam;
import app.logistikappbeta.com.logistikapp.Results.ProductsResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PuntoVentaActivity extends AppCompatActivity implements View.OnClickListener,
        TabLayout.OnTabSelectedListener, SearchView.OnQueryTextListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String storeName;
    private int idS;

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.imgIco)
    ImageView imgIcon;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvStore)
    TextView tvStore;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.btnShow)
    Button btnShow;

    SearchView searchView;
    MenuItem itemSearch;

    PVUno pvuno;
    PVDos pvdos;
    PVTres pvtres;
    PVCuatro pvcuatro;

    ArrayList<ProductPV> data;
    GetProductsParam param;

    public static ArrayList<String> products;
    String retail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punto_venta);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Punto de Venta");

        toolbar.setNavigationOnClickListener(this);

        data = new ArrayList<>();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        pvuno = new PVUno();
        pvdos = new PVDos();
        pvtres = new PVTres();
        pvcuatro = new PVCuatro();

        idS = getIntent().getIntExtra("id", 0);
        retail = getIntent().getStringExtra("retail");

        Bundle bundle = new Bundle();
        bundle.putInt("idS", idS);
        pvcuatro.setArguments(bundle);

        param = new Gson().fromJson(getIntent().getStringExtra("params"), GetProductsParam.class);

        mSectionsPagerAdapter.addFragment(pvuno);
        mSectionsPagerAdapter.addFragment(pvdos);
        mSectionsPagerAdapter.addFragment(pvtres);

        mSectionsPagerAdapter.addFragment(pvcuatro);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_attach_money);
        tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorBlue), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_compare_arrows);
        tabLayout.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorGray), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_cancel);
        tabLayout.getTabAt(2).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorGray), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_restore);
        tabLayout.getTabAt(3).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorGray), PorterDuff.Mode.SRC_IN);

        tabLayout.addOnTabSelectedListener(this);

        storeName = getIntent().getStringExtra("store");
        tvStore.setText(storeName);
        this.reset();
    }

    @Override
    protected void onResume() {
        //this.offlineData();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_punto_venta, menu);
        itemSearch = menu.findItem(R.id.iSearch);
        searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
        searchView.setQueryHint("Búsqueda...");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            MenuItemCompat.collapseActionView(itemSearch);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.getIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorBlue), PorterDuff.Mode.SRC_IN);
        switch (tab.getPosition()) {
            case 0: {
                btnSave.setVisibility(View.VISIBLE);
                btnShow.setVisibility(View.VISIBLE);
                imgIcon.setImageResource(R.drawable.ic_attach_money);
                tvTitle.setText("Venta");
            }
            break;
            case 1: {
                btnSave.setVisibility(View.INVISIBLE);
                btnShow.setVisibility(View.INVISIBLE);
                imgIcon.setImageResource(R.drawable.ic_compare_arrows);
                tvTitle.setText("Cambio");
            }
            break;
            case 2: {
                btnSave.setVisibility(View.INVISIBLE);
                btnShow.setVisibility(View.INVISIBLE);
                imgIcon.setImageResource(R.drawable.ic_cancel);
                tvTitle.setText("Devolución");
            }
            break;
            case 3: {
                btnSave.setVisibility(View.INVISIBLE);
                btnShow.setVisibility(View.INVISIBLE);
                imgIcon.setImageResource(R.drawable.ic_compare_arrows);
                tvTitle.setText("Últimas Transacciones");
            }
            break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        tab.getIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorGray), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (this.products == null) return false;

        this.pvuno.search(query);
        this.pvdos.search(query);
        this.pvtres.search(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (this.products == null) return false;

        this.pvuno.search(newText);
        this.pvdos.search(newText);
        this.pvtres.search(newText);
        if (!searchView.isIconified()) {
            searchView.setIconified(false);
        }
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> list;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            list = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        public void addFragment(Fragment fragment) {
            list.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }


    @OnClick({R.id.btnProcess, R.id.btnReset, R.id.btnCalc, R.id.btnSave, R.id.btnShow})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btnProcess: {
                SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
                dialog.setTitleText("Procesando transacción")
                        .setContentText("Espere un momento")
                        .setCancelable(false);
                dialog.show();

                Gson gson = new Gson();
                String jsonuno = gson.toJson(pvuno.getData());
                String jsondos = gson.toJson(pvdos.getData());
                String jsontres = gson.toJson(pvtres.getData());

                ArrayList<OrderDetail> vtaList = new ArrayList<>();
                ArrayList<OrderDetail> devList = new ArrayList<>();
                ArrayList<OrderDetail> chgList = new ArrayList<>();

                OrderDetail od = null;

                try {
                    JSONArray jsonArray = new JSONArray(jsonuno);
                    int size = jsonArray.length();
                    for (int i = 0; i < size; i++) {
                        ProductPV pv = gson.fromJson(jsonArray.getString(i), ProductPV.class);

                        if (pv.getQtyproducts() > 0) {
                            od = new OrderDetail();
                            od.setPrice_sale(pv.getPrice_sale());
                            od.setId_product(pv.getId_product());
                            od.setPrice_sug(pv.getPrice_sug());
                            od.setQuantity(pv.getQtyproducts());
                            od.setType(pv.getProduct_type_temp());

                            vtaList.add(od);
                        }
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                try {
                    JSONArray jsonArray = new JSONArray(jsondos);
                    int size = jsonArray.length();
                    for (int i = 0; i < size; i++) {
                        ProductPV pv = gson.fromJson(jsonArray.getString(i), ProductPV.class);

                        if (pv.getQtyproducts() > 0) {
                            od = new OrderDetail();
                            od.setPrice_sale(pv.getPrice_sale());
                            od.setId_product(pv.getId_product());
                            od.setPrice_sug(pv.getPrice_sug());
                            od.setQuantity(pv.getQtyproducts());
                            od.setType(pv.getProduct_type_temp());

                            chgList.add(od);
                        }
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                try {
                    JSONArray jsonArray = new JSONArray(jsontres);
                    int size = jsonArray.length();
                    for (int i = 0; i < size; i++) {
                        ProductPV pv = gson.fromJson(jsonArray.getString(i), ProductPV.class);

                        if (pv.getQtyproducts() > 0) {
                            od = new OrderDetail();
                            od.setPrice_sale(pv.getPrice_sale());
                            od.setId_product(pv.getId_product());
                            od.setPrice_sug(pv.getPrice_sug());
                            od.setQuantity(pv.getQtyproducts());
                            od.setType(pv.getProduct_type_temp());

                            devList.add(od);
                        }
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                if (vtaList.size() > 0 || chgList.size() > 0 || devList.size() > 0) {
                    Intent intent = new Intent(PuntoVentaActivity.this, ProcessSaleActivity.class);
                    String vta = gson.toJson(vtaList);
                    Log.i("VTA", vta);
                    String chg = gson.toJson(chgList);
                    Log.i("CHG", chg);
                    String dev = gson.toJson(devList);
                    Log.i("DEV", dev);
                    intent.putExtra("vta", vta);
                    intent.putExtra("chg", chg);
                    intent.putExtra("dev", dev);
                    intent.putExtra("store", storeName);
                    intent.putExtra("idS", idS);
                    intent.putExtra("retail", retail);
                    intent.putExtra("pvta", jsonuno);
                    intent.putExtra("pchg", jsondos);
                    intent.putExtra("pdev", jsontres);

                    Bundle bundle = getIntent().getExtras();

                    if (bundle.containsKey("wid")) {
                        intent.putExtra("wid", bundle.getInt("wid"));
                        intent.putExtra("distance", bundle.getString("distance"));
                        Log.d("LGK", "Tiene WID");
                    }

                    if (bundle.containsKey("lat") && bundle.containsKey("lng")) {
                        intent.putExtra("lat", bundle.getDouble("lat"));
                        intent.putExtra("lng", bundle.getDouble("lng"));
                        Log.d("LGK", "Tiene lat and lng");
                    }

                    startActivityForResult(intent, 1);
                    //finish();
                } else {
                    Toast.makeText(PuntoVentaActivity.this, "No se puede procesar la transacción por el momento", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
            break;
            case R.id.btnReset: {
                this.reset();
            }
            break;
            case R.id.btnSave: {
                this.savePreferences();
            }
            break;
            case R.id.btnShow: {
                this.loadOfflineProducts();
            }
            break;
            case R.id.btnCalc: {
                SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
                dialog.setTitleText("Calculando")
                        .setContentText("Espere un momento")
                        .setCancelable(false);
                dialog.show();

                Double total = 0d;

                for (ProductPV p : pvuno.getData()) {
                    total += (p.getPrice_sale() * p.getQtyproducts());
                }

                dialog.changeAlertType(SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("Total")
                        .setContentText("$" + total);
            }
            break;
        }
    }


    private void savePreferences() {
        if(data == null){
            data = new ArrayList<>();
            Toast.makeText(PuntoVentaActivity.this, "No hay datos para mostrar",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        prefsEditor.putString("MyObject", json);
        prefsEditor.commit();
        Toast.makeText(PuntoVentaActivity.this, "¡Productos guardados exitosamente!",
                Toast.LENGTH_SHORT).show();
    }


    private void loadOfflineProducts(){
        SharedPreferences sharedPreferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("MyObject", null);
        Type type = new TypeToken<ArrayList<ProductPV>>() {}.getType();
        data = gson.fromJson(json, type);

        if(data == null){
            data = new ArrayList<>();
            Toast.makeText(PuntoVentaActivity.this, "No hay datos para mostrar",
                    Toast.LENGTH_SHORT).show();
        }else{
            try {
                setList();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }


    // Mod offline, save some products on cellphone using realm
    // Erick Gtz
    public void reset() {
        if (data != null && data.size() == 0) {
            Call<ProductsResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getProducts(param);
            call.enqueue(new Callback<ProductsResult>() {
                @Override
                public void onResponse(Call<ProductsResult> call, Response<ProductsResult> response) {
                    data.clear();
                    if (!response.isSuccessful()) {
                        Toast.makeText(PuntoVentaActivity.this, "No se pudo obtener información para realizar la venta, revise su información y vuelta a intentar", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ProductsResult result = response.body();

                    if (result.getStatus() == 0) {

                        products = new ArrayList<String>();

                        for (ProductPV pv : result.getProducts()) {
                            pv.setQtyproducts(0l);
                            pv.setPrice_sale(pv.getPrice_sug());
                            pv.setSale(0d);
                            pv.setProduct_type_temp(pv.getProduct_type());
                            products.add(pv.getName_short());
                        }

                        data.addAll(result.getProducts());

                    } else {
                        Toast.makeText(PuntoVentaActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
                    }

                    try {
                        setList();
                    } catch (CloneNotSupportedException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ProductsResult> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(PuntoVentaActivity.this, "No se pudo iniciar la venta\nIntente más tarde", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            try {
                setList();
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            finish();
        }

    }

    public void setList() throws CloneNotSupportedException {
        ArrayList<ProductPV> listUno = new ArrayList<>();
        for (ProductPV p : data) {
            listUno.add(p.clone());
        }
        pvuno.reset(listUno);

        ArrayList<ProductPV> listDos = new ArrayList<>();
        for (ProductPV p : data) {
            listDos.add(p.clone());
        }
        pvdos.reset(listDos);

        ArrayList<ProductPV> listTres = new ArrayList<>();
        for (ProductPV p : data) {
            listTres.add(p.clone());
        }
        pvtres.reset(listTres);

    }
}
