package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 09/01/2017.
 */

public class ReportByDri {

  private Long id_user;
  private String username;
  private Double sales;
  private Double total_products;
  private Long no_trx;

  public Long getId_user() {
    return this.id_user;
  }

  public void setId_user(Long id_user) {
    this.id_user = id_user;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Double getSales() {
    return this.sales;
  }

  public void setSales(Double sales) {
    this.sales = sales;
  }

  public Double getTotal_products() {
    return this.total_products;
  }

  public void setTotal_products(Double total_products) {
    this.total_products = total_products;
  }

  public Long getNo_trx() {
    return this.no_trx;
  }

  public void setNo_trx(Long no_trx) {
    this.no_trx = no_trx;
  }

}
