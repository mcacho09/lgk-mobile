package app.logistikappbeta.com.logistikapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.math.BigInteger;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Fragments.ActivesBarcodeFragment;
import app.logistikappbeta.com.logistikapp.Interfaces.IBarcodeActives;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.Params.AddActiveParam;
import app.logistikappbeta.com.logistikapp.Results.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActiveActivity extends AppCompatActivity implements IBarcodeActives {

  @BindView(R.id.etBarcode)
  EditText etBarcode;

  private Long id_store;

  private ActivesBarcodeFragment mBarcodeFragment;
  private AddActiveParam param;
  private BigInteger token;
  private Login login;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_active);
    ButterKnife.bind(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    this.mBarcodeFragment = new ActivesBarcodeFragment();
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_layout, mBarcodeFragment)
        .commit();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    login = Utils.getSessionInfo(this);
    token = new BigInteger(Utils.getSaveToken(this));

    param = new AddActiveParam();
    param.setToken(token);
    param.setUid(login.getId());

    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();

    if (bundle != null) {
      if (bundle.containsKey("barcode")) {
        etBarcode.setText(bundle.getString("barcode", ""));
        param.setCode(bundle.getString("barcode", ""));
      }

      if (bundle.containsKey("ids")) {
        id_store = bundle.getLong("ids");
        param.setId_store(id_store);
      }

    }



  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_actives, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case android.R.id.home: {
        finish();
      }
      break;

      case R.id.item_flash: {
        if (mBarcodeFragment != null) {
          mBarcodeFragment.flash();
        }
      }
      break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void sendBarcode(String barcode) {
    etBarcode.setText(barcode);
    param.setCode(barcode);
  }

  @OnClick({R.id.btnAddImage, R.id.btnCancel, R.id.btnSave})
  public void click(View v) {
    switch (v.getId()) {
      case R.id.btnCancel: {
        finish();
      }
      break;
      case R.id.btnAddImage: {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
          } else {
            Toast.makeText(this, "La aplicación no tiene permisos para usar la camara", Toast.LENGTH_SHORT).show();
          }
        } else {
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          startActivityForResult(intent, 1);
        }
      }
      break;
      case R.id.btnSave: {

        if (param.getCode() == null || param.getCode().trim().isEmpty()) {
          Toast.makeText(this, "Código de barras faltante", Toast.LENGTH_SHORT).show();
        }

        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorGreen));
        dialog.setTitleText("Espere por favor")
            .setContentText("Creando activo información")
            .setCancelable(false);
        dialog.show();

        Log.d("LGK", new Gson().toJson(param));

        Call<Result> call = Utils.buildRetrofit(this).create(WSInterface.class).addActive(param);
        call.enqueue(new Callback<Result>() {
          @Override
          public void onResponse(Call<Result> call, Response<Result> response) {

            dialog.dismiss();

            Result result = response.body();
            if (result == null) return;

            if (result.getStatus() == 0) {
              etBarcode.setText("");
              param.setImage(null);
              param.setCode(null);
              dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
              dialog.setTitleText("Éxito")
                  .setContentText("Activo agregado con éxito")
                  .show();
            } else {
              Toast.makeText(AddActiveActivity.this, result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
            }

          }

          @Override
          public void onFailure(Call<Result> call, Throwable t) {
            t.printStackTrace();
            dialog.dismiss();
            Toast.makeText(AddActiveActivity.this, "No se obtuvo respuesta", Toast.LENGTH_SHORT).show();
          }
        });
      } break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      /*if (requestCode == GALERI_OPTION) {
        if (data != null) {

          Uri selectedImage = data.getData();
          try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            String base = encodeToBas64(bitmap, Bitmap.CompressFormat.JPEG, 100);

            ImageView img = null;
            switch (image) {
              case R.id.imgUno: {
                img = imgUno;
                comment.setImage1(base);
              }
              break;

              case R.id.imgDos: {
                img = imgDos;
                comment.setImage2(base);
              }
              break;

              case R.id.imgTres: {
                img = imgTres;
                comment.setImage3(base);
              }
              break;

            }

            img.setImageURI(selectedImage);
          } catch (IOException ex) {
            Toast.makeText(CommentsActivity.this, "Imagen no cargada", Toast.LENGTH_SHORT).show();
          }

        }
      } else*/ if (requestCode == 1) {

        Bundle bundle = data.getExtras();
        Bitmap bmap = (Bitmap) bundle.get("data");

        if (bmap != null) {

          Log.d("BMAP", bmap.toString());


          String base = Utils.encodeToBas64(bmap, Bitmap.CompressFormat.JPEG, 100);
          param.setImage(base);

          SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
          dialog.setTitleText("Imagen agregada")
              .setContentText("Se agregó la imagen con éxito")
              .show();

        } else {
          Toast.makeText(this, "No se pudo obtener la imagen", Toast.LENGTH_SHORT).show();
        }

      }
    } else {
      Toast.makeText(this, "Imagen no cargada", Toast.LENGTH_SHORT).show();
    }
  }

}
