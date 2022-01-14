package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 04/08/2016.
 */
public class Metric {

  private int travels;
  private int stores;
  private int checkin;
  private int notcheckin;
  private int productsQty;
  private double sales;
  private double decrease;
  private double utility;

  public int getTravels() {
    return travels;
  }

  public void setTravels(int travels) {
    this.travels = travels;
  }

  public int getStores() {
    return stores;
  }

  public void setStores(int stores) {
    this.stores = stores;
  }

  public int getCheckin() {
    return checkin;
  }

  public void setCheckin(int checkin) {
    this.checkin = checkin;
  }

  public int getNotcheckin() {
    return notcheckin;
  }

  public void setNotcheckin(int notcheckin) {
    this.notcheckin = notcheckin;
  }

  public int getProductsQty() {
    return productsQty;
  }

  public void setProductsQty(int productsQty) {
    this.productsQty = productsQty;
  }

  public double getSales() {
    return sales;
  }

  public void setSales(double sales) {
    this.sales = sales;
  }

  public double getDecrease() {
    return decrease;
  }

  public void setDecrease(double decrease) {
    this.decrease = decrease;
  }

  public double getUtility() {
    return utility;
  }

  public void setUtility(double utility) {
    this.utility = utility;
  }
}
