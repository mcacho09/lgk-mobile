package app.logistikappbeta.com.logistikapp.Results;

import app.logistikappbeta.com.logistikapp.POJOs.Qty;

/**
 * Created by danie on 23/01/2017.
 */

public class NotificationsResult extends Result {

  private Qty qty;

  public Qty getQty() {
    return qty;
  }

  public void setQty(Qty qty) {
    this.qty = qty;
  }
}
