package app.logistikappbeta.com.logistikapp.Fragments.ReporteHistoricoTabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import app.logistikappbeta.com.logistikapp.POJOs.Travel;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RHUnoFragment extends Fragment {

    @BindView(R.id.tvNombre) TextView tvNombre;
    @BindView(R.id.tvAsignado) TextView tvAsignado;
    @BindView(R.id.tvProgramado) TextView tvProgramado;
    @BindView(R.id.tvIniciado) TextView tvIniciado;
    @BindView(R.id.tvFinalizado) TextView tvFinalizado;
    @BindView(R.id.tvClientes) TextView tvClientes;
    @BindView(R.id.imgAvance) ImageView imgAvance;
    @BindView(R.id.tvTAvance) TextView tvTAvance;
    @BindView(R.id.tvAvance) TextView tvAvance;
    @BindView(R.id.tvCheckins) TextView tvCheckins;
    @BindView(R.id.tvFueraRango) TextView tvFueraRango;
    @BindView(R.id.imgComentarios) ImageView imgComentarios;
    @BindView(R.id.tvTComentarios) TextView tvTComentarios;
    @BindView(R.id.tvComentarios) TextView tvComentarios;
    @BindView(R.id.imgImagenes) ImageView imgImagenes;
    @BindView(R.id.tvTImagenes) TextView tvTImagenes;
    @BindView(R.id.tvImagenes) TextView tvImagenes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rhuno, container, false);
        ButterKnife.bind(this, v);

        String content = getArguments().getString("travel");

        Gson gson = new Gson();
        Travel travel = gson.fromJson(content, Travel.class);

        tvNombre.setText(travel.getName());
        tvAsignado.setText(travel.getDriver());
        tvProgramado.setText(travel.getScheduled());

        SimpleDateFormat sdfIn = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM HH:mm");

        try{
            tvIniciado.setText(sdfOut.format(sdfIn.parse(travel.getStarted())));
            tvFinalizado.setText(sdfOut.format(sdfIn.parse(travel.getFinished())));
        }catch (Exception ex){
            Log.e("Logistikapp", "Error al parsear\n" + ex.getMessage());
        }

        tvClientes.setText("" + travel.getStores());

        DecimalFormat df = new DecimalFormat("0.00");
        tvAvance.setText(df.format(travel.getProgress()) + "%");
        if (travel.getProgress() > 79.99 && travel.getProgress() < 89.99){
            imgAvance.setColorFilter(getContext().getResources().getColor(R.color.colorOrange));
            imgAvance.setImageResource(R.drawable.ic_sentiment_neutral);
            tvTAvance.setTextColor(getContext().getResources().getColor(R.color.colorOrange));
        }else if (travel.getProgress() > 89.99){
            imgAvance.setColorFilter(getContext().getResources().getColor(R.color.colorBlue));
            imgAvance.setImageResource(R.drawable.ic_sentiment_satisfied);
            tvTAvance.setTextColor(getContext().getResources().getColor(R.color.colorBlue));
        }

        tvCheckins.setText("" + travel.getCheckinok());
        tvFueraRango.setText("" + travel.getCheckinerr());

        if (travel.getComments() > 0){
            imgComentarios.setColorFilter(getContext().getResources().getColor(R.color.colorBlue));
            tvTComentarios.setTextColor(getContext().getResources().getColor(R.color.colorBlue));
            tvComentarios.setText("" + travel.getComments());
        }

        if (travel.getImages() > 0){
            imgImagenes.setColorFilter(getContext().getResources().getColor(R.color.colorBlue));
            tvTImagenes.setTextColor(getContext().getResources().getColor(R.color.colorBlue));
            tvImagenes.setText("" + travel.getImages());
        }

        return v;
    }

}
