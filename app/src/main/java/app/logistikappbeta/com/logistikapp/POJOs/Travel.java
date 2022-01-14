package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 19/07/2016.
 */
public class Travel {

    private long id;
    private String name;
    private String status;
    private String driver;
    private String scheduled;
    private String started;
    private int stores;
    private float progress;
    private int checkin;
    private int checkinok;
    private int checkinerr;
    private int comments;
    private int images;
    private String finished;
    private float km;
    private float time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public int getStores() {
        return stores;
    }

    public void setStores(int stores) {
        this.stores = stores;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getCheckin() {
        return checkin;
    }

    public void setCheckin(int checkin) {
        this.checkin = checkin;
    }

    public int getCheckinok() {
        return checkinok;
    }

    public void setCheckinok(int checkinok) {
        this.checkinok = checkinok;
    }

    public int getCheckinerr() {
        return checkinerr;
    }

    public void setCheckinerr(int checkinerr) {
        this.checkinerr = checkinerr;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Travel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", driver='" + driver + '\'' +
                ", scheduled='" + scheduled + '\'' +
                ", started='" + started + '\'' +
                ", stores=" + stores +
                ", progress=" + progress +
                ", checkin=" + checkin +
                ", checkinok=" + checkinok +
                ", checkinerr=" + checkinerr +
                ", comments=" + comments +
                ", images=" + images +
                ", finished='" + finished + '\'' +
                ", km=" + km +
                ", time=" + time +
                '}';
    }

}
