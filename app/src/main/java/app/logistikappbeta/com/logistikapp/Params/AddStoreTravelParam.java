package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by danie on 26/08/2016.
 */
public class AddStoreTravelParam {

    private Long uid;
    private BigInteger token;
    private Long tid;
    private ArrayList<Long> ids;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public BigInteger getToken() {
        return token;
    }

    public void setToken(BigInteger token) {
        this.token = token;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public ArrayList<Long> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Long> ids) {
        this.ids = ids;
    }
}
