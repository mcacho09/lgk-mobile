package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Waybill;

/**
 * Created by danie on 25/01/2017.
 */

public class TOWResult extends Result {

  private ArrayList<Waybill> stores;

  public ArrayList<Waybill> getStores() {
    return stores;
  }

  public void setStores(ArrayList<Waybill> stores) {
    this.stores = stores;
  }

}
