package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danie on 30/08/2016.
 */
public class GetQtyNotifications {

    private Long uid;
    private BigInteger token;
    private String profile;

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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
