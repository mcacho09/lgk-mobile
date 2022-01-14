package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danielmarin on 10/06/17.
 */

public class GetReportByDriParam {

  private Long uid;
  private BigInteger token;
  private String profile;
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

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

}
