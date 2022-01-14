package app.logistikappbeta.com.logistikapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.Params.LogInParam;
import app.logistikappbeta.com.logistikapp.Results.LoginRetult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

  private static final String TAG = "LOGISTIKAPP";

  @BindView(R.id.tvVersion)
  TextView tvVersion;
  @BindView(R.id.etUser)
  EditText etUser;
  @BindView(R.id.etPass)
  EditText etPass;
  @BindView(R.id.btnSettings)
  ImageButton btnSettings;
  @BindView(R.id.btnIngresar)
  Button btnIngresar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loggin);
    ButterKnife.bind(this);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    //btnSettings.setVisibility(BuildConfig.DEBUG ? View.VISIBLE : View.GONE);

    etPass.setTransformationMethod(new PasswordTransformationMethod());

    String version = "0.0.0";
    version = Utils.getVersionApp();
    tvVersion.setText(getString(R.string.iVersion, version));

    etPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
          btnIngresar.performClick();
          return true;
        }
        return false;
      }
    });

  }

  @OnClick({R.id.btnIngresar, R.id.btnSettings, R.id.btnRegister})
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.btnIngresar: {
        if (!Utils.isOnline(this)) {
          Toast.makeText(LoginActivity.this, "Error al conectar a internet", Toast.LENGTH_LONG).show();
        } else if (etUser.getText().toString().isEmpty()) {

          Toast.makeText(LoginActivity.this, "Ingrese el usuario", Toast.LENGTH_LONG).show();

        } else if (etPass.getText().toString().isEmpty()) {

          Toast.makeText(LoginActivity.this, "Ingrese la contraseña", Toast.LENGTH_LONG).show();

        } else {

          LogInParam param = new LogInParam();
          param.setName(etUser.getText().toString());
          param.setPasswd(etPass.getText().toString());

          final SweetAlertDialog progress = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
          progress.getProgressHelper().setBarColor(LoginActivity.this.getResources().getColor(R.color.colorGreen));
          progress.setTitleText("Espere")
              .setContentText("Iniciando sesión")
              .setCancelable(false);
          progress.show();

          Utils.buildRetrofit(this)
              .create(WSInterface.class)
              .getLogin(param)
              .enqueue(new Callback<LoginRetult>() {
                @Override
                public void onResponse(Call<LoginRetult> call, Response<LoginRetult> response) {

                  if (response.isSuccessful()) {

                    LoginRetult result = response.body();

                    if (result.getStatus() == 0) {
                      Login login = response.body().getUser();
                      Utils.saveUserPreferences(login, LoginActivity.this);
                      Utils.generateToken(LoginActivity.this);
                      startActivity(new Intent(LoginActivity.this, MainActivity.class));
                      finish();
                    } else {
                      Toast.makeText(LoginActivity.this, "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                  } else {
                    Toast.makeText(LoginActivity.this, "Error al iniciar sesión, verifica tu internet", Toast.LENGTH_SHORT).show();
                  }


                  progress.dismiss();
                }

                @Override
                public void onFailure(Call<LoginRetult> call, Throwable t) {
                  Toast.makeText(LoginActivity.this, "Error al iniciar sesión " + t, Toast.LENGTH_SHORT).show();
                  System.out.println(t);
                  progress.dismiss();
                }
              });
        }
      }
      break;
      case R.id.btnSettings: {
        startActivity(new Intent(LoginActivity.this, SettingsActivity.class));
      }
      break;
      case R.id.btnRegister: {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
      }
      break;
    }

  }
}
