package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 09/08/2016.
 */
public class TransactionList {
  private int id;
  private String store;
  private String driver;
  private String invoice;
  private double sale;
  private int changes;
  private int devolutions;
  private String state;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getStore() {
    return store;
  }

  public void setStore(String store) {
    this.store = store;
  }

  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public String getInvoice() {
    return invoice;
  }

  public void setInvoice(String invoice) {
    this.invoice = invoice;
  }

  public double getSale() {
    return sale;
  }

  public void setSale(double sale) {
    this.sale = sale;
  }

  public int getChanges() {
    return changes;
  }

  public void setChanges(int changes) {
    this.changes = changes;
  }

  public int getDevolutions() {
    return devolutions;
  }

  public void setDevolutions(int devolutions) {
    this.devolutions = devolutions;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}