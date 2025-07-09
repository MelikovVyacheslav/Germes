package org.slavik.ocs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    private String location;
    private String description;
    private String type;
    private Quantity quantity;
    private boolean canReserve;
    private String deliveryDate; // Или Date, если хочешь парсить дату
    private String arrivalDate; // или другой нужный тип (например, LocalDateTime)
    private String departureDate; // или LocalDateTime, если используешь дату

    @JsonProperty("departureDate")
    public String getDepartureDate() {
        return departureDate;
    }
    @JsonProperty("departureDate")
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    @JsonProperty("arrivalDate")
    public String getArrivalDate() {
        return arrivalDate;
    }
    @JsonProperty("arrivalDate")
    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @JsonProperty("deliveryDate")
    public String getDeliveryDate() {
        return deliveryDate;
    }
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

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