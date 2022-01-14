package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 17/07/2016.
 */
public class Notification {

    private String message;
    private String created;
    private String username;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
