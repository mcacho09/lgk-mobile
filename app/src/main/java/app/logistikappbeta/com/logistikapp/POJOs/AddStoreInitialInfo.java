package app.logistikappbeta.com.logistikapp.POJOs;

import java.util.ArrayList;

/**
 * Created by danielmarin on 10/06/17.
 */

public class AddStoreInitialInfo {

  private ArrayList<State> states;
  private ArrayList<City> cities;
  private ArrayList<Category> categories;
  private ArrayList<Retail> retails;
  private StoreInfo store;

  public ArrayList<State> getStates() {
    return states;
  }

  public void setStates(ArrayList<State> states) {
    this.states = states;
  }

  public ArrayList<City> getCities() {
    return cities;
  }

  public void setCities(ArrayList<City> cities) {
    this.cities = cities;
  }

  public ArrayList<Category> getCategories() {
    return categories;
  }

  public void setCategories(ArrayList<Category> categories) {
    this.categories = categories;
  }

  public ArrayList<Retail> getRetails() {
    return retails;
  }

  public void setRetails(ArrayList<Retail> retails) {
    this.retails = retails;
  }

  public StoreInfo getStore() {
    return store;
  }

  public void setStore(StoreInfo store) {
    this.store = store;
  }
}
