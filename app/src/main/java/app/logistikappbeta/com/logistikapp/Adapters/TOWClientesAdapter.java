package app.logistikappbeta.com.logistikapp.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import app.logistikappbeta.com.logistikapp.ActivesActivity;
import app.logistikappbeta.com.logistikapp.AdminCustomersActivity;
import app.logistikappbeta.com.logistikapp.App;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.CommentsActivity;
import app.logistikappbeta.com.logistikapp.Fragments.TravelOnWayTabs.TOWDosFragment;
import app.logistikappbeta.com.logistikapp.Interfaces.TOWInterface;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Travel;
import app.logistikappbeta.com.logistikapp.POJOs.User;
import app.logistikappbeta.com.logistikapp.POJOs.Waybill;
import app.logistikappbeta.com.logistikapp.Params.CheckinParam;
import app.logistikappbeta.com.logistikapp.Params.DeliveryNotificationParam;
import app.logistikappbeta.com.logistikapp.Params.GetProductsParam;
import app.logistikappbeta.com.logistikapp.Params.GetUserParam;
import app.logistikappbeta.com.logistikapp.Params.RemoveStoreTravelParam;
import app.logistikappbeta.com.logistikapp.Params.RollbackParam;
import app.logistikappbeta.com.logistikapp.PuntoVentaActivity;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.DeliveryNotificationResult;
import app.logistikappbeta.com.logistikapp.Results.Result;
import app.logistikappbeta.com.logistikapp.Results.UserResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by danie on 20/07/2016.
 */
public class TOWClientesAdapter extends RecyclerView.Adapter<TOWClientesAdapter.VHTOWClientes> {
    private ArrayList<Waybill> list;
    private View view;
    TOWInterface towInterface;
    TOWDosFragment context;
    Waybill waybill;
    int index;
    int status;
    boolean isDRI;
    Login login;
    private GoogleApiClient mGApi;
    private Location mLocation;
    private Travel travel;
    private String u_name;
    private String u_email;
    private String u_tel;

    public TOWClientesAdapter(ArrayList<Waybill> list, Travel travel, TOWInterface towInterface, TOWDosFragment context) {
        this.list = list;
        this.towInterface = towInterface;
        this.context = context;
        this.travel = travel;

        login = Utils.getSessionInfo(context.getContext());
        isDRI = login.getProfile().contains("DRI");

        if (App.getGoogleApiHelper().isConnected()) {
            this.mGApi = App.getGoogleApiHelper().getGoogleApiClient();
        }
    }

    @Override
    public VHTOWClientes onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_tow_cliente, parent, false);
        return new VHTOWClientes(view);
    }

    @Override
    public void onBindViewHolder(VHTOWClientes holder, int position) {
        Log.d("Logistikapp", "Waybill: position(" + position + ") " + list.get(position));
        holder.setDataStore(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void getLocation() {
        if (this.mGApi == null) {
            this.mGApi = App.getGoogleApiHelper().getGoogleApiClient();
            if (this.mGApi == null) return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context.getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGApi);
            }
        } else {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGApi);
        }
        //Toast.makeText(context.getContext(), mLocation.getLatitude() + " " + mLocation.getLongitude() ,Toast.LENGTH_SHORT).show();
    }

    protected class VHTOWClientes extends RecyclerView.ViewHolder {

        private String[] options = {"Comentarios e Imágenes", "Agregar Imágenes", "Quitar del viaje"};

        @BindView(R.id.imgStar)
        ImageView imgStar;
        @BindView(R.id.tvNombre)
        TextView tvNombre;
        @BindView(R.id.imgVisitado)
        ImageView imgVisitado;
        @BindView(R.id.tvVisitado)
        TextView tvVisitado;
        @BindView(R.id.tvFueraRango)
        TextView tvFueraRango;
        @BindView(R.id.imgIco)
        ImageView imgIco;
        @BindView(R.id.tvNoImg)
        TextView tvNoImg;
        @BindView(R.id.imgComentarios)
        ImageView imgComentarios;
        @BindView(R.id.tvNoComentarios)
        TextView tvNoComentarios;
        @BindView(R.id.imgCheck)
        CircularImageView imgCheck;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.cvSale)
        CircularImageView cvSale;

        public VHTOWClientes(View itemView) {
            super(itemView);
            Login cliente = Utils.getSessionInfo(view.getContext());

            GetUserParam param = new GetUserParam();
            param.setUid(cliente.getId().intValue());
            param.setToken(new BigInteger(Utils.getSaveToken(view.getContext())));

            Call<UserResult> call = Utils.buildRetrofit(context.getContext()).create(WSInterface.class).getUser(param);
            call.enqueue(new Callback<UserResult>() {
                @Override
                public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                    UserResult result = response.body();
                    if (result.getStatus() == 0) {
                        User user = result.getUser();
                        u_name = user.getName();
                        u_email = user.getEmail();
                        if (user.getPhone1() != null)
                            u_tel = user.getPhone1();
                        if (u_tel.equals(" "))
                            u_tel = "000 000 0000";
                    } else {
                        Toast.makeText(context.getContext(), "No es posible hacer una notificación: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResult> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context.getContext(), android.R.layout.select_dialog_item);
                    adapter.add("Comentarios e Imágenes");

                    if (login.getProfile().contentEquals("DRI") && login.getSuperuser().contentEquals("S"))
                        adapter.add("Remover del viaje");

                    if (tvStatus.getText().toString().equals("S")) {
                        adapter.add("Cancelar checkin");
                    }

                    if (login.getSuperuser().contentEquals("S")) {
                        adapter.add("Editar cliente");
                    }

                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.create();
                    alert.setTitle("Seleccionar opción")
                            .setAdapter(adapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    switch (adapter.getItem(i)) {
                                        case "Comentarios e Imágenes": {
                                            Intent intent = new Intent(context.getContext(), CommentsActivity.class);
                                            Log.d("EL WID", "---> " + list.get(getLayoutPosition()).getWid());
                                            intent.putExtra("wid", list.get(getLayoutPosition()).getWid());
                                            context.getActivity().startActivity(intent);
                                        }
                                        break;
                                        case "Remover del viaje": {
                                            RemoveStoreTravelParam param = new RemoveStoreTravelParam();
                                            param.setUid(Utils.getSessionInfo(context.getContext()).getId());
                                            param.setToken(new BigInteger(Utils.getSaveToken(context.getContext())));
                                            param.setWid((long) list.get(getLayoutPosition()).getWid());

                                            Call<Result> call = Utils.buildRetrofit(view.getContext()).create(WSInterface.class).removeStoreTravel(param);
                                            call.enqueue(new Callback<Result>() {
                                                @Override
                                                public void onResponse(Call<Result> call, Response<Result> response) {
                                                    Result result = response.body();
                                                    if (result.getStatus() == 0) {
                                                        SweetAlertDialog alert = new SweetAlertDialog(context.getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                                        alert.setTitleText("Éxito")
                                                                .setContentText("Se removió el cliente con éxito")
                                                                .show();
                                                        list.remove(getLayoutPosition());
                                                        notifyItemRemoved(getAdapterPosition());
                                                    } else {

                                                        SweetAlertDialog alert = new SweetAlertDialog(context.getContext(), SweetAlertDialog.ERROR_TYPE);
                                                        alert.setTitleText("Error")
                                                                .setContentText("No se pudo remover al cliente")
                                                                .show();

                                                        Toast.makeText(context.getContext(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_LONG).show();

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Result> call, Throwable t) {
                                                    t.printStackTrace();
                                                    Toast.makeText(context.getContext(), "No se pudo obtener la información", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                        break;
                                        case "Editar cliente": {
                                            Intent intent = new Intent(context.getContext(), AdminCustomersActivity.class);
                                            intent.putExtra("sid", list.get(getLayoutPosition()).getSid());
                                            context.startActivity(intent);
                                        }
                                        break;
                                        case "Cancelar checkin": {

                                            SweetAlertDialog alert = new SweetAlertDialog(context.getContext(), SweetAlertDialog.WARNING_TYPE);
                                            alert.setTitleText("Cancelar checkin")
                                                .setContentText("¿En verdad desea continuar?")
                                                .setCancelText("No")
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismiss();
                                                    }
                                                })
                                                .setConfirmText("Sí, continuar")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        Login login = Utils.getSessionInfo(context.getContext());
                                                        String token = Utils.getSaveToken(context.getContext());

                                                        RollbackParam param = new RollbackParam();
                                                        param.setToken(new BigInteger(token));
                                                        param.setUid(login.getId());
                                                        param.setWid(list.get(getLayoutPosition()).getWid());
                                                        String params = new Gson().toJson(param);
                                                        waybill = list.get(getLayoutPosition());
                                                        index = getLayoutPosition();
                                                        status = 1;

                                                        final SweetAlertDialog progress = new SweetAlertDialog(view.getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                                        progress.getProgressHelper().setBarColor(view.getContext().getResources().getColor(R.color.colorGreen));
                                                        progress.setTitleText("Cancelando checkin");
                                                        progress.setContentText("Espere por favor");
                                                        progress.setCancelable(false);
                                                        progress.show();

                                                        Call<Result> call = Utils.buildRetrofit(context.getContext()).create(WSInterface.class).rollback(param);
                                                        call.enqueue(new Callback<Result>() {
                                                            @Override
                                                            public void onResponse(Call<Result> call, Response<Result> response) {
                                                                Result result = response.body();
                                                                if (result.getStatus() == 0) {
                                                                    waybill.setCheckin(null);
                                                                    waybill.setStatus("N");
                                                                    waybill.setOutrange("N");
                                                                    list.remove(index);
                                                                    list.add(index, waybill);
                                                                    towInterface.changeMarkerState(index, status);
                                                                    towInterface.updateInfoTravel();
                                                                    notifyItemChanged(index);
                                                                    SweetAlertDialog alert = new SweetAlertDialog(context.getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                                                    alert.setTitleText("Éxito")
                                                                            .setContentText("Se canceló el checkin con éxito")
                                                                            .show();
                                                                } else {
                                                                    SweetAlertDialog alert = new SweetAlertDialog(context.getContext(), SweetAlertDialog.WARNING_TYPE);
                                                                    alert.setTitleText("Alerta")
                                                                            .setContentText("Error: " + result.getMessage() + "\nCodigo: " + result.getStatus())
                                                                            .show();
                                                                }
                                                                progress.dismiss();
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Result> call, Throwable t) {
                                                                t.printStackTrace();
                                                                progress.dismiss();
                                                            }
                                                        });
                                                        sweetAlertDialog.dismiss();
                                                    }
                                                }).show();

                                        }
                                        break;
                                    }
                                }
                            }).show();
                }
            });

        }

        @OnClick({R.id.imgCheck, R.id.cvSale, R.id.cvBarcode, R.id.imgMap})
        public void OnCLick(View v) {
            switch (v.getId()) {
                case R.id.cvBarcode: {
                    float distance = getDistance();
                    if (distance < 50) {
                        Intent intent = new Intent(v.getContext(), ActivesActivity.class);
                        intent.putExtra("ids", (long) list.get(getLayoutPosition()).getSid());
                        intent.putExtra("idw", (long) list.get(getLayoutPosition()).getWid());
                        String checkInOutRange = (Float.valueOf(distance) < 50) ? "N" : "S";
                        intent.putExtra("outrange", checkInOutRange);
                        v.getContext().startActivity(intent);
                    } else alertOutRange(v, "visita", distance);
                }
                break;
                case R.id.imgMap: {
                    waybill = list.get(getLayoutPosition());
                    SweetAlertDialog dialog = new SweetAlertDialog(context.getContext(), SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("Notificar visita")
                        .setContentText("¿Notificar por correo al cliente?")
                        .setConfirmText("Sí")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sendNotification(dialog, getDistance());
                        })
                        .setNeutralText("Continuar")
                        .setNeutralClickListener(sweetAlertDialog -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr=" + list.get(getLayoutPosition()).getLatitude() + "," + list.get(getLayoutPosition()).getLongitude()));
                            context.getContext().startActivity(intent);
                            sweetAlertDialog.dismiss();
                    }).show();
                }
                break;
                case R.id.imgCheck: {
                    if (isDRI) {
                        waybill = list.get(getLayoutPosition());
                        index = getLayoutPosition();
                        float distance = getDistance();
                        SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.NORMAL_TYPE);
                        if (distance <= 50) {
                            Login login = Utils.getSessionInfo(view.getContext());
                            String token = Utils.getSaveToken(view.getContext());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                            CheckinParam param = new CheckinParam();
                            param.setUid(login.getId());
                            param.setToken(token);
                            param.setWid(waybill.getWid());
                            param.setDate(sdf.format(new Date()));
                            param.setOutrange("N");
                            param.setLatitude(mLocation.getLatitude());
                            param.setLongitude(mLocation.getLongitude());

                            waybill.setOutrange("N");
                            waybill.setStatus("S");
                            waybill.setCheckin(sdf.format(new Date()));
                            status = 2;

                            final SweetAlertDialog progress = new SweetAlertDialog(view.getContext(), SweetAlertDialog.PROGRESS_TYPE);
                            progress.getProgressHelper().setBarColor(view.getContext().getResources().getColor(R.color.colorGreen));
                            progress.setTitleText("Realizando checkin");
                            progress.setContentText("Espere por favor");
                            progress.setCancelable(false);
                            progress.show();

                            Call<Result> call = Utils.buildRetrofit(context.getContext()).create(WSInterface.class).checkin(param);
                            call.enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(Call<Result> call, Response<Result> response) {
                                    Result result = response.body();
                                    if (result.getStatus() == 0) {
                                        list.remove(index);
                                        list.add(index, waybill);
                                        towInterface.changeMarkerState(index, status);
                                        notifyItemChanged(index);
                                        towInterface.updateInfoTravel();
                                        SweetAlertDialog alert = new SweetAlertDialog(context.getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                        alert.setTitleText("Éxito")
                                                .setContentText("Se realizó el checkin con éxito")
                                                .show();
                                    } else {
                                        SweetAlertDialog alert = new SweetAlertDialog(context.getContext(), SweetAlertDialog.WARNING_TYPE);
                                        alert.setTitleText("Alerta")
                                                .setContentText("Error: " + result.getMessage() + "\nCodigo: " + result.getStatus())
                                                .show();
                                    }
                                    progress.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {
                                    t.printStackTrace();
                                    progress.dismiss();
                                }
                            });
                        } else if (distance > 50 && distance <= 150) {
                            dialog.setTitleText("Alerta");
                            dialog.setContentText("Checkin fuera del rango permitido\n¿Desea continuar?");
                            dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            dialog.setCancelText("No");
                            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });
                            dialog.setConfirmText("Sí")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            Login login = Utils.getSessionInfo(view.getContext());
                                            String token = Utils.getSaveToken(view.getContext());
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                            CheckinParam param = new CheckinParam();
                                            param.setUid(login.getId());
                                            param.setToken(token);
                                            param.setWid(waybill.getWid());
                                            param.setDate(sdf.format(new Date()));
                                            param.setOutrange("S");
                                            param.setLatitude(mLocation.getLatitude());
                                            param.setLongitude(mLocation.getLongitude());

                                            waybill.setOutrange("S");
                                            waybill.setStatus("S");
                                            waybill.setCheckin(sdf.format(new Date()));
                                            status = 3;
                                            final SweetAlertDialog progress = new SweetAlertDialog(view.getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                            progress.getProgressHelper().setBarColor(view.getContext().getResources().getColor(R.color.colorGreen));
                                            progress.setTitleText("Realizando checkin");
                                            progress.setContentText("Espere por favor");
                                            progress.setCancelable(false);
                                            progress.show();

                                            Call<Result> call = Utils.buildRetrofit(context.getContext()).create(WSInterface.class).checkin(param);
                                            call.enqueue(new Callback<Result>() {
                                                @Override
                                                public void onResponse(Call<Result> call, Response<Result> response) {
                                                    Result result = response.body();
                                                    if (result.getStatus() == 0) {
                                                        list.remove(index);
                                                        list.add(index, waybill);
                                                        towInterface.changeMarkerState(index, status);
                                                        notifyItemChanged(index);
                                                        towInterface.updateInfoTravel();
                                                        SweetAlertDialog alert = new SweetAlertDialog(context.getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                                        alert.setTitleText("Éxito")
                                                                .setContentText("Se realizó el checkin con éxito")
                                                                .show();
                                                    } else {
                                                        SweetAlertDialog alert = new SweetAlertDialog(context.getContext(), SweetAlertDialog.WARNING_TYPE);
                                                        alert.setTitleText("Alerta")
                                                                .setContentText("Error: " + result.getMessage() + "\nCódigo: " + result.getStatus())
                                                                .show();
                                                    }
                                                    progress.dismiss();
                                                }

                                                @Override
                                                public void onFailure(Call<Result> call, Throwable t) {
                                                    t.printStackTrace();
                                                    progress.dismiss();
                                                }
                                            });
                                        }
                                    });
                            dialog.show();
                        } else {
                            dialog.setTitleText("Alerta");
                            dialog.setContentText("Haz excedido la distancia permitida para realizar el\n check-In");
                            dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            dialog.show();
                        }
                    } else {
                        SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("Alerta");
                        dialog.setContentText("No se pudo obtener tu posición actual\nIntenta más tarde");
                        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        dialog.show();
                    }
                }
                break;
                case R.id.cvSale: {
                    if (getDistance() > 50) {
                        SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("Alerta");
                        dialog.setContentText("Venta fuera del rango permitido\n¿Desea continuar?");
                        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        dialog.setCancelText("No");
                        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });
                        dialog.setConfirmText("Sí")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        goToSaleIntent(v);
                                        sweetAlertDialog.dismiss();
                                        return;
                                    }
                                });
                        dialog.show();
                    } else {
                        goToSaleIntent(v);
                    }
                    break;
                }

            }
        }

        public float getDistance() {
            float distance = 0;
            waybill = list.get(getLayoutPosition());
            index = getLayoutPosition();
            if (waybill.getStatus().equals("N")) {
                getLocation();
                if (mLocation != null) {
                    Location storeLocation = new Location(mLocation.getProvider());
                    storeLocation.setLongitude(waybill.getLongitude());
                    storeLocation.setLatitude(waybill.getLatitude());

                    distance = mLocation.distanceTo(storeLocation);
                }
            }
            return distance;
        }

        public void goToSaleIntent(View v) {
            if (isDRI || login.getSuperuser().contentEquals("S")) {
                Intent intent = new Intent(v.getContext(), PuntoVentaActivity.class);
                intent.putExtra("id", list.get(getLayoutPosition()).getSid());
                intent.putExtra("store", list.get(getLayoutPosition()).getName());
                intent.putExtra("email", list.get(getLayoutPosition()).getEmail());
                intent.putExtra("retail", list.get(getLayoutPosition()).getRetail());
                intent.putExtra("wid", list.get(getLayoutPosition()).getWid());
                getLocation();

                if (mLocation != null) {
                    intent.putExtra("lat", mLocation.getLatitude());
                    intent.putExtra("lng", mLocation.getLongitude());
                }

                float distance = getDistance();
                intent.putExtra("distance", distance + "");

                GetProductsParam param = new GetProductsParam();
                Login login = Utils.getSessionInfo(context.getContext());
                param.setToken(new BigInteger(Utils.getSaveToken(context.getContext())));
                param.setUid(login.getId());
                param.setSid((long) list.get(getLayoutPosition()).getSid());
                String params = new Gson().toJson(param);
                intent.putExtra("params", params);

                intent.putExtra("distance", distance + "");

                context.getActivity().startActivity(intent);
            }
        }


        public void alertOutRange(View v, String message, float distance) {
            SweetAlertDialog dialog = new SweetAlertDialog(context.getContext(), SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("La " + message + " se realizará fuera de rango")
                .setContentText("¿Está seguro que desea continuar?")
                .setConfirmText("Sí, continuar")
                .setConfirmClickListener(sweetAlertDialog -> {
                    Intent intent = new Intent(context.getContext(), ActivesActivity.class);
                    intent.putExtra("ids", (long) list.get(getLayoutPosition()).getSid());
                    String idw = list.get(getLayoutPosition()).getWid() + "";
                    intent.putExtra("distance", distance + "");
                    intent.putExtra("lat", list.get(getLayoutPosition()).getLatitude() + "");
                    intent.putExtra("lng", list.get(getLayoutPosition()).getLongitude() + "");
                    String checkInOutRange = (Float.valueOf(distance) < 50) ? "N" : "S";
                    intent.putExtra("outrange", checkInOutRange);
                    intent.putExtra("idw", idw + "");
                    v.getContext().startActivity(intent);
                    sweetAlertDialog.dismiss();
                })
                .setCancelText("Regresar")
                .setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.dismiss()).show();
        }


        public void setDataStore(Waybill s) {
            imgStar.setVisibility(s.getShelf() != null && s.getShelf().equals("S") ? View.VISIBLE : View.GONE);
            tvNombre.setText(" (" + s.getOrderby() + ") " + s.getName());
            if (s.getStatus().equals("S")) {
                imgVisitado.setColorFilter(context.getResources().getColor(R.color.colorGreen));
                tvVisitado.setText("VISITADO");
                if (s.getCheckin() != null) {
                    SimpleDateFormat sdfIn = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM HH:mm");
                    try {
                        tvVisitado.append(" " + sdfOut.format(sdfIn.parse(s.getCheckin())));
                    } catch (ParseException ex) {
                        Log.e("Logistikapp", "ParseException:\n" + ex.getMessage());
                    }
                }

                imgCheck.setImageResource(R.drawable.ic_check_circle);
                if (s.getOutrange() != null && s.getOutrange().equals("S")) {
                    tvFueraRango.setVisibility(View.VISIBLE);
                    imgCheck.setBorderColor(context.getResources().getColor(R.color.colorOrange));
                } else {
                    imgCheck.setBorderColor(context.getResources().getColor(R.color.colorGreen));
                    tvFueraRango.setVisibility(View.GONE);
                }

                imgCheck.setColorFilter(context.getResources().getColor(R.color.bgCards));
                if (isDRI) {
                    cvSale.setVisibility(View.VISIBLE);
                } else {
                    cvSale.setVisibility(View.GONE);
                }

            } else {
                imgVisitado.setColorFilter(context.getResources().getColor(R.color.colorGray));
                tvVisitado.setText("NO VISITADO");

                imgCheck.setBorderColor(context.getResources().getColor(R.color.colorGray));
                imgCheck.setImageResource(R.drawable.ic_place);
                imgCheck.setColorFilter(context.getResources().getColor(android.R.color.white));

                tvFueraRango.setVisibility(View.GONE);
                cvSale.setVisibility(View.GONE);
            }

            if (s.getImages() > 0) {
                imgIco.setColorFilter(context.getResources().getColor(R.color.colorBlue));
                tvNoImg.setText("" + s.getImages());
            } else {
                imgIco.setColorFilter(context.getResources().getColor(R.color.colorGray));
                tvNoImg.setText("0");
            }

            if (s.getComments() > 0) {
                imgComentarios.setColorFilter(context.getResources().getColor(R.color.colorBlue));
                tvNoComentarios.setText("" + s.getComments());
            } else {
                imgComentarios.setColorFilter(context.getResources().getColor(R.color.colorGray));
                tvNoComentarios.setText("0");
            }

            tvStatus.setText(s.getStatus());

            if (login.getSuperuser().contentEquals("S")) {
                cvSale.setVisibility(View.VISIBLE);
            }
            //cvSale.setVisibility(View.VISIBLE);
        }
    }

    private void sendNotification(final SweetAlertDialog progress, float delivery_dist) {
        progress.dismiss();
        progress.getProgressHelper().setBarColor(ContextCompat.getColor(context.getContext(), R.color.colorGreen));
        progress.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        progress.setNeutralText("Por favor espere...");
        progress.setTitleText("Enviando notificación...");
        progress.setContentText("");
        progress.show();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DeliveryNotificationParam params = new DeliveryNotificationParam();

        float speed = 200;
        float time = delivery_dist/speed;
        //Toast.makeText(context.getContext(), time +"", Toast.LENGTH_LONG).show();
        //Log.e("time: ", ""+time);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/Mexico_City"));
        calendar.add(Calendar.MINUTE, (int) time);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        String delivery_time = timeFormat.format(calendar.getTime());

        params.setUid(Utils.getSessionInfo(view.getContext()).getId());
        String supplier = Utils.getSupplierPreferences(context.getContext());
        params.setSupplier(supplier+"");
        params.setStore(waybill.getName());
        params.setDate(sdf.format(new Date()));
        params.setSeller(Utils.getSessionInfo(view.getContext()).getName());
        params.setR_email(u_email);
        params.setS_email(waybill.getEmail());
        params.setTelefono(u_tel);
        params.setLlegada(delivery_time);
        Log.e("params", params.toString());
        Call<DeliveryNotificationResult> call2 = Utils.buildRetrofit(context.getContext()).create(WSInterface.class).sendNotification(params);
        call2.enqueue(new Callback<DeliveryNotificationResult>() {
            @Override
            public void onResponse(Call<DeliveryNotificationResult> call2, Response<DeliveryNotificationResult> response) {
                progress.dismiss();
                DeliveryNotificationResult result = response.body();
                if (result.getStatus() == 0) {
                    if (result.getResult() != null && !result.getResult().isEmpty()) {
                        progress.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        progress.setTitleText("Alerta")
                                .setContentText(result.getResult())
                                .setConfirmText("Ok")
                                .setConfirmClickListener(sweetAlertDialog -> {
                                    progress.dismiss();
                                }).show();
                    } else {
                        progress.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        progress.setTitleText("¡Notificación enviada!")
                                .setConfirmText("Continuar")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("http://maps.google.com/maps?daddr=" + waybill.getLatitude() + "," + waybill.getLongitude()));
                                        context.getContext().startActivity(intent);
                                    }
                                })
                                .setNeutralText("Regresar")
                                .setNeutralClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        progress.dismiss();
                                    }
                                }).show();
                    }
                } else {
                    progress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    progress.setTitleText("Favor de verificar correo electrónico.")
                            .setContentText("")
                            .setConfirmClickListener(sweetAlertDialog1 -> progress.dismiss())
                            .show();
                }
            }

            @Override
            public void onFailure(Call<DeliveryNotificationResult> call, Throwable t) {
                progress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                progress.setTitleText("Favor de verificar correo electrónico.")
                        .setContentText("")
                        .setConfirmClickListener(sweetAlertDialog12 -> progress.dismiss())
                        .show();
            }
        });
    }

    private void sendOK() {
        ((Activity) context.getContext()).finish();
    }
}
