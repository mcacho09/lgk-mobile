package app.logistikappbeta.com.logistikapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.AddCurrentPosition;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.Results.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationResultHelper {

    public static final String KEY_LOCATION_RESULTS = "key-location-results";
    private Context mContext;
    private List<Location> mLocationList;
    private Double lat;
    private Double lng;
    private Login login;
    private BigInteger token;
    private SimpleDateFormat sdf;

    public LocationResultHelper(Context mContext, List<Location> mLocationList) {
        this.mContext = mContext;
        this.mLocationList = mLocationList;
    }

    public String getLocationResultText() {

        if (mLocationList.isEmpty()) {
            return "Location not received";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Location location : mLocationList) {
                sb.append(location.getLatitude()+",");
                sb.append(location.getLongitude());
            }

            return sb.toString();

        }

    }

    private CharSequence getLocationResultTitle() {

        String result = mContext.getResources().getQuantityString
                (R.plurals.num_locations_reported, mLocationList.size(), mLocationList.size());

        return result + " : " + DateFormat.getDateTimeInstance().format(new Date());
    }

    public void showNotification() {

        Intent notificationIntent = new Intent(mContext, BatchLocationActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = null;
        notificationBuilder = new NotificationCompat.Builder(mContext,
                App.CHANNEL_ID)
                .setContentTitle(getLocationResultTitle())
                .setContentText(getLocationResultText())
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent);

        getNotificationManager().notify(0, notificationBuilder.build());

    }

    private NotificationManager getNotificationManager() {

        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;

    }

    public void saveLocationResults() {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(KEY_LOCATION_RESULTS, getLocationResultTitle() + "\n" +
                        getLocationResultText())
                .apply();

        String _lat = "";
        String _lng = "";
        boolean x = true;
        for (Character ext: getLocationResultText().toCharArray()) {
            if(x && !ext.equals(',')) _lat += ext;
            else if(!x && !ext.equals(',')) _lng += ext;
            if(ext.equals(',')) x = false;
        }
        lat = Double.parseDouble(_lat);
        lng = Double.parseDouble(_lng);

        login = Utils.getSessionInfo(mContext);
        token = new BigInteger(Utils.getSaveToken(mContext));
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        AddCurrentPosition param = new AddCurrentPosition();
        param.setCreated(sdf.format(new Date()));
        param.setToken(token);
        param.setLat(lat);
        param.setLng(lng);
        param.setUid(login.getId());

        Utils.buildRetrofit(mContext)
                .create(WSInterface.class)
                .addUserLocation(param)
                .enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        if (result != null) {
                            Log.d("SERVICE_LOCATION", "Message: " + result.getMessage() + "\nCode: " + result.getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(mContext, "No es posible actualizar la ubicación, intentando nuevamente...",
                                Toast.LENGTH_SHORT).show();
                    }

                });
    }

    public static String getSavedLocationResults(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_RESULTS, "default value");

    }

}