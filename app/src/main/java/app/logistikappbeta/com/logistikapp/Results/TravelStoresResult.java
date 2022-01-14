package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Travel;
import app.logistikappbeta.com.logistikapp.POJOs.Waybill;

/**
 * Created by danie on 25/01/2017.
 */

public class TravelStoresResult extends Result {

  private Travel travel;
  private ArrayList<Waybill> stores;

  public Travel getTravel() {
    return travel;
  }

  public void setTravel(Travel travel) {
    this.travel = travel;
  }

  public ArrayList<Waybill> getStores() {
    return stores;
  }

  public void setStores(ArrayList<Waybill> stores) {
    this.stores = stores;
  }

}
