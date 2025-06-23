package org.slavik.OCS.model;


import com.fasterxml.jackson.annotation.*;

public class Quantity {
    private long value;
    private boolean isGreatThan;

    @JsonProperty("value")
    public long getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(long value) {
        this.value = value;
    }

    @JsonProperty("isGreatThan")
    public boolean getIsGreatThan() {
        return isGreatThan;
    }

    @JsonProperty("isGreatThan")
    public void setIsGreatThan(boolean value) {
        this.isGreatThan = value;
    }
}
