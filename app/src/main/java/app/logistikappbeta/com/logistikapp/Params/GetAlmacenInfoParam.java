package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danie on 22/08/2016.
 */
public class GetAlmacenInfoParam {

    private Long uid;

    private BigInteger token;

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

}
