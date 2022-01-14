package app.logistikappbeta.com.logistikapp.Params;

import app.logistikappbeta.com.logistikapp.POJOs.Transaction;
import app.logistikappbeta.com.logistikapp.Results.Result;

/**
 * Created by danie on 12/08/2016.
 */
public class GetTransactionParamById extends Result {

    private Long tid;
    private Transaction transaction;


    public Transaction getTransactionById(long id) {
        return transaction;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

}
