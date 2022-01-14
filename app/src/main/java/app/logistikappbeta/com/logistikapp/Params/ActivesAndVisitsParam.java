package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danielmarin on 13/11/17.
 */

public class ActivesAndVisitsParam {

  private Long uid;

  private BigInteger token;

  private Long ids;

  private String date;

  public Long getUid() {
    return uid;
  }

  public void setUid(Long uid) {
    this.uid = uid;
  }

  public BigInteger getToken() {
    return token;
  }

  public void setToken(BigInteger token) {
    this.token = token;
  }

  public Long getIds() {
    return ids;
  }

  public void setIds(Long ids) {
    this.ids = ids;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

}
