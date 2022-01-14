package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 20/08/2016.
 */
public class OrderDetail {

  private Long id_product;
  private Double price_sale;
  private Double price_sug;
  private Long quantity;
  private String type;

  public Long getId_product() {
    return id_product;
  }

  public void setId_product(Long id_product) {
    this.id_product = id_product;
  }

  public Double getPrice_sale() {
    return price_sale;
  }

  public void setPrice_sale(Double price_sale) {
    this.price_sale = price_sale;
  }

  public Double getPrice_sug() {
    return price_sug;
  }

  public void setPrice_sug(Double price_sug) {
    this.price_sug = price_sug;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
