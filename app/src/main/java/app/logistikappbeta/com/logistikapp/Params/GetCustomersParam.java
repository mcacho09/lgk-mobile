package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danie on 20/12/2016.
 */

public class GetCustomersParam {

  private int uid;
  private int rid;
  private BigInteger token;

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public int getRid() {
    return rid;
  }

  public void setRid(int rid) {
    this.rid = rid;
  }

  public BigInteger getToken() {
    return token;
  }

  public void setToken(BigInteger token) {
    this.token = token;
  }
}
