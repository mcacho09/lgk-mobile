package app.logistikappbeta.com.logistikapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.math.BigInteger;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.Params.SendPrintTicketParam;
import app.logistikappbeta.com.logistikapp.Results.SendPrintTicketResult;
import app.logistikappbeta.com.logistikapp.sdk.BluetoothService;
import app.logistikappbeta.com.logistikapp.sdk.DeviceListActivity;
import app.logistikappbeta.com.logistikapp.sdk.command.sdk.Command;
import app.logistikappbeta.com.logistikapp.sdk.command.sdk.PrinterCommand;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrintTicketActivity extends AppCompatActivity {

  private SweetAlertDialog mProgress;
  private String ticket;

  private BluetoothAdapter mBluetoothAdapter;
  private BluetoothService mService;

  private Button mBtnPrint;
  private Button mBtnSearchPrinter;

  private static final int REQUEST_CONNECT_DEVICE = 1;
  private static final int REQUEST_ENABLE_BT = 2;

  public static final int MESSAGE_STATE_CHANGE = 1;
  public static final int MESSAGE_READ = 2;
  public static final int MESSAGE_WRITE = 3;
  public static final int MESSAGE_DEVICE_NAME = 4;
  public static final int MESSAGE_TOAST = 5;
  public static final int MESSAGE_CONNECTION_LOST = 6;
  public static final int MESSAGE_UNABLE_CONNECT = 7;

  private static final String CHINESE = "GBK";
  public static final String DEVICE_NAME = "device_name";
  public static final String TOAST = "toast";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_print_ticket);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    // If the adapter is null, then Bluetooth is not supported
    if (mBluetoothAdapter == null) {
      Toast.makeText(this, "Bluetooth is not available",
          Toast.LENGTH_LONG).show();
      finish();
    }

  }

  @Override
  protected void onStart() {
    super.onStart();
    if (!mBluetoothAdapter.isEnabled()) {
      Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
      Toast.makeText(this, "Por favor encienda su Bluetooth y vuelva  intentar.", Toast.LENGTH_LONG).show();
      super.onBackPressed();
      // Otherwise, setup the session
    } else {
      if (mService == null) {
        init();
      }
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();

    if(bundle.containsKey("diario")) {
      setTitle("Imprimir reporte del día");
      mBtnPrint.setText("Imprimir Reporte");
    } else if (bundle.containsKey("diario_adm")) {
        setTitle("Imprimir reporte general del día");
        mBtnPrint.setText("Imprimir Reporte");
    }
    if (bundle.containsKey("ticket")) {
      this.ticket = bundle.getString("ticket");
      startService();
    } else {
      initProgress("Espere", "Obteniendo información del ticket");
      printTicketOnline();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mService != null) {
      mService.stop();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case android.R.id.home: {
        finish();
      }
      break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if (this.mProgress != null && this.mProgress.isShowing()) {
      this.mProgress.dismiss();
    }
    finish();
  }

  @OnClick({R.id.btnPrint, R.id.btnSearchPrinter, R.id.btnGoBack})
  public void click(View v) {

    switch (v.getId()) {
      case R.id.btnPrint: {
        if (this.ticket.length() > 0) {
          SendDataByte(PrinterCommand.POS_Print_Text(ticket, CHINESE, 0, 0, 0, 0));
          SendDataByte(Command.LF);
          Log.d("TICKET : ",this.ticket);

        } else {
          Toast.makeText(this, "No hay información en ticket", Toast.LENGTH_SHORT).show();
        }

      }
      break;
      case R.id.btnSearchPrinter: {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
      }
      break;
      case R.id.btnGoBack:{
        finish();
      }
      break;
    }

  }

  @SuppressLint("HandlerLeak")
  private final Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MESSAGE_STATE_CHANGE:
          switch (msg.arg1) {
            case BluetoothService.STATE_CONNECTED: {
              mBtnSearchPrinter.setText("Conectado");
              mBtnSearchPrinter.setEnabled(false);
              mBtnPrint.setEnabled(true);
            }
            break;
          }
          break;
        case MESSAGE_CONNECTION_LOST: {
          Toast.makeText(getApplicationContext(), "Impresora desconectada",
              Toast.LENGTH_SHORT).show();
          mBtnPrint.setEnabled(false);
          mService.stop();
        }
        break;
        case MESSAGE_UNABLE_CONNECT: {
          Toast.makeText(getApplicationContext(), "Este dispositivo no esta disponible para conectarse",
              Toast.LENGTH_SHORT).show();
        }
        break;
      }
    }
  };

  public void initProgress(String title, String desc) {
    this.mProgress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
    this.mProgress.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorGreen));
    this.mProgress.setTitleText(title)
        .setContentText(desc)
        .show();
  }

  public void init() {
    mService = new BluetoothService(this, mHandler);
    mBtnPrint = (Button) findViewById(R.id.btnPrint);
    mBtnSearchPrinter = (Button) findViewById(R.id.btnSearchPrinter);
  }

  private void SendDataByte(byte[] data) {

    if (mService.getState() != BluetoothService.STATE_CONNECTED) {
      Toast.makeText(this, "Impresora no conectada", Toast.LENGTH_SHORT)
          .show();
      return;
    }
    mService.write(data);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQUEST_CONNECT_DEVICE: {
        // When DeviceListActivity returns with a device to connect
        if (resultCode == Activity.RESULT_OK) {
          // Get the device MAC address
          String address = data.getExtras().getString(
              DeviceListActivity.EXTRA_DEVICE_ADDRESS);
          // Get the BLuetoothDevice object
          if (BluetoothAdapter.checkBluetoothAddress(address)) {
            BluetoothDevice device = mBluetoothAdapter
                .getRemoteDevice(address);
            // Attempt to connect to the device
            mService.connect(device);
          }
        }

      }
      break;
      case REQUEST_ENABLE_BT: {
        // When the request to enable Bluetooth returns
        if (resultCode == Activity.RESULT_OK) {
          // Bluetooth is now enabled, so set up a session
          init();
        } else {
          // User did not enable Bluetooth or an error occured
          Toast.makeText(this, "Bluetooth no disponible o ocurrio un error",
              Toast.LENGTH_SHORT).show();
          finish();
        }

      }
      break;
    }
  }

  public void printTicketOnline() {
    SendPrintTicketParam param = new SendPrintTicketParam();
    param.setOid(getIntent().getLongExtra("id_order", 0l));
    param.setToken(new BigInteger(Utils.getSaveToken(this)));
    param.setUid(Utils.getSessionInfo(this).getId());

    Call<SendPrintTicketResult> call = Utils.buildRetrofit(this).create(WSInterface.class).sendPrintTicket(param);
    call.enqueue(new Callback<SendPrintTicketResult>() {
      @Override
      public void onResponse(Call<SendPrintTicketResult> call, Response<SendPrintTicketResult> response) {
        PrintTicketActivity.this.mProgress.dismiss();
        SendPrintTicketResult result = response.body();
        if (result == null) {
          Toast.makeText(PrintTicketActivity.this, "Información no obtenida", Toast.LENGTH_SHORT).show();
          finish();
        } else if (result.getStatus() == 0) {
          PrintTicketActivity.this.ticket = result.getResult();
          startService();
        } else {
          Toast.makeText(PrintTicketActivity.this, "Error: " + result.getStatus() + "\n" + result.getMessage(), Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<SendPrintTicketResult> call, Throwable t) {
        PrintTicketActivity.this.mProgress.dismiss();
        Toast.makeText(PrintTicketActivity.this, "Información no obtenida", Toast.LENGTH_SHORT).show();
        finish();
      }
    });
  }

  public void startService() {
    if (mService != null) {

      if (mService.getState() == BluetoothService.STATE_NONE) {
        // Start the Bluetooth services
        mService.start();
      }
    }
  }

}
