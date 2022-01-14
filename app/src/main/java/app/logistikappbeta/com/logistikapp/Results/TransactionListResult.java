package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.TransactionList;

/**
 * Created by danie on 26/01/2017.
 */

public class TransactionListResult extends Result {

  private ArrayList<TransactionList> transactions;

  public ArrayList<TransactionList> getTransactions() {
    return transactions;
  }

  public void setTransactions(ArrayList<TransactionList> transactions) {
    this.transactions = transactions;
  }
}
