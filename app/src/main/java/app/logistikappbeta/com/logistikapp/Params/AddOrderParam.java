package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.OrderDetail;

/**
 * Created by danie on 20/08/2016.
 */
public class AddOrderParam {

  private Long uid;
  private Long stid;
  private BigInteger token;
  private String status;
  private ArrayList<OrderDetail> products_VTA;
  private ArrayList<OrderDetail> products_CHG;
  private ArrayList<OrderDetail> products_DEV;
  private Long wid;
  private Double latitude;
  private Double longitude;

  public Long getUid() {
    return uid;
  }

  public void setUid(Long uid) {
    this.uid = uid;
  }

  public Long getStid() {
    return stid;
  }

  public void setStid(Long stid) {
    this.stid = stid;
  }

  public BigInteger getToken() {
    return token;
  }

  public void setToken(BigInteger token) {
    this.token = token;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ArrayList<OrderDetail> getProducts_VTA() {
    return products_VTA;
  }

  public void setProducts_VTA(ArrayList<OrderDetail> products_VTA) {
    this.products_VTA = products_VTA;
  }

  public ArrayList<OrderDetail> getProducts_CHG() {
    return products_CHG;
  }

  public void setProducts_CHG(ArrayList<OrderDetail> products_CHG) {
    this.products_CHG = products_CHG;
  }

  public ArrayList<OrderDetail> getProducts_DEV() {
    return products_DEV;
  }

  public void setProducts_DEV(ArrayList<OrderDetail> products_DEV) {
    this.products_DEV = products_DEV;
  }

  public Long getWid() {
    return wid;
  }

  public void setWid(Long wid) {
    this.wid = wid;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }
}
