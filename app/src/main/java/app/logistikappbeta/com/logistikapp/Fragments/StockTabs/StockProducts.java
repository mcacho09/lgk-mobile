package app.logistikappbeta.com.logistikapp.Fragments.StockTabs;

import java.util.List;

import app.logistikappbeta.com.logistikapp.POJOs.ProductsAlmacenInfo;

public class StockProducts {
    public static List<ProductsAlmacenInfo> ourInstance = null;

    public static List<ProductsAlmacenInfo> getInstance() {
        return ourInstance;
    }

    public StockProducts(List<ProductsAlmacenInfo> list) {
        ourInstance = list;
    }
}
