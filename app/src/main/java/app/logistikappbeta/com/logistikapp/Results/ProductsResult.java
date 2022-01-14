package app.logistikappbeta.com.logistikapp.Results;

import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.ProductPV;

/**
 * Created by danie on 26/01/2017.
 */

public class ProductsResult extends Result {

  private ArrayList<ProductPV> products;

  public ArrayList<ProductPV> getProducts() {
    return products;
  }

  public void setProducts(ArrayList<ProductPV> products) {
    this.products = products;
  }
}
