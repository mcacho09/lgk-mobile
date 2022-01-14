package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Login;
import app.logistikappbeta.com.logistikapp.POJOs.ProductPV;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danie on 19/08/2016.
 */
public class PVTresAdapter extends RecyclerView.Adapter<PVTresAdapter.VHPVTres> {

  private ArrayList<ProductPV> list;
  private Login login;
  private String search;

  public PVTresAdapter(ArrayList<ProductPV> list, Login login) {
    this.list = list;
    this.login = login;
    this.search = "";
  }

  @Override
  public VHPVTres onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_pvuno, parent, false);
    return new VHPVTres(v);
  }

  @Override
  public void onBindViewHolder(VHPVTres holder, int position) {
    ProductPV p = list.get(position);
    holder.setData(p);
  }

  @Override
  public int getItemCount() {
    return this.list.size();
  }

  public void setSearch(String search) {
    this.search = search;
    this.reset();
    this.setVisible();
  }

  public void reset() {
    Log.d("BUSQUEDA", "REINICIANDO");
    for (ProductPV p : this.list) {
      p.setVisible(true);
    }
  }

  public void setVisible() {
    for (ProductPV p : this.list) {
      p.setVisible(isVisible(p, search));
    }
  }

  public int getNoVisiblesItems() {
    int i = 0;

    for (ProductPV p : this.list) {
      i += p.getVisible() ? 1 : 0;
    }

    return i;
  }

  boolean isVisible(ProductPV p, String search) {
    return (p.getCode().toLowerCase().contains(search.toLowerCase()) || p.getName_short().toLowerCase().contains(search.toLowerCase()));
  }

  protected class VHPVTres extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    @BindView(R.id.tvQty)
    TextView tvQty;
    @BindView(R.id.imgProductAdd)
    ImageView imgProductAdd;
    @BindView(R.id.imgProductLess)
    ImageView imgProductLess;
    @BindView(R.id.imgProduct)
    ImageView imgProduct;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvShortName)
    TextView tvShortName;
    @BindView(R.id.tvLongName)
    TextView tvLongName;
    @BindView(R.id.tvSug)
    TextView tvSug;
    @BindView(R.id.tvSale)
    TextView tvSale;
    @BindView(R.id.tvType)
    TextView tvType;

    DecimalFormat df;

    public VHPVTres(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      df = new DecimalFormat("0.00");
      df.setMaximumIntegerDigits(999999999);
      imgProduct.setVisibility(View.GONE);
      itemView.setOnLongClickListener(this);
    }

    public void setData(ProductPV p) {
      if (p.getQtyproducts() == 0) {
        tvQty.setBackgroundResource(R.drawable.circle_shape_disabled);
        imgProductLess.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorGray));
      } else {
        tvQty.setBackgroundResource(R.drawable.circle_shape_no_aprove);
        imgProductLess.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorRed));
      }
      tvQty.setText("" + p.getQtyproducts());
      tvCode.setText(p.getCode());
      tvShortName.setText(p.getName_short());
      tvLongName.setText(p.getName_long());
      tvSug.setText("$" + p.getPrice_sale());
      tvSale.setText("$" + p.getSale());

      if (p.getVisible()) {
        Log.d("BUSQUEDA", "Visible " + p.getName_short());
        itemView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      } else {
        Log.d("BUSQUEDA", "No Visible " + p.getName_short());
        itemView.getLayoutParams().height = 0;
      }

      tvType.setText(p.getProduct_type_temp().contentEquals("PCS") ? "Piezas" : "Cajas");
    }

    @OnClick({R.id.tvQty, R.id.imgProductAdd, R.id.tvSug, R.id.imgProductLess})
    public void OnClick(final View v) {
      switch (v.getId()) {
        case R.id.tvQty: {

          final EditText etQty = new EditText(v.getContext());
          etQty.setInputType(InputType.TYPE_CLASS_NUMBER);
          etQty.setText("" + list.get(getLayoutPosition()).getQtyproducts());
          etQty.setHint("Cantidad de productos");

          if (etQty.requestFocus()) {
            InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
              inputManager.toggleSoftInputFromWindow(etQty.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            }
          }

          AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
          builder.create();
          builder.setView(etQty).setTitle("Cantidad de productos")
              .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  if (!etQty.getText().toString().isEmpty()) {
                    tvQty.setText(etQty.getText());
                    ProductPV pv = list.get(getLayoutPosition());

                    if (etQty.getText().toString().equals("0")) {

                      if (pv.getQtyproducts() > 0) {
                        pv.setQtyproducts(0l);
                        tvQty.setText("0");
                        list.remove(getLayoutPosition());
                        list.add(getLayoutPosition(), pv);
                        pv.setSale(pv.getQtyproducts() * pv.getPrice_sale());
                      }

                      if (list.get(getLayoutPosition()).getQtyproducts() == 0) {
                        tvQty.setBackgroundResource(R.drawable.circle_shape_disabled);
                        imgProductLess.setColorFilter(v.getContext().getResources().getColor(R.color.colorGray));
                        pv.setSale(0d);
                      }

                    } else {

                      pv.setQtyproducts(new Long(etQty.getText().toString()));
                      tvQty.setText("" + etQty.getText());
                      pv.setSale(pv.getQtyproducts() * pv.getPrice_sale());
                      list.remove(getLayoutPosition());
                      list.add(getLayoutPosition(), pv);
                      imgProductLess.setColorFilter(v.getContext().getResources().getColor(R.color.colorRed));
                      tvQty.setBackgroundResource(R.drawable.circle_shape_aprove);
                    }

                    notifyDataSetChanged();
                  }

                }
              }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
            }
          }).show();

        }
        break;
        case R.id.imgProductAdd: {
          ProductPV pv = list.get(getLayoutPosition());

          pv.setQtyproducts(pv.getQtyproducts() + 1);
          tvQty.setText("" + pv.getQtyproducts());
          list.remove(getLayoutPosition());
          list.add(getLayoutPosition(), pv);
          imgProductLess.setColorFilter(v.getContext().getResources().getColor(R.color.colorRed));
          tvQty.setBackgroundResource(R.drawable.circle_shape_aprove);
          pv.setSale(pv.getQtyproducts() * pv.getPrice_sale());

          notifyDataSetChanged();
        }
        break;
        case R.id.imgProductLess: {
          ProductPV pv = list.get(getLayoutPosition());
          if (pv.getQtyproducts() > 0) {
            pv.setQtyproducts(pv.getQtyproducts() - 1);
            tvQty.setText("" + pv.getQtyproducts());
            list.remove(getLayoutPosition());
            list.add(getLayoutPosition(), pv);
            pv.setSale(pv.getQtyproducts() * pv.getPrice_sale());
          }

          if (list.get(getLayoutPosition()).getQtyproducts() == 0) {
            tvQty.setBackgroundResource(R.drawable.circle_shape_disabled);
            imgProductLess.setColorFilter(v.getContext().getResources().getColor(R.color.colorGray));
            pv.setSale(0d);
          }
          notifyDataSetChanged();
        }
        break;
        case R.id.tvSug: {
          final EditText etQty = new EditText(v.getContext());
          etQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
          etQty.setText(df.format(list.get(getLayoutPosition()).getPrice_sale()));
          etQty.setHint("Nuevo precio");
          if (etQty.requestFocus()) {
            InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
              inputManager.toggleSoftInputFromWindow(etQty.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            }
          }
          AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
          builder.create();
          builder.setView(etQty).setTitle("Establecer nuevo precio")
              .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  if (!etQty.getText().toString().isEmpty() && !TextUtils.equals(etQty.getText(), ".") && !TextUtils.equals(etQty.getText(), "-")) {

                    ProductPV pv = list.get(getLayoutPosition());
                    pv.setPrice_sale(new Double(etQty.getText().toString()));
                    pv.setSale(pv.getQtyproducts() * pv.getPrice_sale());
                    list.remove(getLayoutPosition());
                    list.add(getLayoutPosition(), pv);
                    notifyDataSetChanged();
                  }

                }
              }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
            }
          }).show();
        }
        break;
        /*case R.id.imgProduct: {
          final ImageView imageView = new ImageView(v.getContext());
          imageView.setCropToPadding(true);
          imageView.setAdjustViewBounds(true);
          imageView.setImageResource(R.drawable.default_product);
          final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
          builder.setView(imageView)
              .create();

          GetImageProductParam param = new GetImageProductParam();
          param.setPid(list.get(getLayoutPosition()).getId_product());
          param.setToken(new BigInteger(Utils.getSaveToken(v.getContext())));
          param.setUid(Utils.getSessionInfo(v.getContext()).getId());

          Call<ImageProductResult> call = Utils.buildRetrofit(v.getContext()).create(WSInterface.class).getImageProduct(param);
          call.enqueue(new Callback<ImageProductResult>() {
            @Override
            public void onResponse(Call<ImageProductResult> call, Response<ImageProductResult> response) {
              ImageProductResult result = response.body();
              String image = result.getImage();
              if (image != null) {
                if (image.trim().isEmpty() || image.contains("img/products/default.png")) {
                  Toast.makeText(v.getContext(), "El producto no cuenta con imagen", Toast.LENGTH_SHORT).show();
                } else {
                  imageView.setImageBitmap(decodeBase64(image.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "")));
                  builder.show();
                }
              } else {
                Toast.makeText(v.getContext(), "El producto no cuenta con imagen", Toast.LENGTH_SHORT).show();
              }
            }

            @Override
            public void onFailure(Call<ImageProductResult> call, Throwable t) {
              t.printStackTrace();
              Toast.makeText(v.getContext(), "No se pudo obtener la imagen del producto", Toast.LENGTH_SHORT).show();
            }
          });
        }
        break;*/
      }
    }

    @Override
    public boolean onLongClick(View view) {

      final ProductPV p = list.get(getLayoutPosition());

      if (p.getProduct_type().contentEquals("PKG")) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
        dialog
            .setTitle("Selecciona el tipo de cambio")
            .setSingleChoiceItems(new String[]{"Pieza", "Caja"}, (p.getProduct_type_temp().contentEquals("PCS") ? 0 : 1), new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                list.get(getLayoutPosition()).setProduct_type_temp(i == 0 ? "PCS" : "PKG");
                list.get(getLayoutPosition()).setPrice_sale(i == 0 ? p.getPrice_sale_piece() : p.getPrice_sug());
                notifyDataSetChanged();
              }
            })
            .setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            })
            .create()
            .show();

      }

      return true;
    }
  }

}
