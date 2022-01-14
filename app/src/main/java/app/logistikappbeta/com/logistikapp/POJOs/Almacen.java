package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danielmarin on 18/09/17.
 */

public class Almacen {

  private Long id_almacen;

  private Long id_user;

  private Long id_retail;

  private String code;

  private Long id_supplier;

  public Long getId_almacen() {
    return id_almacen;
  }

  public void setId_almacen(Long id_almacen) {
    this.id_almacen = id_almacen;
  }

  public Long getId_user() {
    return id_user;
  }

  public void setId_user(Long id_user) {
    this.id_user = id_user;
  }

  public Long getId_retail() {
    return id_retail;
  }

  public void setId_retail(Long id_retail) {
    this.id_retail = id_retail;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Long getId_supplier() {
    return id_supplier;
  }

  public void setId_supplier(Long id_supplier) {
    this.id_supplier = id_supplier;
  }

}
