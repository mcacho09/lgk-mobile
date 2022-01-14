package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 27/01/2017.
 */

public class UserPosition {

  private Long id_user_position;
  private Long id_user;
  private Long id_travel;
  private Double latitude;
  private Double longitude;
  private Long created;

  public Long getId_user_position() {
    return this.id_user_position;
  }

  public void setId_user_position(Long id_user_position) {
    this.id_user_position = id_user_position;
  }

  public Long getId_user() {
    return this.id_user;
  }

  public void setId_user(Long id_user) {
    this.id_user = id_user;
  }

  public Long getId_travel() {
    return this.id_travel;
  }

  public void setId_travel(Long id_travel) {
    this.id_travel = id_travel;
  }

  public Double getLatitude() {
    return this.latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return this.longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Long getCreated() {
    return this.created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }

}
