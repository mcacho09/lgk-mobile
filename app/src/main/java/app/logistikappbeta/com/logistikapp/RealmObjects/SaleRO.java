package app.logistikappbeta.com.logistikapp.RealmObjects;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by danielmarin on 01/12/17.
 */

public class SaleRO extends RealmObject {

  @PrimaryKey
  private String id;

  @Required
  private String params;

  @Required
  private String store;

  @Required
  private Long id_store;

  @Required
  private Date created;

  @Required
  private Double total;

  @Required
  private Double devSale;

  private Long id_order;

  @Required
  private String type;

  @Required
  private String retail;

  @Required
  private String seller;

  @Required
  private String ticket;

  public enum SaleType {
    NOT_SYNC, SYNC, SYNCED
  }

  public SaleRO() {
    type = SaleType.NOT_SYNC.toString();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getStore() {
    return store;
  }

  public void setStore(String store) {
    this.store = store;
  }

  public Long getId_store() {
    return id_store;
  }

  public void setId_store(Long id_store) {
    this.id_store = id_store;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Double getTotal() {
    return total;
  }

  public void setTotal(Double total) {
    this.total = total;
  }

  public Double getDevSale() {
    return devSale;
  }

  public void setDevSale(Double devSale) {
    this.devSale = devSale;
  }

  public Long getId_order() {
    return id_order;
  }

  public void setId_order(Long id_order) {
    this.id_order = id_order;
  }

  public String getType() {
    return type;
  }

  public void setType(SaleType type) {
    this.type = type.toString();
  }

  public String getRetail() {
    return retail;
  }

  public void setRetail(String retail) {
    this.retail = retail;
  }

  public String getSeller() {
    return seller;
  }

  public void setSeller(String seller) {
    this.seller = seller;
  }

  public String getTicket() {
    return ticket;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }
}
