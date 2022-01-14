package app.logistikappbeta.com.logistikapp.POJOs;

/**
 * Created by danie on 19/08/2016.
 */
public class ProductDetail {

    private Long qtyproducts;
    private String code;
    private String name_short;
    private String name_long;
    private Double sale;
    private Double sale_sug;
    private Integer tax;

    public Long getQtyproducts() {
        return qtyproducts;
    }

    public void setQtyproducts(Long qtyproducts) {
        this.qtyproducts = qtyproducts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName_short() {
        return name_short;
    }

    public void setName_short(String name_short) {
        this.name_short = name_short;
    }

    public String getName_long() {
        return name_long;
    }

    public void setName_long(String name_long) {
        this.name_long = name_long;
    }

    public Double getSale() {
        return sale;
    }

    public void setSale(Double sale) {
        this.sale = sale;
    }

    public Double getSale_sug() {
        return sale_sug;
    }

    public void setSale_sug(Double sale_sug) {
        this.sale_sug = sale_sug;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }
}
