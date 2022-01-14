package app.logistikappbeta.com.logistikapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import app.logistikappbeta.com.logistikapp.Adapters.TransactionListAdapter;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.OrderDetail;
import app.logistikappbeta.com.logistikapp.POJOs.ProductPV;
import app.logistikappbeta.com.logistikapp.POJOs.TransactionList;
import app.logistikappbeta.com.logistikapp.Params.AddOrderParam;
import app.logistikappbeta.com.logistikapp.Params.CheckinParam;
import app.logistikappbeta.com.logistikapp.Params.GetSaleByDriParam;
import app.logistikappbeta.com.logistikapp.Params.SendTicketParam;
import app.logistikappbeta.com.logistikapp.RealmObjects.SaleRO;
import app.logistikappbeta.com.logistikapp.Results.AddOrderResult;
import app.logistikappbeta.com.logistikapp.Results.Result;
import app.logistikappbeta.com.logistikapp.Results.SendTicketResult;
import app.logistikappbeta.com.logistikapp.Results.TransactionListResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Modified by Erick Gtz on 15/04/2020.
 * Checkin implementation on sale completed
 */

public class ProcessSaleActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvStore)
    TextView tvStore;
    @BindView(R.id.tvQtySale)
    TextView tvQtySale;
    @BindView(R.id.tvSale)
    TextView tvSale;
    @BindView(R.id.tvDev)
    TextView tvDev;
    @BindView(R.id.tvQtyChg)
    TextView tvQtyChg;
    @BindView(R.id.tvQtyDev)
    TextView tvQtyDev;
    @BindView(R.id.btnStatus)
    Button btnStatus;
    @BindView(R.id.btnProcess)
    Button btnProcess;
    @BindView(R.id.rvList)
    RecyclerView rvList;

    ArrayList<OrderDetail> vtaList = null;
    ArrayList<OrderDetail> devList = null;
    ArrayList<OrderDetail> chgList = null;

    ArrayList<TransactionList> datos;

    Long qtySale = 0l;
    Long qtyChg = 0l;
    Long qtyDev = 0l;
    Double sale = 0d;
    Double devSale = 0d;
    Gson gson;
    String vta;
    String chg;
    String dev;

    String pvta;
    String pchg;
    String pdev;

    ArrayList<ProductPV> pvtaList;
    ArrayList<ProductPV> pchgList;
    ArrayList<ProductPV> pdevList;

    int indexStatus = 0;

    Login login;
    BigInteger token;

    BottomSheetBehavior bsb;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_sale);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Punto de Venta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(this);

        login = Utils.getSessionInfo(this);
        token = new BigInteger(Utils.getSaveToken(this));

        String store = getIntent().getStringExtra("store");
        tvStore.setText(store);

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
        dialog.setTitleText("Detallando transacción")
                .setContentText("Espere un momento")
                .setCancelable(false);
        dialog.show();

        vta = getIntent().getStringExtra("vta");
        Log.d("VTA", vta);
        chg = getIntent().getStringExtra("chg");
        Log.d("CHG", chg);
        dev = getIntent().getStringExtra("dev");
        Log.d("DEV", dev);

        pvta = getIntent().getStringExtra("pvta");
        Log.d("VTA", pvta);
        pchg = getIntent().getStringExtra("pchg");
        Log.d("CHG", pchg);
        pdev = getIntent().getStringExtra("pdev");
        Log.d("DEV", pdev);

        gson = new Gson();

        pvtaList = gson.fromJson(pvta, new TypeToken<ArrayList<ProductPV>>() {
        }.getType());
        pchgList = gson.fromJson(pchg, new TypeToken<ArrayList<ProductPV>>() {
        }.getType());
        pdevList = gson.fromJson(pdev, new TypeToken<ArrayList<ProductPV>>() {
        }.getType());

        OrderDetail od;

        try {
            JSONArray jsonArray = new JSONArray(vta);
            int size = jsonArray.length();
            vtaList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                od = gson.fromJson(jsonArray.getString(i), OrderDetail.class);
                vtaList.add(od);
                qtySale += od.getQuantity();
                sale += (od.getPrice_sale() * od.getQuantity());
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(chg);
            int size = jsonArray.length();
            chgList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                od = gson.fromJson(jsonArray.getString(i), OrderDetail.class);
                chgList.add(od);
                qtyChg += od.getQuantity();
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(dev);
            int size = jsonArray.length();
            devList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                od = gson.fromJson(jsonArray.getString(i), OrderDetail.class);
                devList.add(od);
                qtyDev += od.getQuantity();
                devSale += (od.getPrice_sale() * od.getQuantity());
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        dialog.dismiss();
        DecimalFormat df = new DecimalFormat("0.00");
        tvQtySale.setText("" + qtySale);
        tvSale.setText("$" + df.format(sale));
        tvDev.setText("$" + df.format(devSale));
        tvQtyChg.setText("" + qtyChg);
        tvQtyDev.setText("" + qtyDev);

        bsb = BottomSheetBehavior.from(findViewById(R.id.bottomSheet));
        datos = new ArrayList<>();
        rvList.setHasFixedSize(true);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.setAdapter(new TransactionListAdapter(datos, this));

    }

    @OnClick({R.id.btnCancelar, R.id.btnProcess, R.id.btnStatus, R.id.btnCloseBs})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelar: {
                cancelTrx();
            }
            break;
            case R.id.btnProcess: {
                //btnProcess.setEnabled(false);
                Intent intent = getIntent();
                Integer idS = intent.getIntExtra("idS", 0);

                AddOrderParam addOrderParam = new AddOrderParam();
                addOrderParam.setStid(idS.longValue());
                addOrderParam.setUid(login.getId());
                addOrderParam.setStatus(getStatus(indexStatus));
                addOrderParam.setToken(token);
                addOrderParam.setProducts_VTA(vtaList);
                addOrderParam.setProducts_DEV(devList);
                addOrderParam.setProducts_CHG(chgList);

                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    if (bundle.containsKey("wid")) {
                        addOrderParam.setWid((long) bundle.getInt("wid"));
                        Log.d("LGK", "Tiene WID");

                        if (bundle.containsKey("lat") && bundle.containsKey("lng")) {
                            addOrderParam.setLatitude(bundle.getDouble("lat"));
                            addOrderParam.setLongitude(bundle.getDouble("lng"));
                            Log.d("LGK", "Tiene lat and lng");
                        }
                    }
                }

                final SweetAlertDialog progress = new SweetAlertDialog(ProcessSaleActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                progress.getProgressHelper().setBarColor(ContextCompat.getColor(ProcessSaleActivity.this, R.color.colorGreen));
                progress.setTitleText("Procesando transacción")
                        .setCancelable(false);
                progress.show();

                Call<AddOrderResult> call = Utils.buildRetrofit(this).create(WSInterface.class).addOrder(addOrderParam);
                call.enqueue(new Callback<AddOrderResult>() {
                    @Override
                    public void onResponse(Call<AddOrderResult> call, Response<AddOrderResult> response) {
                        progress.dismiss();
                        if (response.isSuccessful()) {
                            final AddOrderResult result = response.body();
                            if (result.getStatus() == 0) {
                                progress.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                progress.setTitleText("Transacción exitosa")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            if (result.getId_order() > 0L) {
                                                progress.dismiss();
                                                String distance = intent.getExtras().getString("distance");
                                                String checkInOutRange = (Float.valueOf(distance) < 50) ? "N" : "S";
                                                CheckinParam param = new CheckinParam();
                                                param.setUid(login.getId());
                                                param.setToken(String.valueOf(token));
                                                param.setWid(bundle.getInt("wid"));
                                                SimpleDateFormat ese = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                                param.setDate(ese.format(new Date()));
                                                param.setLatitude(bundle.getDouble("lat"));
                                                param.setOutrange(checkInOutRange);
                                                param.setLongitude(bundle.getDouble("lng"));

                                                Call<Result> call = Utils.buildRetrofit(getApplicationContext()).create(WSInterface.class).checkin(param);
                                                call.enqueue(new Callback<Result>() {
                                                    @Override
                                                    public void onResponse(Call<Result> call, Response<Result> response) {
                                                        Result result = response.body();
                                                        if (result.getStatus() == 0) {
                                                            return;
                                                        } else {
                                                            Toast.makeText(ProcessSaleActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
                                                            progress.dismiss();
                                                        }
                                                        Toast.makeText(ProcessSaleActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
                                                        progress.dismiss();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Result> call, Throwable t) {
                                                        t.printStackTrace();
                                                        progress.dismiss();
                                                    }
                                                });
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(ProcessSaleActivity.this);
                                                dialog.setTitle("Ticket")
                                                    .setItems(new String[]{"Imprimirlo", "Enviar por correo", "Volver"}, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            switch (i) {
                                                                case 0: {
                                                                    Intent intent = new Intent(ProcessSaleActivity.this, PrintTicketActivity.class);
                                                                    intent.putExtra("id_order", result.getId_order());
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                break;
                                                                case 1: {
                                                                    sendTicket(result.getId_order(), progress);
                                                                }
                                                                break;
                                                                case 2: {
                                                                    sendOK();
                                                                }
                                                                break;
                                                            }
                                                        }
                                                    }).setCancelable(false).create().show();
                                            } else {
                                                sendOK();
                                            }

                                        }
                                    }).show();

                            } else {
                                Toast.makeText(ProcessSaleActivity.this, "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
                                progress.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                progress.setTitleText("No se obtuvo respuesta")
                                    .setContentText("¿Desea verificar en transacciones si fue realizada la venta?")
                                    .setConfirmText("Verificar")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            showSales();
                                            sweetAlertDialog.dismiss();
                                        }
                                    })
                                    .setCancelText("Guardar venta")
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            saveSale();
                                        }
                                    })
                                    .show();
                                btnProcess.setEnabled(false);
                            }
                        } else {
                            progress.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            progress.setTitleText("No se obtuvo respuesta")
                                .setContentText("¿Desea verificar en transacciones si fue realizada la venta?")
                                .setConfirmText("Verificar")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        showSales();
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .setCancelText("Guardar venta")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        saveSale();
                                    }
                                })
                                .show();
                            btnProcess.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<AddOrderResult> call, Throwable t) {
                        t.printStackTrace();
                        progress.dismiss();
                        progress.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        progress.setTitleText("Sin señal de internet")
                            .setContentText("¿Desea guardar la venta en modo offline?")
                            .setConfirmText("Guardar venta")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    showSales();
                                    saveSale();
                                }
                            })
                            .setNeutralText("Regresa")
                            .setNeutralClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                    }
                });
            }
            break;
            case R.id.btnStatus: {
                final String[] options = getResources().getStringArray(R.array.order_status);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Estado").setSingleChoiceItems(options, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        indexStatus = i;
                        if (indexStatus == 0) {
                            btnStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
                        } else {
                            btnStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorYellow));
                        }
                        btnStatus.setText(options[i]);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
            break;
            case R.id.btnCloseBs: {
                bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
            break;
        }
    }


    @Override
    public void onBackPressed() {
        cancelTrx();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public String getStatus(int index) {
        return getResources().getStringArray(R.array.order_status_val)[index];
    }

    private void sendOK() {
        setResult(RESULT_OK, new Intent());
        ProcessSaleActivity.this.finish();
    }

    public void sendTicket(Long id_order, final SweetAlertDialog progress) {
        Log.i("SEND_TICKET", "OID: " + id_order);
        SendTicketParam param = new SendTicketParam();
        param.setOid(id_order);
        param.setToken(token);
        param.setUid(login.getId());

        progress.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        progress.setTitleText("Espere")
                .setContentText("Enviando ticket de compra")
                .setCancelable(false);

        Call<SendTicketResult> call = Utils.buildRetrofit(getApplicationContext()).create(WSInterface.class).sendTicket(param);
        call.enqueue(new Callback<SendTicketResult>() {
            @Override
            public void onResponse(Call<SendTicketResult> call, Response<SendTicketResult> response) {
                progress.dismiss();
                SendTicketResult result = response.body();
                Log.d("SEND_TICKET", "RESULT: " + result.getResult() + " - STATUS: " + result.getStatus() + " - MSG: " + result.getMessage());
                if (result.getStatus() == 0) {
                    if (result.getResult() != null && !result.getResult().isEmpty()) {
                        progress.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        progress.setTitleText("Alerta")
                                .setContentText(result.getResult())
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        sendOK();
                                    }
                                }).show();
                    } else {
                        progress.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        progress.setTitleText("Éxito")
                                .setContentText("Ticket enviado")
                                .show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                sendOK();
                            }
                        }, 2500);
                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(ProcessSaleActivity.this, "Error: " + result.getStatus() + "\n" + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendTicketResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(ProcessSaleActivity.this, "Error al enviar ticket\nCompruebe su conexión a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelTrx() {
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("Alerta")
                .setContentText("Se perderá la venta\n¿Desea continuar?")
                .setConfirmText("Si")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setCancelText("No")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }

    public void showSales() {
        GetSaleByDriParam param = new GetSaleByDriParam();
        Integer idS = getIntent().getIntExtra("idS", 0);
        if (login.getProfile().startsWith("DRI")) {
            param.setUid(login.getId().intValue());
        }
        param.setToken(token);
        param.setProfile(login.getProfile());
        param.setDate(sdf.format(new Date()));
        param.setId_store(idS.longValue());

        final SweetAlertDialog progress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progress.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
        progress.setTitleText("Espere")
                .setContentText("Descargando transacciones")
                .setCancelable(false);
        progress.show();

        Call<TransactionListResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getTransactions(param);
        call.enqueue(new Callback<TransactionListResult>() {
            @Override
            public void onResponse(Call<TransactionListResult> call, Response<TransactionListResult> response) {
                datos.clear();

                TransactionListResult result = response.body();
                if (result.getStatus() == 0) {
                    Log.i("Logistikapp", "ESTATUS 0");


                    datos.addAll(result.getTransactions());
                    rvList.getAdapter().notifyDataSetChanged();

                    bsb.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    Toast.makeText(ProcessSaleActivity.this, "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();
                }

                progress.dismiss();
            }

            @Override
            public void onFailure(Call<TransactionListResult> call, Throwable t) {
                t.printStackTrace();
                rvList.getAdapter().notifyDataSetChanged();
                progress.dismiss();
            }
        });
    }


    public void saveSale() {
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
        Realm realm = Realm.getDefaultInstance();

        Integer idS = getIntent().getIntExtra("idS", 0);
        String retail = getIntent().getStringExtra("retail");
        String supplier = Utils.getSupplierPreferences(this);
        Log.d("LGK", supplier);

        final String tiket = Utils.getTicket(supplier, retail,
                tvStore.getText().toString(), new Date(), getStatus(indexStatus), login.getName(),
                pvtaList, pchgList, pdevList);

        realm.beginTransaction();

        SaleRO saleRO = realm.createObject(SaleRO.class, UUID.randomUUID().toString());
        saleRO.setCreated(new Date());
        saleRO.setId_store(idS.longValue());
        saleRO.setStore(tvStore.getText().toString());
        saleRO.setTotal(sale);
        saleRO.setDevSale(devSale);

        AddOrderParam addOrderParam = new AddOrderParam();
        addOrderParam.setStid(idS.longValue());
        addOrderParam.setUid(login.getId());
        addOrderParam.setStatus(getStatus(indexStatus));
        addOrderParam.setToken(token);
        addOrderParam.setProducts_VTA(vtaList);
        addOrderParam.setProducts_DEV(devList);
        addOrderParam.setProducts_CHG(chgList);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("wid")) {
                addOrderParam.setWid((long) bundle.getInt("wid"));
                Log.d("LGK", "Tiene WID");

                if (bundle.containsKey("lat") && bundle.containsKey("lng")) {
                    addOrderParam.setLatitude(bundle.getDouble("lat"));
                    addOrderParam.setLongitude(bundle.getDouble("lng"));
                    Log.d("LGK", "Tiene lat and lng");
                }
            }
        }

        saleRO.setParams(gson.toJson(addOrderParam));
        saleRO.setRetail(retail);
        saleRO.setSeller(login.getName());
        saleRO.setTicket(tiket);

        realm.commitTransaction();

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Éxito")
                .setContentText("Venta guardada de forma Offline")
                .show();

        Toast.makeText(ProcessSaleActivity.this, "Éxito, venta guardada.", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                sendOK();
            }
        }, 2500);
    }

}
