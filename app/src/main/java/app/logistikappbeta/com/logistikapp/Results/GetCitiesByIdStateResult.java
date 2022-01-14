package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.City;

/**
 * Created by danielmarin on 10/06/17.
 */

public class GetCitiesByIdStateResult extends Result {

  private ArrayList<City> cities;

  public ArrayList<City> getCities() {
    return cities;
  }

  public void setCities(ArrayList<City> cities) {
    this.cities = cities;
  }
}
