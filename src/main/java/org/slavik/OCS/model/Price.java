package org.slavik.OCS.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Price {
    private Order priceList;
    private Order order;
    private long discountB2B;

    @JsonProperty("priceList")
    public Order getPriceList() {
        return priceList;
    }

    @JsonProperty("priceList")
    public void setPriceList(Order value) {
        this.priceList = value;
    }

    @JsonProperty("order")
    public Order getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(Order value) {
        this.order = value;
    }

    @JsonProperty("discountB2B")
    public long getDiscountB2B() {
        return discountB2B;
    }

    @JsonProperty("discountB2B")
    public void setDiscountB2B(long value) {
        this.discountB2B = value;
    }
}
