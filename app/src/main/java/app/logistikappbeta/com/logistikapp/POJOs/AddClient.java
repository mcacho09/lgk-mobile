package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 25/08/2016.
 */
public class AddClient {

    private Long id_store;
    private String name;
    private String shelf;
    private Boolean isSelected;

    public Long getId_store() {
        return id_store;
    }

    public void setId_store(Long id_store) {
        this.id_store = id_store;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
