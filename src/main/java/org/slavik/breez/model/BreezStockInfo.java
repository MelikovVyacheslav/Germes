package org.slavik.breez.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BreezStockInfo {
    private String nc;
    private String articul;
    private String title;
    private String quantity;
    private String stock;
    private Price price;
    private String forMarketplace;
    private String time;

    @JsonProperty("nc")
    public String getNc() {
        return nc;
    }

    @JsonProperty("nc")
    public void setNc(String nc) {
        this.nc = nc;
    }

    @JsonProperty("articul")
    public String getArticul() {
        return articul;
    }

    @JsonProperty("articul")
    public void setArticul(String articul) {
        this.articul = articul;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("quantity")
    public int getQuantity() {
        if (quantity.equals(">50")) {
            return 0;
        }
        return Integer.parseInt(quantity);
    }

    @JsonProperty("quantity")
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("stock")
    public String getStock() {
        return stock;
    }

    @JsonProperty("stock")
    public void setStock(String stock) {
        this.stock = stock;
    }

    @JsonProperty("price")
    public Price getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Price price) {
        this.price = price;
    }

    @JsonProperty("for_marketplace")
    public String getForMarketplace() {
        return forMarketplace;
    }

    @JsonProperty("for_marketplace")
    public void setForMarketplace(String forMarketplace) {
        this.forMarketplace = forMarketplace;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }
}
