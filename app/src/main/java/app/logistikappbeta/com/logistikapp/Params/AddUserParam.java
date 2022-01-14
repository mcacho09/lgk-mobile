package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danie on 13/02/2017.
 */

public class AddUserParam {

  private Integer orderby;
  private String active;
  private String username;
  private String userlogin;
  private String passwd;
  private String profile;
  private String superuser;
  private String email;
  private String phone1;
  private String job;

  private Long uid;
  private BigInteger token;

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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUserlogin() {
    return userlogin;
  }

  public void setUserlogin(String userlogin) {
    this.userlogin = userlogin;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public String getSuperuser() {
    return superuser;
  }

  public void setSuperuser(String superuser) {
    this.superuser = superuser;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone1() {
    return phone1;
  }

  public void setPhone1(String phone1) {
    this.phone1 = phone1;
  }

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
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
}
