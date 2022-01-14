package app.logistikappbeta.com.logistikapp.Params;

/**
 * Created by danie on 12/08/2016.
 */
public class CheckinParam {

    private Long uid;
    private String token;
    private Integer wid;
    private String date;
    private String outrange;
    private Double latitude;
    private Double longitude;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getWid() {
        return wid;
    }

    public void setWid(Integer wid) {
        this.wid = wid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOutrange() {
        return outrange;
    }

    public void setOutrange(String outrange) {
        this.outrange = outrange;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
