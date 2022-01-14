package app.logistikappbeta.com.logistikapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import app.logistikappbeta.com.logistikapp.Class.Utils;

public class Splashscreen extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splashscreen);
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
  }

  @Override
  protected void onResume() {
    super.onResume();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {

        Intent intent = null;

        if (Utils.isLoggginUser(Splashscreen.this) && Utils.isOnline(Splashscreen.this)) {
          intent = new Intent(Splashscreen.this, MainActivity.class);
        } else {
          intent = new Intent(Splashscreen.this, LoginActivity.class);
        }

        startActivity(intent);
        finish();

      }
    }, 2000);
  }
}
