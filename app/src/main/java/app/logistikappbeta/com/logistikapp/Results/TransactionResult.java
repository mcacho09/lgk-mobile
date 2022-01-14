package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.ProductDetail;
import app.logistikappbeta.com.logistikapp.POJOs.Transaction;

/**
 * Created by danie on 26/01/2017.
 */

public class TransactionResult extends Result {

  private Transaction transaction;
  private ArrayList<ProductDetail> vtalist;
  private ArrayList<ProductDetail> devlist;
  private ArrayList<ProductDetail> chglist;

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  public ArrayList<ProductDetail> getVtalist() {
    return vtalist;
  }

  public void setVtalist(ArrayList<ProductDetail> vtalist) {
    this.vtalist = vtalist;
  }

  public ArrayList<ProductDetail> getDevlist() {
    return devlist;
  }

  public void setDevlist(ArrayList<ProductDetail> devlist) {
    this.devlist = devlist;
  }

  public ArrayList<ProductDetail> getChglist() {
    return chglist;
  }

  public void setChglist(ArrayList<ProductDetail> chglist) {
    this.chglist = chglist;
  }

}
