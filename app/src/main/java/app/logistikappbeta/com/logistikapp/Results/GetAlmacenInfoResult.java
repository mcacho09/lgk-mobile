package app.logistikappbeta.com.logistikapp.Results;

import app.logistikappbeta.com.logistikapp.POJOs.AlmacenInfo;

/**
 * Created by danie on 10/03/2017.
 */

public class GetAlmacenInfoResult extends Result {

  private AlmacenInfo info;

  public AlmacenInfo getInfo() {
    return info;
  }

  public void setInfo(AlmacenInfo info) {
    this.info = info;
  }
}
