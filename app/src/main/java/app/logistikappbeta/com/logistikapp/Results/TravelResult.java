package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Travel;

/**
 * Created by danie on 25/01/2017.
 */

public class TravelResult extends Result {

  private ArrayList<Travel> travels;

  public ArrayList<Travel> getTravels() {
    return travels;
  }

  public void setTravels(ArrayList<Travel> travels) {
    this.travels = travels;
  }

}
