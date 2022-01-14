package app.logistikappbeta.com.logistikapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.Params.RegisterParam;
import app.logistikappbeta.com.logistikapp.Results.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = "LOGISTIKAPP";

  @BindView(R.id.etUserName)
  EditText etUserName;
  @BindView(R.id.etEmail)
  EditText etEmail;
  @BindView(R.id.etUserLogin)
  EditText etUserLogin;
  @BindView(R.id.etPasswd)
  EditText etPasswd;
  @BindView(R.id.etPhone)
  EditText etPhone;
  @BindView(R.id.etCompany)
  EditText etCompany;
  @BindView(R.id.cbTerms)
  CheckBox cbTerms;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);

    etPasswd.setTransformationMethod(new PasswordTransformationMethod());
  }

  @Override
  public void onClick(View view) {
    finish();
  }

  @OnClick({R.id.btnSave, R.id.btnCancel})
  public void click(View v) {
    final SweetAlertDialog dialog;
    switch (v.getId()) {
      case R.id.btnSave: {
        if (TextUtils.isEmpty(etCompany.getText()) || TextUtils.isEmpty(etEmail.getText()) ||
            TextUtils.isEmpty(etPasswd.getText()) || TextUtils.isEmpty(etPhone.getText()) ||
            TextUtils.isEmpty(etUserLogin.getText()) || TextUtils.isEmpty(etUserName.getText())) {
          Toast.makeText(this, "Ingrese todos los datos para continuar", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
          Toast.makeText(this, "Ingrese un correo correcto", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(etPhone.getText()).matches()) {
          Toast.makeText(this, "Ingrese un teléfono correcto", Toast.LENGTH_SHORT).show();
        } else if (!cbTerms.isChecked()) {
          Toast.makeText(this, "Acepte los términos y condiciones para continuar", Toast.LENGTH_SHORT).show();
        } else {
          RegisterParam param = new RegisterParam();
          param.setEmail(etEmail.getText().toString().trim());
          param.setId_plan(1l);
          param.setName(etCompany.getText().toString().trim());
          param.setPasswd(etPasswd.getText().toString().trim());
          param.setPhone1(etPhone.getText().toString().trim());
          param.setUserlogin(etUserLogin.getText().toString().trim());
          param.setUsername(etUserName.getText().toString().trim());

          dialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);
          dialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorGreen));
          dialog.setTitleText("Espere")
              .setContentText("Registrando cuenta")
              .setCancelable(false);

          dialog.show();

          Call<Result> call = Utils.buildRetrofit(this).create(WSInterface.class).register(param);
          call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
              dialog.dismiss();
              Result result = response.body();
              if (result.getStatus() == 0) {

                dialog.setTitleText("!Bienvenido a Logistikapp!")
                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                dialog.setContentText("Usuario creado con exito")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                      @Override
                      public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                      }
                    });
              } else {
                dialog.setTitleText("Error")
                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                dialog.setContentText("Error al registrarte\nIntente más tarde")
                    .setConfirmText("Aceptar")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                      @Override
                      public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                      }
                    });
                Toast.makeText(RegisterActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
              }
              dialog.show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
              dialog.dismiss();
              Toast.makeText(RegisterActivity.this, "Sin conexión a internet", Toast.LENGTH_SHORT).show();
            }
          });
        }
      }
      break;
      case R.id.btnCancel: {
        finish();
      }
      break;
    }
  }

}
