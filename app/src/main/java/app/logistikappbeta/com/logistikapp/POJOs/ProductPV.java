package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 19/08/2016.
 */
public class ProductPV implements Cloneable {

  private Long id_product;
  private String name_short;
  private String name_long;
  private String code;
  private Double price_sug;
  private Double price_sale_piece;
  private Double price_sale;
  private Long qtyproducts;
  private Double sale;
  private Boolean isVisible;
  private String product_type;
  private String product_type_temp;
  private Double piece_in_box;
  private Integer tax;

  public ProductPV() {
    this.isVisible = true;
  }

  public Long getId_product() {
    return id_product;
  }

  public void setId_product(Long id_product) {
    this.id_product = id_product;
  }

  public String getName_short() {
    return name_short;
  }

  public void setName_short(String name_short) {
    this.name_short = name_short;
  }

  public String getName_long() {
    return name_long;
  }

  public void setName_long(String name_long) {
    this.name_long = name_long;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Double getPrice_sug() {
    return price_sug;
  }

  public void setPrice_sug(Double price_sug) {
    this.price_sug = price_sug;
  }

  public Double getPrice_sale() {
    return price_sale;
  }

  public void setPrice_sale(Double price_sale) {
    this.price_sale = price_sale;
  }

  public Long getQtyproducts() {
    return qtyproducts;
  }

  public void setQtyproducts(Long qtyproducts) {
    this.qtyproducts = qtyproducts;
  }

  public Double getSale() {
    return sale;
  }

  public void setSale(Double sale) {
    this.sale = sale;
  }

  public Boolean getVisible() {
    return isVisible;
  }

  public void setVisible(Boolean visible) {
    isVisible = visible;
  }

  public String getProduct_type() {
    return product_type;
  }

  public void setProduct_type(String product_type) {
    this.product_type = product_type;
  }

  public String getProduct_type_temp() {
    return product_type_temp;
  }

  public void setProduct_type_temp(String product_type_temp) {
    this.product_type_temp = product_type_temp;
  }

  public Double getPiece_in_box() {
    return piece_in_box;
  }

  public void setPiece_in_box(Double piece_in_box) {
    this.piece_in_box = piece_in_box;
  }

  public Double getPrice_sale_piece() {
    return price_sale_piece;
  }

  public void setPrice_sale_piece(Double price_sale_piece) {
    this.price_sale_piece = price_sale_piece;
  }

  public Integer getTax() {
    return tax;
  }

  public void setTax(Integer tax) {
    this.tax = tax;
  }

  @Override
  public ProductPV clone() throws CloneNotSupportedException {
    return (ProductPV) super.clone();
  }
}
