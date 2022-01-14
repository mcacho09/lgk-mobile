package app.logistikappbeta.com.logistikapp.Results;

import app.logistikappbeta.com.logistikapp.POJOs.SubAlmacenInfo;

/**
 * Created by danie on 10/03/2017.
 */

public class GetSubAlmacenInfoResult extends Result {

  private SubAlmacenInfo subAlmacenInfo;

  public SubAlmacenInfo getSubAlmacenInfo() {
    return subAlmacenInfo;
  }

  public void setSubAlmacenInfo(SubAlmacenInfo subAlmacenInfo) {
    this.subAlmacenInfo = subAlmacenInfo;
  }
}
