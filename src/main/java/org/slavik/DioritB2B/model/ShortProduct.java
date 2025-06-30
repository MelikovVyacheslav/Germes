package org.slavik.DioritB2B.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShortProduct {
    private UUID id;
    private String sku;
    private String name;
    private String description;
    private List<Map<String, Attribute>> attributes;
    private String mainPhoto;
    private String mainPhoto50;
    private String mainPhoto100;
    private String mainPhoto200;
    private List<String> photos;
    private int stock;
    private int price;

    private final double PERCENT_VALUE = 20;

    @JsonProperty("id")
    public UUID getID() { return id; }
    @JsonProperty("id")
    public void setID(UUID value) { this.id = value; }

    @JsonProperty("sku")
    public String getSku() { return sku; }
    @JsonProperty("sku")
    public void setSku(String value) { this.sku = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

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
    public int getStock() { return stock; }
    @JsonProperty("stock")
    public void setStock(int value) { this.stock = value; }

    @JsonProperty("price")
    public int getPrice() { return (int) (price + ((price * PERCENT_VALUE) / 100)); }
    @JsonProperty("price")
    public void setPrice(int value) { this.price = value; }

    public Double getHeight() {
        return extractDimension("Высота (см)");
    }

    public Double getWeight() {
        return extractDimension("Вес (кг)");
    }

    public Double getWidth() {
        return extractDimension("Ширина (см)");
    }

    public Double getLength() {
        return extractDimension("Глубина (см)");
    }

    public static Map<String, Object> extractAttributesExceptGroup(ShortProduct product) {
        Map<String, Object> result = new HashMap<>();

        if (product.getAttributes() == null) {
            return result;
        }

        for (Map<String, Attribute> attributeGroup : product.getAttributes()) {
            for (Map.Entry<String, Attribute> entry : attributeGroup.entrySet()) {
                String attributeName = entry.getKey();
                Attribute attribute = entry.getValue();
                if ("Группа".equals(attributeName)) {
                    continue;
                }
                if (attribute.doubleValue != null) {
                    result.put(attributeName, attribute.doubleValue);
                } else if (attribute.stringValue != null) {
                    result.put(attributeName, attribute.stringValue);
                }
            }
        }

        return result;
    }


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
}

