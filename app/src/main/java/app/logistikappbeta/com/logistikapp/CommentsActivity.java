package app.logistikappbeta.com.logistikapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigInteger;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Comment;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.Params.AddCommentParam;
import app.logistikappbeta.com.logistikapp.Params.GetComment;
import app.logistikappbeta.com.logistikapp.Results.CommentResult;
import app.logistikappbeta.com.logistikapp.Results.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener {

  @BindView(R.id.etNotes)
  EditText etNotes;
  @BindView(R.id.etComments)
  EditText etComments;
  @BindView(R.id.imgUno)
  ImageButton imgUno;
  @BindView(R.id.imgDos)
  ImageButton imgDos;
  @BindView(R.id.imgTres)
  ImageButton imgTres;
  @BindView(R.id.btnSave)
  Button btnSave;

  private Integer wid;
  private Gson gson;
  Comment comment;
  private int image;
  private static final int GALERI_OPTION = 1;
  private static final int CAMERA_OPTION = 2;
  private Login login;

  SweetAlertDialog progress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_comments);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(this);

    setTitle("Comentarios");

    gson = new Gson();

    Intent intent = getIntent();
    wid = intent.getIntExtra("wid", 0);

    login = Utils.getSessionInfo(this);
    BigInteger token = new BigInteger(Utils.getSaveToken(this));

    GetComment params = new GetComment();
    params.setUid(login.getId());
    params.setWid(wid.longValue());
    params.setToken(token);

    progress = new SweetAlertDialog(CommentsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
    progress.getProgressHelper().setBarColor(CommentsActivity.this.getResources().getColor(R.color.colorGreen));
    progress.setTitleText("Espere")
        .setContentText("Descargando comentarios e imágenes")
        .setCancelable(false);
    progress.show();

    Call<CommentResult> call = Utils.buildRetrofit(this).create(WSInterface.class).getComment(params);
    call.enqueue(new Callback<CommentResult>() {
      @Override
      public void onResponse(Call<CommentResult> call, Response<CommentResult> response) {
        CommentResult result = response.body();
        if (result.getStatus() == 0) {
          comment = result.getComment();
          etComments.setText(comment.getComment());
          etNotes.setText(comment.getNote());
          if (!TextUtils.isEmpty(comment.getImage1())) {
            imgUno.setImageBitmap(Utils.decodeBase64(comment.getImage1().replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "")));
          }
          if (!TextUtils.isEmpty(comment.getImage2())) {
            imgDos.setImageBitmap(Utils.decodeBase64(comment.getImage2().replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "")));
          }
          if (!TextUtils.isEmpty(comment.getImage3())) {
            imgTres.setImageBitmap(Utils.decodeBase64(comment.getImage3().replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "")));
          }
        } else {
          Toast.makeText(CommentsActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
        }

        progress.dismiss();

      }

      @Override
      public void onFailure(Call<CommentResult> call, Throwable t) {
        t.printStackTrace();
        progress.dismiss();
        Toast.makeText(CommentsActivity.this, "No se pudo obtener las comentarios", Toast.LENGTH_SHORT).show();
      }
    });

    if (!login.getProfile().contains("DRI")) {
      btnSave.setVisibility(View.GONE);
      etNotes.setEnabled(false);
      etComments.setEnabled(false);
    }

  }

  @Override
  public void onClick(View view) {
    finish();
  }

  @OnClick({R.id.imgUno, R.id.imgDos, R.id.imgTres, R.id.btnCancel, R.id.btnSave})
  public void OnClick(final View v) {

    switch (v.getId()) {
      case R.id.btnCancel: {
        finish();
      }
      break;
      case R.id.btnSave: {
        comment.setComment(TextUtils.htmlEncode(etComments.getText().toString().trim()));
        comment.setNote(TextUtils.htmlEncode(etNotes.getText().toString().trim()));

        AddCommentParam params = new AddCommentParam();
        params.setComment(comment.getComment());
        params.setWid(comment.getId_waybill());
        params.setUid(Utils.getSessionInfo(CommentsActivity.this).getId());
        params.setImage1(comment.getImage1());
        params.setImage2(comment.getImage2());
        params.setImage3(comment.getImage3());
        params.setNote(comment.getNote());
        params.setToken(new BigInteger(Utils.getSaveToken(CommentsActivity.this)));

        progress = new SweetAlertDialog(CommentsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        progress.getProgressHelper().setBarColor(CommentsActivity.this.getResources().getColor(R.color.colorGreen));
        progress.setTitleText("Espere")
            .setContentText("Agregando comentarios e imágenes");
        progress.show();

        Call<Result> call = Utils.buildRetrofit(this).create(WSInterface.class).addComment(params);
        call.enqueue(new Callback<Result>() {
          @Override
          public void onResponse(Call<Result> call, Response<Result> response) {
            progress.dismiss();
            Result result = response.body();
            if (result.getStatus() == 0) {
              progress.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
              progress.setTitleText("Éxito al agregar comentarios")
                  .setContentText("")
                  .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                      CommentsActivity.this.finish();
                    }
                  })
                  .show();
            } else {
              progress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
              progress.setTitleText("Error al agregar comentarios")
                  .setContentText("")
                  .show();
              Toast.makeText(CommentsActivity.this, "Error: " + result.getMessage() + "\nCódigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onFailure(Call<Result> call, Throwable t) {
            t.printStackTrace();
            progress.dismiss();
            Toast.makeText(CommentsActivity.this, "No se pudo obtener respuesta", Toast.LENGTH_SHORT).show();
          }
        });
      }
      break;
      default: {
        image = v.getId();
        String[] options = null;
        if (login.getProfile().contains("DRI")) {
          options = new String[]{"Ver imagen", "Usar cámara", "Usar galería"};
        } else {
          options = new String[]{"Ver imagen"};
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar opción")
            .setItems(options, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                  case 0: {
                    String base = "";
                    switch (v.getId()) {
                      case R.id.imgUno: {
                        base = comment.getImage1();
                      }
                      break;

                      case R.id.imgDos: {
                        base = comment.getImage2();
                      }
                      break;

                      case R.id.imgTres: {
                        base = comment.getImage3();
                      }
                      break;
                    }

                    if (!TextUtils.isEmpty(base)) {

                      ImageView imageView = new ImageView(v.getContext());
                      imageView.setCropToPadding(true);
                      imageView.setAdjustViewBounds(true);
                      imageView.setImageBitmap(Utils.decodeBase64(base.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "")));
                      AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                      builder.setView(imageView)
                          .create().show();
                    }
                  }
                  break;
                  case 1: {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                      if (ContextCompat.checkSelfPermission(CommentsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_OPTION);
                      } else {
                        Toast.makeText(CommentsActivity.this, "La aplicación no tiene permisos para usar la camara", Toast.LENGTH_SHORT).show();
                      }
                    } else {
                      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                      startActivityForResult(intent, CAMERA_OPTION);
                    }

                  }
                  break;
                  case 2: {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                      if (ContextCompat.checkSelfPermission(CommentsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        intent.setType("image/png, image/jpg");
                        startActivityForResult(Intent.createChooser(intent, "Seleccionar app de imagen"), GALERI_OPTION);
                      } else {
                        Toast.makeText(CommentsActivity.this, "La aplicación no tiene permisos para usar la galería", Toast.LENGTH_SHORT).show();
                      }
                    } else {
                      Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                      intent.setType("image/*");
                      startActivityForResult(Intent.createChooser(intent, "Seleccionar app de imagen"), GALERI_OPTION);
                    }
                  }
                  break;
                }
              }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            })
            .create()
            .show();
      }

    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      if (requestCode == GALERI_OPTION) {
        if (data != null) {

          Uri selectedImage = data.getData();
          try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            String base = Utils.encodeToBas64(bitmap, Bitmap.CompressFormat.JPEG, 100);

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
      } else if (requestCode == CAMERA_OPTION) {

        Bundle bundle = data.getExtras();
        Bitmap bmap = (Bitmap) bundle.get("data");

        if (bmap != null) {

          Log.d("BMAP", bmap.toString());

          ImageView img = null;
          switch (image) {
            case R.id.imgUno: {
              img = imgUno;
            }
            break;

            case R.id.imgDos: {
              img = imgDos;
            }
            break;

            case R.id.imgTres: {
              img = imgTres;
            }
            break;

          }

          img.setImageBitmap(bmap);
          String base = Utils.encodeToBas64(bmap, Bitmap.CompressFormat.JPEG, 100);

          switch (image) {
            case R.id.imgUno: {
              comment.setImage1(base);
            }
            break;

            case R.id.imgDos: {
              comment.setImage2(base);
            }
            break;

            case R.id.imgTres: {
              comment.setImage3(base);
            }
            break;

          }

        } else {
          Toast.makeText(CommentsActivity.this, "No se pudo obtener la imagen", Toast.LENGTH_SHORT).show();
        }

      }
    } else {
      Toast.makeText(CommentsActivity.this, "Imagen no cargada", Toast.LENGTH_SHORT).show();
    }

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if (progress != null) {
      progress.dismiss();
    }
    finish();
  }
}
