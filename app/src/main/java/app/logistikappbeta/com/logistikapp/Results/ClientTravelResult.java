package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.AddClient;

/**
 * Created by danie on 26/01/2017.
 */

public class ClientTravelResult extends Result {

  private ArrayList<AddClient> stores;

  public ArrayList<AddClient> getStores() {
    return stores;
  }

  public void setStores(ArrayList<AddClient> stores) {
    this.stores = stores;
  }
}
