package app.logistikappbeta.com.logistikapp.POJOs;

import java.util.List;

/**
 * Created by danielmarin on 18/09/17.
 */

public class AlmacenAndProducts {

  private Almacen almacen;
  private List<ProductsAlmacenInfo> products;

  public Almacen getAlmacen() {
    return almacen;
  }

  public void setAlmacen(Almacen almacen) {
    this.almacen = almacen;
  }

  public List<ProductsAlmacenInfo> getProducts() {
    return products;
  }

  public void setProducts(List<ProductsAlmacenInfo> products) {
    this.products = products;
  }
}
