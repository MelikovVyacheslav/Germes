package org.slavik.OCS.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Price {
    private Order priceList;
    private Order order;
    private long discountB2B;
    private Order endUser;
    private Order endUserWeb;
    private Boolean mustKeepEndUserPrice;

    @JsonProperty("priceList")
    public Order getPriceList() { return priceList; }
    @JsonProperty("priceList")
    public void setPriceList(Order value) { this.priceList = value; }

    @JsonProperty("order")
    public Order getOrder() { return order; }
    @JsonProperty("order")
    public void setOrder(Order value) { this.order = value; }

    @JsonProperty("discountB2B")
    public long getDiscountB2B() { return discountB2B; }
    @JsonProperty("discountB2B")
    public void setDiscountB2B(long value) { this.discountB2B = value; }

    @JsonProperty("endUser")
    public Order getEndUser() { return endUser; }
    @JsonProperty("endUser")
    public void setEndUser(Order value) { this.endUser = value; }

    @JsonProperty("endUserWeb")
    public Order getEndUserWeb() { return endUserWeb; }
    @JsonProperty("endUserWeb")
    public void setEndUserWeb(Order value) { this.endUserWeb = value; }

    @JsonProperty("mustKeepEndUserPrice")
    public Boolean getMustKeepEndUserPrice() { return mustKeepEndUserPrice; }
    @JsonProperty("mustKeepEndUserPrice")
    public void setMustKeepEndUserPrice(Boolean value) { this.mustKeepEndUserPrice = value; }
}
