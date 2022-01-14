package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 12/08/2016.
 */
public class Transaction {

  private int id;
  private String store;
  private String driver;
  private String invoice;
  private double sale;
  private Double sale_dev;
  private Long changes;
  private Long devolutions;
  private Long trxnumber;
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

  public Double getSale_dev() {
    return sale_dev;
  }

  public void setSale_dev(Double sale_dev) {
    this.sale_dev = sale_dev;
  }

  public Long getChanges() {
    return changes;
  }

  public void setChanges(Long changes) {
    this.changes = changes;
  }

  public Long getDevolutions() {
    return devolutions;
  }

  public void setDevolutions(Long devolutions) {
    this.devolutions = devolutions;
  }

  public Long getTrxnumber() {
    return trxnumber;
  }

  public void setTrxnumber(Long trxnumber) {
    this.trxnumber = trxnumber;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
