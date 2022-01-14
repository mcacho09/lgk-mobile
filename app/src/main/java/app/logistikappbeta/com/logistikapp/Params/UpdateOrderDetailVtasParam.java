package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;
import java.util.ArrayList;

import app.logistikappbeta.com.logistikapp.POJOs.OrderDetail;

/**
 * Created by Erick on 03/03/2020.
 */
public class UpdateOrderDetailVtasParam {

    private Long uid;

    private Long oid;

    private ArrayList<OrderDetail> products_VTA;

    private BigInteger token;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public BigInteger getToken() {
        return token;
    }

    public void setToken(BigInteger token) {
        this.token = token;
    }

    public void setProducts_VTA(ArrayList<OrderDetail> products_VTA) {
        this.products_VTA = products_VTA;
    }

    public ArrayList<OrderDetail> getProducts_VTA() {
        return products_VTA;
    }
}
