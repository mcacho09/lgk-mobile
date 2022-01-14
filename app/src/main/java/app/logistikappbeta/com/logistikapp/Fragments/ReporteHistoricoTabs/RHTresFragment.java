package app.logistikappbeta.com.logistikapp.Fragments.ReporteHistoricoTabs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.logistikappbeta.com.logistikapp.POJOs.Waybill;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RHTresFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.fMap)
    MapView mapView;
    GoogleMap gMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rhtres, container, false);
        ButterKnife.bind(this, v);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        MapsInitializer.initialize(this.getContext());
        mapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (gMap != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    gMap.setMyLocationEnabled(true);

                } else {
                    Toast.makeText(getContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                }

            } else {
                gMap.setMyLocationEnabled(true);
            }

        }
    }


    private Bitmap createStoreMarker(String marker_text, int color) {
        View markerLayout = getLayoutInflater().inflate(R.layout.view_custom_marker, null);

        ImageView markerImage = (ImageView) markerLayout.findViewById(R.id.marker_image);
        TextView markerOrder = (TextView) markerLayout.findViewById(R.id.marker_text);
        markerImage.setImageResource(R.drawable.ic_place);
        markerImage.setColorFilter(color);
        markerOrder.setText(marker_text);

        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);
        return bitmap;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
        String content = getArguments().getString("stores");
        try {
            JSONArray jsonArray = new JSONArray(content);
            int size = jsonArray.length();
            Gson gson = new Gson();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Waybill waybill = gson.fromJson(jsonObject.toString(), Waybill.class);
                MarkerOptions markerOptions = new MarkerOptions();
                if (waybill.getOutrange() != null && waybill.getOutrange().equals("S")) {
                    //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createStoreMarker(waybill.getOrderby() + "", Color.parseColor("#ff9900"))));
                } else if (waybill.getStatus().equals("S")) {
                    //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createStoreMarker(waybill.getOrderby() + "", Color.parseColor("#009e0f"))));
                } else {
                    //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createStoreMarker(waybill.getOrderby() + "", Color.parseColor("#cf2a27"))));
                }

                markerOptions.title("(" + waybill.getOrderby() + ") " + waybill.getName());
                markerOptions.position(new LatLng(waybill.getLatitude(), waybill.getLongitude()));
                gMap.addMarker(markerOptions);
            }
        } catch (JSONException ex) {
            Log.e("Logistikapp", ex.getMessage());
        }
    }
}
