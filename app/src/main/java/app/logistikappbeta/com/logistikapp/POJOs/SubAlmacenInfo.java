package app.logistikappbeta.com.logistikapp.POJOs;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danielmarin on 19/09/17.
 */

public class SubAlmacenInfo {

  private SubAlmacen almacen;

  private List<ProductsAlmacenInfo> products;

  private List<ProductsAlmacenInfo> newCloneableList;

  private List<ProductsAlmacenInfo> productsTotal;

  public SubAlmacen getAlmacen() {
    return almacen;
  }

  public void setAlmacen(SubAlmacen almacen) {
    this.almacen = almacen;
  }

  public List<ProductsAlmacenInfo> getProducts() {
    return products;
  }

  public void setProducts(List<ProductsAlmacenInfo> products) {
    this.products = products;
  }

  //  Erick Gtz - 3/4/2020 Method that returns the initial value of the products
  @RequiresApi(api = Build.VERSION_CODES.N)
  public List<ProductsAlmacenInfo> getProductsInitialAlmacen() throws CloneNotSupportedException {
    ArrayList<ProductsAlmacenInfo> iterator = new ArrayList<>();

    HashMap<Long, ProductsAlmacenInfo> map = new HashMap<Long, ProductsAlmacenInfo>();
      for (ProductsAlmacenInfo x : products) {
          //Set products on stock always positive, we don't want to show negative products
          //x.setQty(Math.abs(x.getQty()));

          //We are only interested on do the add operation with (VTA, VDO & CHG)
          if((!x.getStock_type().equals("DEV"))) {
              map.merge(x.getId_product(), x, (a, b) -> {
                  ProductsAlmacenInfo temp = null;
                  try {
                      temp = (ProductsAlmacenInfo) b.clone();
                  } catch (CloneNotSupportedException e) {
                      e.printStackTrace();
                  }
                  temp.setQty(a.getQty() + b.getQty());
                  return temp;
              });
          }
      }
    List<ProductsAlmacenInfo> productsTemp = new ArrayList<ProductsAlmacenInfo>(map.values());
    if(productsTemp.isEmpty())  return products;
    return productsTemp;
  }

  public List<ProductsAlmacenInfo> getProductsTotal() {
    return productsTotal;
  }

  public void setProductsTotal(List<ProductsAlmacenInfo> productsTotal) {
    this.productsTotal = productsTotal;
  }
}
