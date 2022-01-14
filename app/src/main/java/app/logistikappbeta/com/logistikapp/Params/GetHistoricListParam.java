package app.logistikappbeta.com.logistikapp.Params;

/**
 * Created by danie on 15/08/2016.
 */
public class GetHistoricListParam {

    private int uid;
    private String token;
    private String profile;
    private String date;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
