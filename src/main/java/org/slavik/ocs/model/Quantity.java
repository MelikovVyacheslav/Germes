package org.slavik.ocs.model;


import com.fasterxml.jackson.annotation.*;

public class Quantity {
   private int value;
    private boolean isGreatThan;

    @JsonProperty("value")
    public int getValue() { return value; }
    @JsonProperty("value")
    public void setValue(int value) { this.value = value; }


    @JsonProperty("isGreatThan")
    public boolean getIsGreatThan() { return isGreatThan; }
    @JsonProperty("isGreatThan")
    public void setIsGreatThan(boolean value) { this.isGreatThan = value; }
}