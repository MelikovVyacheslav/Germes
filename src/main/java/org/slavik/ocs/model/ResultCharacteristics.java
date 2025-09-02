package org.slavik.ocs.model;

import com.fasterxml.jackson.annotation.*;

        import java.util.List;

public class ResultCharacteristics {
    private String itemID;
    private String partNumber;
    private String productName;
    private String producer;
    private String itemName;
    private List<OCSProperty> properties;
    private List<Image> images;
    private List<Image> mediumImages;

    @JsonProperty("itemId")
    public String getItemID() {
        return itemID;
    }

    @JsonProperty("itemId")
    public void setItemID(String value) {
        this.itemID = value;
    }

    @JsonProperty("partNumber")
    public String getPartNumber() {
        return partNumber;
    }

    @JsonProperty("partNumber")
    public void setPartNumber(String value) {
        this.partNumber = value;
    }

    @JsonProperty("productName")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("productName")
    public void setProductName(String value) {
        this.productName = value;
    }

    @JsonProperty("producer")
    public String getProducer() {
        return producer;
    }

    @JsonProperty("producer")
    public void setProducer(String value) {
        this.producer = value;
    }

    @JsonProperty("itemName")
    public String getItemName() {
        return itemName;
    }

    @JsonProperty("itemName")
    public void setItemName(String value) {
        this.itemName = value;
    }

    @JsonProperty("properties")
    public List<OCSProperty> getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(List<OCSProperty> value) {
        this.properties = value;
    }

    @JsonProperty("images")
    public List<Image> getImages() {
        return images;
    }

    @JsonProperty("images")
    public void setImages(List<Image> value) {
        this.images = value;
    }

    @JsonProperty("mediumImages")
    public List<Image> getMediumImages() {
        return mediumImages;
    }

    @JsonProperty("mediumImages")
    public void setMediumImages(List<Image> value) {
        this.mediumImages = value;
    }
}