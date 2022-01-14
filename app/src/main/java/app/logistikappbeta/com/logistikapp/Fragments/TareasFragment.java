package app.logistikappbeta.com.logistikapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.Adapters.TareasAdapter;
import app.logistikappbeta.com.logistikapp.NuevaTareaActivity;
import app.logistikappbeta.com.logistikapp.POJOs.Task;
import app.logistikappbeta.com.logistikapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TareasFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.rvTareas) RecyclerView rvTareas;
    @BindView(R.id.sRefresh) SwipeRefreshLayout sRefresh;
    @BindView(R.id.rbTodo) RadioButton rvTodo;
    @BindView(R.id.rbAlta) RadioButton rbAlta;
    @BindView(R.id.rbMedia) RadioButton rbMedia;
    @BindView(R.id.rbBaja) RadioButton rbBaja;

    MyAsyncTask asyncTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tareas, container, false);
        ButterKnife.bind(this, v);

        asyncTask = new MyAsyncTask();
        asyncTask.execute();

        rvTareas.setHasFixedSize(true);
        rvTareas.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL, false));
        rvTareas.setItemAnimator(new DefaultItemAnimator());
        rvTareas.setAdapter(new TareasAdapter(new ArrayList<Task>()));

        sRefresh.setOnRefreshListener(this);

        return v;
    }

    @Override
    public void onRefresh() {
        asyncTask.execute();
    }

    @OnClick({R.id.rbTodo,R.id.rbAlta,R.id.rbMedia,R.id.rbBaja,R.id.btnNueva})
    public void onClick(View v){
        if (v.getId() == R.id.btnNueva){
            startActivityForResult(new Intent(getActivity(),NuevaTareaActivity.class), 1);
        }else{
            switch (v.getId()){
                case R.id.rbTodo:{

                }break;
                case R.id.rbAlta:{

                }break;
                case R.id.rbMedia:{

                }break;
                case R.id.rbBaja:{

                }break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                asyncTask.execute();
            }
        }
    }

    protected class MyAsyncTask extends AsyncTask<String, Integer, String> {

        SweetAlertDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            progress.getProgressHelper().setBarColor(getContext().getResources().getColor(R.color.colorGreen));
            progress.setTitleText("Espere")
                    .setContentText("Descargando tareas")
                    .setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
        }
    }

}
