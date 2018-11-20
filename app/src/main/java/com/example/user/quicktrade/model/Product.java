package com.example.user.quicktrade.model;


/**
 * This class, is the product that add to the db
 */
public class Product {
    private String userID_prod;
    private String titulo_prod;
    private String precio_prod;
    private String lugar_rec;
    private String descr_prod;
    private String cat_prod;
    private String userName_prod;
    private String prod_puntuation;
    private String favorito;

    Product(){
    }

    /**
     * Constructor of the product
     *
     * @param userID_prod
     * @param titulo_prod
     * @param precio_prod
     * @param lugar_rec
     * @param descr_prod
     * @param cat_prod
     */
    public Product(String userID_prod, String userName_prod, String titulo_prod, String precio_prod, String lugar_rec, String descr_prod, String cat_prod, String prod_puntuation, String favorito) {
        this.userID_prod = userID_prod;
        this.titulo_prod = titulo_prod;
        this.precio_prod = precio_prod;
        this.lugar_rec = lugar_rec;
        this.descr_prod = descr_prod;
        this.cat_prod = cat_prod;
        this.userName_prod = userName_prod;
        this.prod_puntuation = prod_puntuation;
        this.favorito = favorito;
    }

    public String getUserID_prod() {
        return userID_prod;
    }

    public void setUserID_prod(String userID_prod) {
        this.userID_prod = userID_prod;
    }

    public String getTitulo_prod() {
        return titulo_prod;
    }

    public void setTitulo_prod(String titulo_prod) {
        this.titulo_prod = titulo_prod;
    }

    public String getPrecio_prod() {
        return precio_prod;
    }

    public void setPrecio_prod(String precio_prod) {
        this.precio_prod = precio_prod;
    }

    public String getLugar_rec() {
        return lugar_rec;
    }

    public void setLugar_rec(String lugar_rec) {
        this.lugar_rec = lugar_rec;
    }

    public String getDescr_prod() {
        return descr_prod;
    }

    public void setDescr_prod(String descr_prod) {
        this.descr_prod = descr_prod;
    }

    public String getCat_prod() {
        return cat_prod;
    }

    public void setCat_prod(String cat_prod) {
        this.cat_prod = cat_prod;
    }

    public String getUserName_prod() {
        return userName_prod;
    }

    public void setUserName_prod(String userName_prod) {
        this.userName_prod = userName_prod;
    }

    public String getProd_puntuation() {
        return prod_puntuation;
    }

    public void setProd_puntuation(String prod_puntuation) {
        this.prod_puntuation = prod_puntuation;
    }

    public String getFavorito() {
        return favorito;
    }

    public void setFavorito(String favorito) {
        this.favorito = favorito;
    }

    /**
     * Tostring method.
     *
     * @return
     */
    @Override
    public String toString() {
        return "Product{" +
                "userID_prod='" + userID_prod + '\'' +
                ", titulo_prod='" + titulo_prod + '\'' +
                ", precio_prod='" + precio_prod + '\'' +
                ", lugar_rec='" + lugar_rec + '\'' +
                ", descr_prod='" + descr_prod + '\'' +
                ", cat_prod='" + cat_prod + '\'' +
                ", cat_prod='" + prod_puntuation + '\'' +
                ", cat_prod='" + favorito + '\'' +
                '}';
    }
}
