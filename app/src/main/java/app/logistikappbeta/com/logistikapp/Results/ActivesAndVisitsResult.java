package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.Active;

/**
 * Created by danielmarin on 13/11/17.
 */

public class ActivesAndVisitsResult extends Result {

  private ArrayList<Active> actives;

  public ArrayList<Active> getActives() {
    return actives;
  }

  public void setActives(ArrayList<Active> actives) {
    this.actives = actives;
  }
}
