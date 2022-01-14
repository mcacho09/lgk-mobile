package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 22/08/2016.
 */
public class Comment {

    private Long id_waybill;
    private String comment;
    private String note;
    private String image1;
    private String image2;
    private String image3;

    public Long getId_waybill() {
        return id_waybill;
    }

    public void setId_waybill(Long id_waybill) {
        this.id_waybill = id_waybill;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
