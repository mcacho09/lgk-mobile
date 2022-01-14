package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danielmarin on 14/11/17.
 */

public class AddActiveVisitParam {

  private Long uid;

  private BigInteger token;

  private Long ids;

  private Long idw;

  private Long id_actives;

  private Double latitude;

  private Double longitude;

  private String comment;

  private String image1;

  private String image2;

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

  public Long getIds() {
    return ids;
  }

  public void setIds(Long ids) {
    this.ids = ids;
  }

  public Long getIdw() {
    return idw;
  }

  public void setIdw(Long idw) {
    this.idw = idw;
  }

  public Long getId_actives() {
    return id_actives;
  }

  public void setId_actives(Long id_actives) {
    this.id_actives = id_actives;
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

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getImage1() {
    return image1;
  }

  public void setImage1(String image1) {
    this.image1 = image1;
  }

  public String getImage2() {
    return image2;
  }

  public void setImage2(String image2) {
    this.image2 = image2;
  }

}
