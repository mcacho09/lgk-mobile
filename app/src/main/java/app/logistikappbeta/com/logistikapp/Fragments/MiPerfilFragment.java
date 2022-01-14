package app.logistikappbeta.com.logistikapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.math.BigInteger;

import app.logistikappbeta.com.logistikapp.Class.Utils;
import app.logistikappbeta.com.logistikapp.Interfaces.WSInterface;
import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.User;
import app.logistikappbeta.com.logistikapp.Params.GetUserParam;
import app.logistikappbeta.com.logistikapp.R;
import app.logistikappbeta.com.logistikapp.Results.UserResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiPerfilFragment extends Fragment {

  private static final String TAG = "Logistikapp";

  @BindView(R.id.imgPerfil)
  ImageView imgPerfil;
  @BindView(R.id.tvName)
  TextView tvName;
  @BindView(R.id.tvUserName)
  TextView tvUserName;
  @BindView(R.id.tvPerfil)
  TextView tvPerfil;
  @BindView(R.id.tvEmail)
  TextView tvEmail;
  @BindView(R.id.tvTel)
  TextView tvTel;
  @BindView(R.id.tvTime)
  TextView tvTime;
  @BindView(R.id.lySuper)
  LinearLayout lySuper;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
    ButterKnife.bind(this, v);

    Login cliente = Utils.getSessionInfo(getContext());

    GetUserParam param = new GetUserParam();
    param.setUid(cliente.getId().intValue());
    param.setToken(new BigInteger(Utils.getSaveToken(getContext())));

    //String params = new Gson().toJson(param);
    //Toast.makeText(getContext(), "Token " + Utils.getSaveToken(getContext()), Toast.LENGTH_SHORT).show();

    final SweetAlertDialog progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
    progress.getProgressHelper().setBarColor(getContext().getResources().getColor(R.color.colorGreen));
    progress.setTitleText("Espere")
        .setContentText("Descargando perfil")
        .setCancelable(false);
    progress.show();
    Call<UserResult> result = Utils.buildRetrofit(getContext()).create(WSInterface.class).getUser(param);
    result.enqueue(new Callback<UserResult>() {
      @Override
      public void onResponse(Call<UserResult> call, Response<UserResult> response) {
        UserResult result = response.body();
        if (result.getStatus() == 0) {
          User user = result.getUser();
          tvName.setText(user.getName());
          tvUserName.setText(user.getLogin());
          String profile = "";
          if (user.getProfile().startsWith("SUP"))
            profile = "Administración";
          else if (user.getProfile().startsWith("DRI"))
            profile = "Vendedor";
          else if (user.getProfile().equals("LGK"))
            profile = "Logistica";

          lySuper.setVisibility(user.getSuperuser().equals("S") ? View.VISIBLE : View.GONE);
          tvPerfil.setText(profile);
          tvEmail.setText(user.getEmail());
          String tel = "";
          String ubi_time = "0";

          if (user.getPhone1() != null)
            tel += user.getPhone1();
          if (user.getPhone2() != null)
            tel += " " + user.getPhone2();

          if (tel.equals(" "))
            tel = "000 000 0000";

          if(user.getUbi_time() != null && Integer.parseInt(user.getUbi_time())>0)
            ubi_time = " " + user.getUbi_time();

          tvTime.setText(ubi_time);
          tvTel.setText(tel);
        } else {
          Toast.makeText(getActivity(), "Error: " + result.getMessage() + "\nCodigo: " + result.getStatus(), Toast.LENGTH_SHORT).show();
        }

        progress.dismiss();
      }

      @Override
      public void onFailure(Call<UserResult> call, Throwable t) {
        t.printStackTrace();
        progress.dismiss();
      }
    });

    return v;
  }

}
