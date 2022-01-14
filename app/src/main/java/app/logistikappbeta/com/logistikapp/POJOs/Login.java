package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 26/07/2016.
 */
public class Login {

    private Long id;
    private String name;
    private String login;
    private String profile;
    private String email;
    private String superuser;
    private String ubi_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSuperuser() {
        return superuser;
    }

    public void setSuperuser(String superuser) {
        this.superuser = superuser;
    }

    public String getUbi_time() {
        return ubi_time;
    }

    public void setUbi_time(String ubi_time) {
        this.ubi_time = ubi_time;
    }

    @Override
    public String toString() {
        return "Login{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", profile='" + profile + '\'' +
                ", email='" + email + '\'' +
                ", superuser='" + superuser + '\'' +
                ", ubi_time='" + ubi_time + '\'' +
                '}';
    }
}
