package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danie on 27/01/2017.
 */

public class GetUserPositionParam {

  private Long uid;
  private BigInteger token;
  private Long upid;

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

  public Long getUpid() {
    return upid;
  }

  public void setUpid(Long upid) {
    this.upid = upid;
  }
}
