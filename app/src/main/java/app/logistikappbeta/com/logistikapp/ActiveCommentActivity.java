package app.logistikappbeta.com.logistikapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActiveCommentActivity extends AppCompatActivity {


  @BindView(R.id.tvActive)
  TextView tvActive;
  @BindView(R.id.etComment)
  EditText etComment;

  private String mImageOne;
  private String mImageTwo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_active_comment);
    ButterKnife.bind(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();

    if (bundle != null) {
      if (bundle.containsKey("code")) {
        tvActive.setText(bundle.getString("code", ""));
      }

    }

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      finish();
    }

    return super.onOptionsItemSelected(item);
  }

  @OnClick({R.id.btnImgTwo, R.id.btnImgOne, R.id.btnCancel, R.id.btnSave})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btnImgTwo: {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          startActivityForResult(intent, 1);
        } else {
          Toast.makeText(this, "La aplicación no tiene permisos para usar la camara", Toast.LENGTH_SHORT).show();
        }
      }
      break;
      case R.id.btnImgOne: {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          startActivityForResult(intent, 2);
        } else {
          Toast.makeText(this, "La aplicación no tiene permisos para usar la camara", Toast.LENGTH_SHORT).show();
        }
      }
      break;
      case R.id.btnCancel: {
        setResult(RESULT_CANCELED);
        finish();
      }
      break;
      case R.id.btnSave:

        if (etComment.getText().length() == 0 && mImageTwo.length() == 0 && mImageOne.length() == 0) {
          Toast.makeText(this, "No haz registrado nada", Toast.LENGTH_SHORT).show();
        } else {
          Intent intent = new Intent();
          intent.putExtra("comment", etComment.getText().toString().trim());
          if (mImageOne != null && !mImageOne.trim().isEmpty()) {
            intent.putExtra("image1", mImageOne);
            Log.d("Lgk", "imageOne");
          }
          if (mImageTwo != null && !mImageTwo.trim().isEmpty()) {
            intent.putExtra("image2", mImageTwo);
            Log.d("Lgk", "imageTwo");
          }

          setResult(RESULT_OK, intent);
          finish();

        }

        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      Bundle bundle = data.getExtras();
      Bitmap bmap = (Bitmap) bundle.get("data");

      if (bmap != null) {

        Log.d("BMAP", bmap.toString());


        String base = Utils.encodeToBas64(bmap, Bitmap.CompressFormat.JPEG, 100);

        if (requestCode == 1) {
          mImageOne = base;
        } else if (requestCode == 2) {
          mImageTwo = base;
        }

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText("Imagen agregada")
            .setContentText("Se agregó la imagen con éxito")
            .show();
      }

    }
  }

}
