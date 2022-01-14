package app.logistikappbeta.com.logistikapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import app.logistikappbeta.com.logistikapp.Interfaces.IBarcodeActives;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ActivesBarcodeFragment extends Fragment implements ZXingScannerView.ResultHandler {

  private ZXingScannerView mScannerView;
  private IBarcodeActives mIBarcode;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    mScannerView = new ZXingScannerView(getActivity());

    List<BarcodeFormat> list = new ArrayList<>();
    list.add(BarcodeFormat.EAN_13);
    list.add(BarcodeFormat.EAN_8);
    list.add(BarcodeFormat.CODE_39);
    list.add(BarcodeFormat.CODE_93);
    list.add(BarcodeFormat.CODE_128);
    list.add(BarcodeFormat.QR_CODE);
    mScannerView.setFormats(list);

    return mScannerView;
  }

  @Override
  public void onResume() {
    super.onResume();
    mScannerView.setResultHandler(this);
    mScannerView.setAspectTolerance(0.5f);
    mScannerView.startCamera();
  }

  public void flash() {
    this.mScannerView.toggleFlash();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    this.mIBarcode = (IBarcodeActives) context;

  }

  @Override
  public void onStop() {
    super.onStop();
    this.mScannerView.stopCamera();
  }

  @Override
  public void onPause() {
    super.onPause();
    this.mScannerView.stopCamera();
  }

  @Override
  public void handleResult(Result result) {
    if (this.mIBarcode != null) {
      mIBarcode.sendBarcode(result.getText());
    }
    mScannerView.resumeCameraPreview(this);
  }
}
