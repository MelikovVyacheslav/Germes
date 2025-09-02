package org.slavik.ocs.model;

import com.fasterxml.jackson.annotation.*;

public class Order {
    private double value;
    private String currency;

    private final double PERCENT_VALUE = 16;

    @JsonProperty("value")
    public double getValue() { return (int) (value + (value * (PERCENT_VALUE / 100))); }
    @JsonProperty("value")
    public void setValue(double value) { this.value = value; }

    @JsonProperty("currency")
    public String getCurrency() { return currency; }
    @JsonProperty("currency")
    public void setCurrency(String value) { this.currency = value; }
}


