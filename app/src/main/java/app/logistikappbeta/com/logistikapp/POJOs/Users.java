package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 20/12/2016.
 */

public class Users {

  private Long id_user;
  private String active;
  private String username;
  private String userlogin;
  private String profile;
  private String superuser;
  private String email;
  private String job;
  private String phone;

  public Long getId_user() {
    return id_user;
  }

  public void setId_user(Long id_user) {
    this.id_user = id_user;
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

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

}
