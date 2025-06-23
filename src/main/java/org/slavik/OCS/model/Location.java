package org.slavik.OCS.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    private String location;
    private String description;
    private String type;
    private Quantity quantity;
    private boolean canReserve;

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String value) {
        this.location = value;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String value) {
        this.description = value;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String value) {
        this.type = value;
    }

    @JsonProperty("quantity")
    public Quantity getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Quantity value) {
        this.quantity = value;
    }

    @JsonProperty("canReserve")
    public boolean getCanReserve() {
        return canReserve;
    }

    @JsonProperty("canReserve")
    public void setCanReserve(boolean value) {
        this.canReserve = value;
    }
}