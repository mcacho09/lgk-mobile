package app.logistikappbeta.com.logistikapp.POJOs;

import java.math.BigInteger;

/**
 * Created by danie on 17/01/2017.
 */

public class AddCurrentPosition {

  private Long uid;
  private BigInteger token;
  private Double lat;
  private Double lng;
  private String created;

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

  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Double getLng() {
    return lng;
  }

  public void setLng(Double lng) {
    this.lng = lng;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }
}
