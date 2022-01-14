package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Store;

/**
 * Created by danie on 25/01/2017.
 */

public class CRMResult extends Result {

  private ArrayList<Store> stores;

  public ArrayList<Store> getStores() {
    return stores;
  }

  public void setStores(ArrayList<Store> stores) {
    this.stores = stores;
  }
}
