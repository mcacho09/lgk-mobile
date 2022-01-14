package app.logistikappbeta.com.logistikapp.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import java.math.BigInteger;
import java.util.Timer;
import java.util.TimerTask;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.MainActivity;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.Qty;
import app.logistikappbeta.com.logistikapp.Params.GetQtyNotifications;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.NotificationsResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends Service implements Callback<NotificationsResult> {

  Login login;
  BigInteger token;
  WSInterface ws;
  private int nId = 001;
  private int mId = 002;
  private NotificationManager notificationManager;
  private Intent intent;
  private Intent intentNot;
  private NotificationCompat.Builder notificationMsg;
  private NotificationCompat.Builder notificationNot;
  private PendingIntent pendingIntent;
  private PendingIntent pendingIntentNot;

  private Long noMsg = 0l;
  private Long noNot = 0l;

  Timer timer;
  NotificationTimerTask timerTask;
  @Override
  public void onCreate() {
    super.onCreate();
    login = Utils.getSessionInfo(this);
    token = new BigInteger(Utils.getSaveToken(this));
    ws = Utils.buildRetrofit(this)
        .create(WSInterface.class);

    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("msg", true);
    intent.putExtra("not", false);;

    pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    notificationMsg = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_email_white)
        .setContentTitle(getString(R.string.app_name))
        .setContentText("Tienes mensajes sin leer")
        .setDefaults(android.app.Notification.DEFAULT_ALL)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true);

    notificationMsg.setContentIntent(pendingIntent);

    intentNot = new Intent(this, MainActivity.class);
    intentNot.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intentNot.putExtra("not", true);
    intentNot.putExtra("msg", false);

    notificationNot = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_notifications_none)
        .setContentTitle(getString(R.string.app_name))
        .setContentText("Tienes nuevas notificaciones")
        .setDefaults(android.app.Notification.DEFAULT_ALL)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true);

    pendingIntentNot = PendingIntent.getActivity(this, 1, intentNot, PendingIntent.FLAG_UPDATE_CURRENT);
    notificationNot.setContentIntent(pendingIntentNot);
  }

  @Override
  public IBinder onBind(Intent intent) {
    throw null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    timer = new Timer();
    timerTask = new NotificationTimerTask();
    timer.schedule(timerTask, 0, 1000 * 60);
    return super.onStartCommand(intent, flags, startId);
  }

  public void verifyNotifications() {
    if (!Utils.isOnline(this) || login == null) return;
    if (token == null) return;
    Log.d("NOT_SERVICE", "SENDIND PARAMS");
    GetQtyNotifications param = new GetQtyNotifications();
    param.setUid(login.getId());
    param.setToken(token);
    param.setProfile(login.getProfile());
    ws.getNotifications(param).enqueue(this);
  }

  @Override
  public void onResponse(Call<NotificationsResult> call, Response<NotificationsResult> response) {
    NotificationsResult result = response.body();

    if (result == null) return;

    if (result.getStatus() == 0) {
      Qty qty = result.getQty();

      if (qty.getMessages() > 0 && noMsg != qty.getMessages()) {
        notificationManager.notify(this.mId, this.notificationMsg.build());
      }
      noMsg = qty.getMessages();

      if (qty.getNotifications() > 0) {
        notificationManager.notify(this.nId, this.notificationNot.build());
      }
      noNot = qty.getNotifications();

      Log.d("NOT_SERVICES", "---> msg: " + qty.getMessages() + " not: " + qty.getNotifications());
    }
  }

  @Override
  public void onFailure(Call<NotificationsResult> call, Throwable t) {
    t.printStackTrace();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    timerTask.cancel();
    timer.cancel();
  }

  protected class NotificationTimerTask extends TimerTask {

    @Override
    public void run() {
      new Handler(Looper.getMainLooper()).post(new Runnable() {
        @Override
        public void run() {
          verifyNotifications();
        }
      });
    }
  }
}
