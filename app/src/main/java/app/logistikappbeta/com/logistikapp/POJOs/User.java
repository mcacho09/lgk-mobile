package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 26/07/2016.
 */
public class User {

  private long id;
  private String login;
  private String passwd;
  private String name;
  private String profile;
  private String superuser;
  private String email;
  private String phone1;
  private String phone2;
  private String job;
  private String image;
  private Integer orderby;
  private String active;
  private String ubi_time;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getPhone2() {
    return phone2;
  }

  public void setPhone2(String phone2) {
    this.phone2 = phone2;
  }

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
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

  public String getUbi_time() {
    return ubi_time;
  }

  public void setUbi_time(String ubi_time) {
    this.ubi_time = ubi_time;
  }
}
