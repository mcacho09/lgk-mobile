package app.logistikappbeta.com.logistikapp.POJOs;

import java.util.List;

/**
 * Created by danielmarin on 18/09/17.
 */

public class AlmacenInfo {

  private AlmacenAndProducts almacenInfo;
  private List<AlmacenInfoList> subalmacens;

  public AlmacenAndProducts getAlmacenInfo() {
    return almacenInfo;
  }

  public void setAlmacenInfo(AlmacenAndProducts almacenInfo) {
    this.almacenInfo = almacenInfo;
  }

  public List<AlmacenInfoList> getSubalmacens() {
    return subalmacens;
  }

  public void setSubalmacens(List<AlmacenInfoList> subalmacens) {
    this.subalmacens = subalmacens;
  }
}
