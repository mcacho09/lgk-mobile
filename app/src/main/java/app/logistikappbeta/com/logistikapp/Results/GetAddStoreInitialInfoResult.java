package app.logistikappbeta.com.logistikapp.Results;

import app.logistikappbeta.com.logistikapp.POJOs.AddStoreInitialInfo;

/**
 * Created by danielmarin on 10/06/17.
 */

public class GetAddStoreInitialInfoResult extends Result {

  private AddStoreInitialInfo data;

  public AddStoreInitialInfo getData() {
    return data;
  }

  public void setData(AddStoreInitialInfo data) {
    this.data = data;
  }
}
