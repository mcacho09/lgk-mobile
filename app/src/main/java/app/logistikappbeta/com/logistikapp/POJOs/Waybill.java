package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 27/07/2016.
 */
public class Waybill {

  private int wid;
  private int sid;
  private String name;
  private String shelf;
  private String status;
  private String checkin;
  private String outrange;
  private double latitude;
  private double longitude;
  private int comments;
  private int images;
  private int orderby;
  private String email;
  private String address1;
  private String address2;
  private String retail;

  public int getWid() {
    return wid;
  }

  public void setWid(int wid) {
    this.wid = wid;
  }

  public int getSid() {
    return sid;
  }

  public void setSid(int sid) {
    this.sid = sid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCheckin() {
    return checkin;
  }

  public void setCheckin(String checkin) {
    this.checkin = checkin;
  }

  public String getOutrange() {
    return outrange;
  }

  public void setOutrange(String outrange) {
    this.outrange = outrange;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public int getComments() {
    return comments;
  }

  public void setComments(int comments) {
    this.comments = comments;
  }

  public int getImages() {
    return images;
  }

  public void setImages(int images) {
    this.images = images;
  }

  public int getOrderby() {
    return orderby;
  }

  public void setOrderby(int orderby) {
    this.orderby = orderby;
  }

  public String getShelf() {
    return shelf;
  }

  public void setShelf(String shelf) {
    this.shelf = shelf;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getRetail() {
    return retail;
  }

  public void setRetail(String retail) {
    this.retail = retail;
  }
}
