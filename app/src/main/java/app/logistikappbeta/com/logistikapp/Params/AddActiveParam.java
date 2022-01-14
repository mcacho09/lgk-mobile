package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danielmarin on 15/11/17.
 */

public class AddActiveParam {

  private Long uid;

  private BigInteger token;

  private String code;

  private String image;

  private Long id_store;

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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Long getId_store() {
    return id_store;
  }

  public void setId_store(Long id_store) {
    this.id_store = id_store;
  }

}
