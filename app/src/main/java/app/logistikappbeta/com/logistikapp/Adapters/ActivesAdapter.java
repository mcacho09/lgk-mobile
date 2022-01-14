package app.logistikappbeta.com.logistikapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Active;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danielmarin on 10/11/17.
 */

public class ActivesAdapter extends RecyclerView.Adapter<ActivesAdapter.VHActivesAdapter> {

  private Context context;
  private ArrayList<Active> list;

  public ActivesAdapter(Context context, ArrayList<Active> list) {
    this.context = context;
    this.list = list;
  }

  @Override
  public VHActivesAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_active, parent, false);
    return new VHActivesAdapter(v);
  }

  @Override
  public void onBindViewHolder(VHActivesAdapter holder, int position) {
    holder.setData(list.get(position));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  protected class VHActivesAdapter extends RecyclerView.ViewHolder {

    @BindView(R.id.tvLastVisited)
    TextView tvLastVisited;
    @BindView(R.id.ivCheck)
    ImageView ivCheck;
    @BindView(R.id.tvCode)
    TextView tvCode;

    SimpleDateFormat sdf;


    public VHActivesAdapter(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      this.sdf = new SimpleDateFormat("dd/MM/yyyy");
    }

    public void setData(Active active) {
      if (active.getLast_visited() != null) {
        tvLastVisited.setText(context.getString(R.string.last_visited, active.getLast_visited()));
      } else {
        tvLastVisited.setText(context.getString(R.string.last_visited, "NA"));
      }
      if (active.getVisited().contentEquals("S")) {
        ivCheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle));
        if (active.getProblems().contentEquals("S")) {
          ivCheck.setColorFilter(ContextCompat.getColor(context, R.color.colorOrange));
        } else {
          ivCheck.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen));
        }
      } else {
        ivCheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cancel));
        ivCheck.setColorFilter(ContextCompat.getColor(context, R.color.colorRed));
      }

      tvCode.setText(active.getCode());
    }

    @OnClick({R.id.ivImage})
    public void click(View v) {
      switch (v.getId()) {
        case R.id.ivImage: {
          String img = list.get(getLayoutPosition()).getImage();
          if (img != null && !img.trim().isEmpty()) {
            final ImageView imageView = new ImageView(v.getContext());
            imageView.setCropToPadding(true);
            imageView.setAdjustViewBounds(true);
            imageView.setImageResource(R.drawable.default_product);
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setView(imageView)
                .create();

            imageView.setImageBitmap(decodeBase64(img.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "")));
            builder.show();

          }
        }
        break;
      }
    }
  }

  public Bitmap decodeBase64(String code) {
    byte[] bytes = Base64.decode(code, Base64.DEFAULT);
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
  }

}
