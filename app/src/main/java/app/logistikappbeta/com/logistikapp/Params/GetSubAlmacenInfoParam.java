package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danie on 22/08/2016.
 */
public class GetSubAlmacenInfoParam {

  private Long uid;

  private BigInteger token;

  private Long rId;

  private Long aId;

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

  public Long getrId() {
    return rId;
  }

  public void setrId(Long rId) {
    this.rId = rId;
  }

  public Long getaId() {
    return aId;
  }

  public void setaId(Long aId) {
    this.aId = aId;
  }
}
