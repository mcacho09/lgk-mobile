package app.logistikappbeta.com.logistikapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

  @BindView(R.id.tvProtocolSelect)
  TextView tvProtocol;
  @BindView(R.id.tvIPSelect)
  TextView tvIP;

  boolean change = false;
  int protocolOptionSelected;
  String ipOptionSelected;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    ButterKnife.bind(this);
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);
    setTitle("Configuración");
    SharedPreferences preferences = getSharedPreferences(Utils.OPTION_NAME_PREFERENCES, MODE_PRIVATE);
    protocolOptionSelected = preferences.getInt(Utils.PROTOCOL_SELECT_PREFERENCES, 0);
    ipOptionSelected = preferences.getString(Utils.IP_SELECT_PREFERENCES, "");
    tvProtocol.setText(Utils.protocolOptions[protocolOptionSelected]);
    tvIP.setText(ipOptionSelected);

  }


  @Override
  public void onClick(View view) {
    if (change) {

      SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);

      dialog.setTitleText("Alerta");
      dialog.setContentText("Cambios sin guardar\n¿Desea guardar los cambios al salir?");
      dialog.setCancelText("No");
      dialog.setConfirmText("Si");
      dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
          sweetAlertDialog.dismiss();
          finish();
        }
      });
      dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
          if (Utils.saveConnectionPreference(SettingsActivity.this, protocolOptionSelected, ipOptionSelected)) {
            Toast.makeText(SettingsActivity.this, "Opciones guardadas con exito", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(SettingsActivity.this, "Opciones guardadas sin exito", Toast.LENGTH_SHORT).show();
          }
          finish();
        }
      });
      dialog.show();

    } else {
      finish();
    }

  }

  @OnClick({R.id.cvIP, R.id.cvProtocol, R.id.btnSave})
  public void OnClick(final View v) {
    if (v.getId() == R.id.btnSave) {
      if (change) {
        if (Utils.saveConnectionPreference(SettingsActivity.this, protocolOptionSelected, ipOptionSelected)) {
          Toast.makeText(SettingsActivity.this, "Opciones guardadas con exito", Toast.LENGTH_SHORT).show();
          change = false;
        } else {
          Toast.makeText(SettingsActivity.this, "Opciones guardadas sin exito", Toast.LENGTH_SHORT).show();
        }
      }
    } else {

      String title = "Seleccionar ";
      switch (v.getId()) {
        case R.id.cvIP:
          title += "IP";
          break;
        case R.id.cvProtocol:
          title += "Protocolo";
          break;
      }

      switch (v.getId()) {
        case R.id.cvIP: {
          final EditText etIP = new EditText(this);
          etIP.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
          etIP.setText(ipOptionSelected);
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setTitle(title)
              .setView(etIP)
              .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  dialogInterface.dismiss();
                }
              })
              .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  ipOptionSelected = etIP.getText().toString();
                  tvIP.setText(ipOptionSelected);
                  change = true;
                }
              }).show();
        }
        break;
        case R.id.cvProtocol: {
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setTitle(title)
              .setItems(Utils.protocolOptions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                  protocolOptionSelected = i;
                  tvProtocol.setText(Utils.protocolOptions[protocolOptionSelected]);

                  dialogInterface.dismiss();
                  change = true;
                }
              }).create().show();
        }
        break;
      }


    }
  }

}
