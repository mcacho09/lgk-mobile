package app.logistikappbeta.com.logistikapp.Results;

import app.logistikappbeta.com.logistikapp.POJOs.Metric;

/**
 * Created by danie on 17/01/2017.
 */

public class MetricResult extends Result {

  private Metric metrics;

  public Metric getMetrics() {
    return metrics;
  }

  public void setMetrics(Metric metrics) {
    this.metrics = metrics;
  }
}
