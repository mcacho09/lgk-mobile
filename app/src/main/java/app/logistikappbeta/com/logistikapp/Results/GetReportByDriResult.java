package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.ReportByDri;

/**
 * Created by danielmarin on 10/06/17.
 */

public class GetReportByDriResult extends Result {

  private ArrayList<ReportByDri> transactions;

  public ArrayList<ReportByDri> getTransactions() {
    return transactions;
  }

  public void setTransactions(ArrayList<ReportByDri> transactions) {
    this.transactions = transactions;
  }
}
