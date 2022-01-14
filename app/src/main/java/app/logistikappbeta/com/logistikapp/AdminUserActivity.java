package app.logistikappbeta.com.logistikapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.math.BigInteger;
import java.util.Arrays;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.User;
import app.logistikappbeta.com.logistikapp.Params.AddUserParam;
import app.logistikappbeta.com.logistikapp.Params.GetUserParam;
import app.logistikappbeta.com.logistikapp.Params.UpdUserParam;
import app.logistikappbeta.com.logistikapp.Results.Result;
import app.logistikappbeta.com.logistikapp.Results.UserResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserActivity extends AppCompatActivity {

  @BindView(R.id.etName)
  EditText etName;
  @BindView(R.id.spProfile)
  Spinner spProfile;
  @BindView(R.id.etLogin)
  EditText etLogin;
  @BindView(R.id.etPassword)
  EditText etPassword;
  @BindView(R.id.tvpassword_change)
  TextView tvpassword_change;
  @BindView(R.id.etEmail)
  EditText etEmail;
  @BindView(R.id.etJob)
  EditText etJob;
  @BindView(R.id.etPhone)
  EditText etPhone;
  @BindView(R.id.etOrder)
  EditText etOrder;
  @BindView(R.id.cbActive)
  CheckBox cbActive;
  @BindView(R.id.cbSU)
  CheckBox cbSU;

  private Boolean mIsNew;
  private Long id_user;
  private BigInteger token;
  private Login login;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_user);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    ButterKnife.bind(this);

    token = new BigInteger(Utils.getSaveToken(this));
    login = Utils.getSessionInfo(this);

    mIsNew = getIntent().getBooleanExtra("new", false);
    id_user = getIntent().getLongExtra("id_user", 0);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    setTitle("Administrar usuario");

    etPassword.setTransformationMethod(new PasswordTransformationMethod());

    spProfile.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.profile_array)));

    tvpassword_change.setVisibility(mIsNew ? View.GONE : View.VISIBLE);

    if (!mIsNew) {
      GetUserParam param = new GetUserParam();
      param.setToken(token);
      param.setUid(id_user.intValue());

      final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
      dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
      dialog.setTitleText("Espere")
          .setContentText("Obteniendo información del usuario")
          .setCancelable(false);
      dialog.show();

      Call<UserResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getUser(param);
      call.enqueue(new Callback<UserResult>() {
        @Override
        public void onResponse(Call<UserResult> call, Response<UserResult> response) {
          UserResult result = response.body();
          if (result.getStatus() == 0) {
            User user = result.getUser();
            etName.setText(user.getName());
            int index = Arrays.asList(getResources().getStringArray(R.array.profile_array_val)).indexOf(user.getProfile());
            spProfile.setSelection(index > -1 ? 0 : index);
            etLogin.setText(user.getLogin());
            etEmail.setText(user.getEmail());
            etJob.setText(user.getJob());
            etPhone.setText(user.getPhone1());
            etOrder.setText("" + user.getOrderby());
            cbActive.setChecked(user.getActive().contentEquals("S"));
            cbSU.setChecked(user.getSuperuser().contentEquals("S"));
            dialog.dismiss();
          } else {
            dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            dialog.setCancelText("Información no guardada").setTitleText("Error");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
              @Override
              public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
              }
            });
            Toast.makeText(AdminUserActivity.this, "Código: " + result.getStatus() + "\n" + result.getMessage(), Toast.LENGTH_SHORT).show();
          }

        }

        @Override
        public void onFailure(Call<UserResult> call, Throwable t) {
          dialog.dismiss();
          Toast.makeText(AdminUserActivity.this, "Información no obtenida\nCompruebe su conexión a internet", Toast.LENGTH_SHORT).show();
        }
      });
    }

  }

  @OnClick({R.id.btnCancel, R.id.btnSave})
  public void click(View v) {
    switch (v.getId()) {
      case R.id.btnCalendar: {
        finish();
      }
      break;
      case R.id.btnSave: {
        if (TextUtils.isEmpty(etEmail.getText()) || TextUtils.isEmpty(etLogin.getText()) || TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etOrder.getText())) {
          Toast.makeText(this, "Ingrese todos los datos requeridos", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(etPhone.getText()).matches()) {
          Toast.makeText(this, "Ingrese un teléfono correcto", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
          Toast.makeText(this, "Ingrese un correo correcto", Toast.LENGTH_SHORT).show();
        } else if (mIsNew) {

          if (TextUtils.isEmpty(etPassword.getText())) {
            Toast.makeText(this, "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
            return;
          }

          AddUserParam param = new AddUserParam();
          param.setToken(token);
          param.setActive(cbActive.isChecked() ? "S" : "N");
          param.setJob(etJob.getText().toString());
          param.setEmail(etEmail.getText().toString());
          param.setOrderby(new Integer(etOrder.getText().toString()));
          param.setPasswd(etPassword.getText().toString());
          param.setPhone1(etPhone.getText().toString());
          param.setProfile(getResources().getStringArray(R.array.profile_array_val)[spProfile.getSelectedItemPosition()]);
          param.setSuperuser(cbSU.isChecked() ? "S" : "N");
          param.setUid(Utils.getSessionInfo(this).getId());
          param.setUserlogin(etLogin.getText().toString());
          param.setUsername(etName.getText().toString());
            Log.e("aca" , String.valueOf(param));
          final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
          dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
          dialog.setTitleText("Espere")
              .setContentText("Guardando información del usuario")
              .setCancelable(false);
          dialog.show();

          Call<Result> call = Utils.buildRetrofit(this).create(WSInterface.class).addUser2(param);
          call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
              Result result = response.body();
              if (result.getStatus() == 0) {
                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                dialog.setContentText("Información guardada").setTitleText("Éxito");
              } else {
                dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                dialog.setCancelText("Información no guardada").setTitleText("Error");
                Toast.makeText(AdminUserActivity.this, "Código: " + result.getStatus() + "\n" + result.getMessage(), Toast.LENGTH_SHORT).show();
              }
              dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                  sweetAlertDialog.dismiss();
                }
              });
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
              dialog.dismiss();
              Toast.makeText(AdminUserActivity.this, "Información no enviada\nCompruebe su conexión a internet", Toast.LENGTH_SHORT).show();
            }
          });

        } else {
          UpdUserParam param = new UpdUserParam();
          param.setToken(token);
          param.setActive(cbActive.isChecked() ? "S" : "N");
          param.setJob(etJob.getText().toString());
          param.setEmail(etEmail.getText().toString());
          param.setOrderby(new Integer(etOrder.getText().toString()));
          param.setPasswd(etPassword.getText().toString());
          param.setPhone1(etPhone.getText().toString());
          param.setProfile(getResources().getStringArray(R.array.profile_array_val)[spProfile.getSelectedItemPosition()]);
          param.setSuperuser(cbSU.isChecked() ? "S" : "N");
          param.setUid(login.getId());
          param.setUserlogin(etLogin.getText().toString());
          param.setUsername(etName.getText().toString());
          param.setId_user(id_user);

          Log.e("aca" , String.valueOf(param));
          final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
          dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorGreen));
          dialog.setTitleText("Espere")
              .setContentText("Guardando información del usuario")
              .setCancelable(false);
          dialog.show();

          Call<Result> call = Utils.buildRetrofit(this).create(WSInterface.class).updUser(param);
          call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
              Result result = response.body();
              if (result.getStatus() == 0) {
                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                dialog.setContentText("Información guardada").setTitleText("Éxito");
              } else {
                dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                dialog.setCancelText("Info").setTitleText("Error");
                Toast.makeText(AdminUserActivity.this, "Código: " + result.getStatus() + "\n" + result.getMessage(), Toast.LENGTH_SHORT).show();
              }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
              dialog.dismiss();
              Toast.makeText(AdminUserActivity.this, "Información no enviada\nCompruebe su conexión a internet", Toast.LENGTH_SHORT).show();
            }
          });
        }
      }
      break;
    }
  }

}
