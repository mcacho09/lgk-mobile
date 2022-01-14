package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 18/07/2016.
 */
public class MessageList {

    private Long id_user_message;
    private String username;
    private String since;
    private String message;
    private String read;
    private Long id_user_to;

    public Long getId_user_message() {
        return id_user_message;
    }

    public void setId_user_message(Long id_user_message) {
        this.id_user_message = id_user_message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public Long getId_user_to() {
        return id_user_to;
    }

    public void setId_user_to(Long id_user_to) {
        this.id_user_to = id_user_to;
    }
}
