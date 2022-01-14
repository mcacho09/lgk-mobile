package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danie on 23/12/2016.
 */

public class AddNewCustomerParam {

  private Long uid;
  private BigInteger token;
  private Long rid;
  private Long id;
  private String name;
  private Long id_state;
  private Long id_city;
  private Long id_category;
  private String address1;
  private String address2;
  private Integer postal;
  private String phone;
  private String email;
  private Double lat;
  private Double lng;
  private Integer orderby;
  private String active;
  private String shelf;
  private String code;
  private Long country;

  public Long getRid() {
    return rid;
  }

  public void setRid(Long rid) {
    this.rid = rid;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId_state() {
    return id_state;
  }

  public void setId_state(Long id_state) {
    this.id_state = id_state;
  }

  public Long getId_city() {
    return id_city;
  }

  public void setId_city(Long id_city) {
    this.id_city = id_city;
  }

  public Long getId_category() {
    return id_category;
  }

  public void setId_category(Long id_category) {
    this.id_category = id_category;
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

  public Integer getPostal() {
    return postal;
  }

  public void setPostal(Integer postal) {
    this.postal = postal;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public Integer getOrderby() {
    return orderby;
  }

  public void setOrderby(Integer orderby) {
    this.orderby = orderby;
  }

  public String getActive() {
    return active;
  }

  public void setActive(String active) {
    this.active = active;
  }

  public String getShelf() {
    return shelf;
  }

  public void setShelf(String shelf) {
    this.shelf = shelf;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

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

  public Long getCountry() {
    return country;
  }

  public void setCountry(Long country) {
    this.country = country;
  }
  
}
