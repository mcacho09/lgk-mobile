package app.logistikappbeta.com.logistikapp.Broadcasts;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.OfflineActivity;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.RealmObjects.SaleRO;
import io.realm.Realm;

public class InternetReceiver extends BroadcastReceiver {

  Realm realm;
  Intent mIntent;
  PendingIntent pendingIntent;
  NotificationCompat.Builder notification;


  public InternetReceiver() {
    realm = Realm.getDefaultInstance();
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    if (Utils.isOnline(context)) {

      int res = realm.where(SaleRO.class)
          .findAll().size();

      if (res > 0) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(3);

        mIntent = new Intent(context, OfflineActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_sync)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("Tienes ventas no sincronizadas, da clic para ver más")
            .setDefaults(android.app.Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true);
        notification.setContentIntent(pendingIntent);

        notificationManager.notify(3, notification.build());

      }

    } else {
      Toast.makeText(context, "Te encuentras sin conexión", Toast.LENGTH_SHORT).show();
    }
  }
}
