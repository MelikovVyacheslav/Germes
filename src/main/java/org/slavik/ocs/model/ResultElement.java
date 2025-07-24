package org.slavik.ocs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ResultElement {
        private String itemID;
        private String partNumber;
        private String productName;
        private String producer;
        private String itemName;
        private String description;
        private List<Property> properties;
        private List<Image> images;
        private List<Object> mediumImages;

        @JsonProperty("itemId")
        public String getItemID() { return itemID; }
        @JsonProperty("itemId")
        public void setItemID(String value) { this.itemID = value; }

        @JsonProperty("partNumber")
        public String getPartNumber() { return partNumber; }
        @JsonProperty("partNumber")
        public void setPartNumber(String value) { this.partNumber = value; }

        @JsonProperty("productName")
        public String getProductName() { return productName; }
        @JsonProperty("productName")
        public void setProductName(String value) { this.productName = value; }

        @JsonProperty("producer")
        public String getProducer() { return producer; }
        @JsonProperty("producer")
        public void setProducer(String value) { this.producer = value; }

        @JsonProperty("itemName")
        public String getItemName() { return itemName; }
        @JsonProperty("itemName")
        public void setItemName(String value) { this.itemName = value; }

        @JsonProperty("description")
        public String getDescription() { return description; }
        @JsonProperty("description")
        public void setDescription(String value) { this.description = value; }

        @JsonProperty("properties")
        public List<Property> getProperties() { return properties; }
        @JsonProperty("properties")
        public void setProperties(List<Property> value) { this.properties = value; }

        @JsonProperty("images")
        public List<Image> getImages() { return images; }
        @JsonProperty("images")
        public void setImages(List<Image> value) { this.images = value; }

        @JsonProperty("mediumImages")
        public List<Object> getMediumImages() { return mediumImages; }
        @JsonProperty("mediumImages")
        public void setMediumImages(List<Object> value) { this.mediumImages = value; }

    }
