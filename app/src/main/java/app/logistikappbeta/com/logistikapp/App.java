package app.logistikappbeta.com.logistikapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.multidex.BuildConfig;

import app.logistikappbeta.com.logistikapp.Class.GoogleApiHelper;
import app.logistikappbeta.com.logistikapp.Class.Migration;
import app.logistikappbeta.com.logistikapp.Class.Utils;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by danielmarin on 18/07/17.
 */

public class App extends Application {
  public static final String CHANNEL_ID = "default-channel";
  private GoogleApiHelper mGApiHelper;
  private static App mInstance;

  @Override
  public void onCreate() {
    super.onCreate();
    mInstance = this;
    this.mGApiHelper = new GoogleApiHelper(mInstance);

    Realm.init(this);

    RealmConfiguration configuration = new RealmConfiguration.Builder()
        .name("lgk_realm.realm")
        .schemaVersion(1)
        .migration(new Migration())
        .build();

    Realm.setDefaultConfiguration(configuration);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      NotificationChannel notificationChannel = new NotificationChannel(
              CHANNEL_ID,
              "LocationChannel",
              NotificationManager.IMPORTANCE_HIGH
      );

      NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      manager.createNotificationChannel(notificationChannel);

    }

    if (BuildConfig.VERSION_CODE >= 19) {
      if (!BuildConfig.DEBUG) {
        Utils.createIfNotExistPreferences(this);
        Utils.saveConnectionPreference(this, 0, "192.168.0.2:8090");
      }
    } else {
      Utils.createIfNotExistPreferences(this);
    }
  }

  public static synchronized App getInstance() {
    return mInstance;
  }

  public GoogleApiHelper getGoogleApiHelperInstance() {
    return this.mGApiHelper;
  }

  public static GoogleApiHelper getGoogleApiHelper() {
    return getInstance().getGoogleApiHelperInstance();
  }

}
