package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.AdminUserActivity;
import app.logistikappbeta.com.logistikapp.Interfaces.IUsuariosFragment;
import app.logistikappbeta.com.logistikapp.POJOs.Users;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danie on 20/12/2016.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.VHUsersAdapter> {

  private ArrayList<Users> list;
  private Context context;
  private IUsuariosFragment iUsuariosFragment;

  public UsersAdapter(ArrayList<Users> list, Context context, IUsuariosFragment iUsuariosFragment) {
    this.list = list;
    this.context = context;
    this.iUsuariosFragment = iUsuariosFragment;
  }

  @Override
  public VHUsersAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_users, parent, false);
    return new VHUsersAdapter(v);
  }

  @Override
  public void onBindViewHolder(VHUsersAdapter holder, int position) {
    holder.setUser(list.get(position));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  protected class VHUsersAdapter extends RecyclerView.ViewHolder {

    @BindView(R.id.imgSU)
    ImageView imgSU;
    @BindView(R.id.tvNombre)
    TextView tvNombre;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.imgActive)
    ImageView imgActive;
    @BindView(R.id.tvActive)
    TextView tvActive;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvJob)
    TextView tvJob;
    @BindView(R.id.btnLocate)
    ImageView btnLocate;

    public VHUsersAdapter(View v) {
      super(v);
      ButterKnife.bind(this, v);
      v.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (list.get(getLayoutPosition()).getProfile().contentEquals("SOP")) return;
          String[] options = new String[]{"Editar"};
          final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
          dialog.setTitle("Opciones").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              switch (i) {
                case 0: {
                  Intent intent = new Intent(context, AdminUserActivity.class);
                  intent.putExtra("id_user", list.get(getLayoutPosition()).getId_user());
                  context.startActivity(intent);
                }
                break;
                case 1: {

                }
                break;
              }
            }
          }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
            }
          }).create().show();
        }
      });
    }

    public void setUser(Users u) {
      imgSU.setVisibility(u.getSuperuser().contentEquals("S") ? View.VISIBLE : View.GONE);
      tvNombre.setText(u.getUsername());
      tvLogin.setText(u.getUserlogin());
      tvEmail.setText(u.getEmail());
      if (u.getActive().contentEquals("S")) {
        imgActive.setImageResource(R.drawable.ic_check);
        imgActive.setColorFilter(context.getResources().getColor(R.color.colorGreen));
        tvActive.setText("Activo");
      } else {
        imgActive.setImageResource(R.drawable.ic_cancel);
        imgActive.setColorFilter(context.getResources().getColor(R.color.colorRed));
        tvActive.setText("Inactivo");
      }
      tvPhone.setText(u.getPhone());
      tvJob.setText(u.getJob());
      btnLocate.setVisibility(u.getProfile().contentEquals("SOP")?View.GONE:View.VISIBLE);
    }

    @OnClick({R.id.btnLocate})
    public void click(View v) {

      switch (v.getId()) {
        case R.id.btnLocate: {
          iUsuariosFragment.showUbication(list.get(getLayoutPosition()).getId_user());
        }
        break;
      }

      /*PopupMenu popupMenu = new PopupMenu(context, v);
      popupMenu.getMenuInflater().inflate(R.menu.menu_pop_user, popupMenu.getMenu());
      popupMenu.show();
      popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

          return false;
        }
      });*/
    }

  }

}
