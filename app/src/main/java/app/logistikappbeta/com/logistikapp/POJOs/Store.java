package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 17/07/2016.
 */
public class Store {

  private int id;
  private String name;
  private String address1;
  private String address2;
  private int postal;
  private String code;
  private String shelf;
  private String retail;
  private String category;
  private double latitude;
  private double longitude;
  private String email;
  private Boolean visible;

  public Store() {
    this.visible = true;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddres1() {
    return address1;
  }

  public void setAddres1(String addres1) {
    this.address1 = addres1;
  }

  public String getAddres2() {
    return address2;
  }

  public void setAddres2(String addres2) {
    this.address2 = addres2;
  }

  public int getPostal() {
    return postal;
  }

  public void setPostal(int postal) {
    this.postal = postal;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getShelf() {
    return shelf;
  }

  public void setShelf(String shelf) {
    this.shelf = shelf;
  }

  public String getRetail() {
    return retail;
  }

  public void setRetail(String retail) {
    this.retail = retail;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }
}
