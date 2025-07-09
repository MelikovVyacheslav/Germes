package org.slavik.ocs.model;

import com.fasterxml.jackson.annotation.*;

public class Order {
    private double value;
    private String currency;

    @JsonProperty("value")
    public double getValue() { return value; }
    @JsonProperty("value")
    public void setValue(double value) { this.value = value; }

    @JsonProperty("currency")
    public String getCurrency() { return currency; }
    @JsonProperty("currency")
    public void setCurrency(String value) { this.currency = value; }
}


