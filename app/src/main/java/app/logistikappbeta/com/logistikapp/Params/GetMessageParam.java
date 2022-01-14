package app.logistikappbeta.com.logistikapp.Params;

import java.math.BigInteger;

/**
 * Created by danie on 28/08/2016.
 */
public class GetMessageParam {

    private Long uid;
    private BigInteger token;
    private Long id_conversation;

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

    public Long getId_conversation() {
        return id_conversation;
    }

    public void setId_conversation(Long id_conversation) {
        this.id_conversation = id_conversation;
    }
}
