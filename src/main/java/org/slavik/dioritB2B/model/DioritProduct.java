package org.slavik.dioritB2B.model;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DioritProduct {
    private UUID id;
    private Brand brand;
    private Brand catalog;
    private String sku;
    private String name;
    private Object description;
    private List<Map<String, Attribute>> attributes;
    private String mainPhoto;
    private String mainPhoto50;
    private String mainPhoto100;
    private String mainPhoto200;
    private List<String> photos;
    private long stock;
    private long price;

    private final double PERCENT_VALUE = 20;

    @JsonProperty("id")
    public UUID getID() { return id; }
    @JsonProperty("id")
    public void setID(UUID value) { this.id = value; }

    @JsonProperty("brand")
    public Brand getBrand() { return brand; }
    @JsonProperty("brand")
    public void setBrand(Brand value) { this.brand = value; }

    @JsonProperty("catalog")
    public Brand getCatalog() { return catalog; }
    @JsonProperty("catalog")
    public void setCatalog(Brand value) { this.catalog = value; }

    @JsonProperty("sku")
    public String getSku() { return sku; }
    @JsonProperty("sku")
    public void setSku(String value) { this.sku = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("description")
    public Object getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(Object value) { this.description = value; }

    @JsonProperty("attributes")
    public List<Map<String, Attribute>> getAttributes() { return attributes; }
    @JsonProperty("attributes")
    public void setAttributes(List<Map<String, Attribute>> value) { this.attributes = value; }

    @JsonProperty("main_photo")
    public String getMainPhoto() { return mainPhoto; }
    @JsonProperty("main_photo")
    public void setMainPhoto(String value) { this.mainPhoto = value; }

    @JsonProperty("main_photo_50")
    public String getMainPhoto50() { return mainPhoto50; }
    @JsonProperty("main_photo_50")
    public void setMainPhoto50(String value) { this.mainPhoto50 = value; }

    @JsonProperty("main_photo_100")
    public String getMainPhoto100() { return mainPhoto100; }
    @JsonProperty("main_photo_100")
    public void setMainPhoto100(String value) { this.mainPhoto100 = value; }

    @JsonProperty("main_photo_200")
    public String getMainPhoto200() { return mainPhoto200; }
    @JsonProperty("main_photo_200")
    public void setMainPhoto200(String value) { this.mainPhoto200 = value; }

    @JsonProperty("photos")
    public List<String> getPhotos() { return photos; }
    @JsonProperty("photos")
    public void setPhotos(List<String> value) { this.photos = value; }

    @JsonProperty("stock")
    public long getStock() { return stock; }
    @JsonProperty("stock")
    public void setStock(long value) { this.stock = value; }

    @JsonProperty("price")
    public long getPrice() { return (int) (price + ((price * PERCENT_VALUE) / 100)); }
    @JsonProperty("price")
    public void setPrice(int value) { this.price = value; }

    private Double extractDimension(String attributeName) {
        if (attributes == null) return null;

        for (Map<String, Attribute> attributeGroup : attributes) {
            Attribute attr = attributeGroup.get(attributeName);
            if (attr != null) {
                return attr.doubleValue != null ? attr.doubleValue : null;
            }
        }
        return null;
    }


    public int getProductId() {
        return 0;
    }
}
