package app.logistikappbeta.com.logistikapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NuevaTareaActivity extends AppCompatActivity {

  @BindView(R.id.rbAlta)
  RadioButton rbAlta;
  @BindView(R.id.rbMedia)
  RadioButton rbMedia;
  @BindView(R.id.rbBaja)
  RadioButton rbBaja;

  boolean createTask = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nueva_tarea);
    ButterKnife.bind(this);
    setTitle("Nueva tarea");
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent i = new Intent();
    i.putExtra("result", createTask);
    setResult(RESULT_OK, i);
    finish();
  }

  @OnClick({R.id.btnCancelar, R.id.btnGuardar})
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.btnCancelar: {
        Intent i = new Intent();
        i.putExtra("result", createTask);
        setResult(createTask ? RESULT_OK : RESULT_CANCELED, i);
      }
      break;
      case R.id.btnGuardar: {
        createTask = true;
        Intent i = new Intent();
        i.putExtra("result", createTask);
        setResult(RESULT_OK, i);
      }
      break;
    }

    finish();

  }
}
