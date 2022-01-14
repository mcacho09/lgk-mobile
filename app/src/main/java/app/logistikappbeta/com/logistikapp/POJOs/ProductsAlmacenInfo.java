package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danielmarin on 18/09/17.
 */

public class ProductsAlmacenInfo implements Cloneable {

  private Long id_product;

  private Long id_category_product;

  private String created;

  private String modified;

  private String login;

  private Integer orderby;

  private String active;

  private String code;

  private String name_short;

  private String name_long;

  private Double price_sale;

  private Double price_cost;

  private String image;

  private Long id_brand;

  private String flag;

  private Double price_cost_box;

  private Long piece_in_box;

  private String type;

  private Long qty;

  private Long min;

  private Long max;

  private String stock_type;

  public Long getId_product() {
    return id_product;
  }

  public void setId_product(Long id_product) {
    this.id_product = id_product;
  }

  public Long getId_category_product() {
    return id_category_product;
  }

  public void setId_category_product(Long id_category_product) {
    this.id_category_product = id_category_product;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getModified() {
    return modified;
  }

  public void setModified(String modified) {
    this.modified = modified;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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

  public Double getPrice_sale() {
    return price_sale;
  }

  public void setPrice_sale(Double price_sale) {
    this.price_sale = price_sale;
  }

  public Double getPrice_cost() {
    return price_cost;
  }

  public void setPrice_cost(Double price_cost) {
    this.price_cost = price_cost;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Long getId_brand() {
    return id_brand;
  }

  public void setId_brand(Long id_brand) {
    this.id_brand = id_brand;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public Double getPrice_cost_box() {
    return price_cost_box;
  }

  public void setPrice_cost_box(Double price_cost_box) {
    this.price_cost_box = price_cost_box;
  }

  public Long getPiece_in_box() {
    return piece_in_box;
  }

  public void setPiece_in_box(Long piece_in_box) {
    this.piece_in_box = piece_in_box;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getQty() {
    return qty;
  }

  public void setQty(Long qty) {
    this.qty = qty;
  }

  public Long getMin() {
    return min;
  }

  public void setMin(Long min) {
    this.min = min;
  }

  public Long getMax() {
    return max;
  }

  public void setMax(Long max) {
    this.max = max;
  }

  public String getStock_type() {
    return stock_type;
  }

  public void setStock_type(String stock_type) {
    this.stock_type = stock_type;
  }

  @Override
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
