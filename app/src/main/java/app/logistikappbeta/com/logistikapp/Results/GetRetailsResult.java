package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Retail;

/**
 * Created by danielmarin on 10/06/17.
 */

public class GetRetailsResult extends Result {

  private ArrayList<Retail> retails;

  public ArrayList<Retail> getRetails() {
    return retails;
  }

  public void setRetails(ArrayList<Retail> retails) {
    this.retails = retails;
  }
}
