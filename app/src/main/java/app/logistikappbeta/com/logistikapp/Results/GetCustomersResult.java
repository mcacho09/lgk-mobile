package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Store;

/**
 * Created by danielmarin on 10/06/17.
 */

public class GetCustomersResult extends Result {

  private ArrayList<Store> customers;

  public ArrayList<Store> getCustomers() {
    return customers;
  }

  public void setCustomers(ArrayList<Store> customers) {
    this.customers = customers;
  }
}
