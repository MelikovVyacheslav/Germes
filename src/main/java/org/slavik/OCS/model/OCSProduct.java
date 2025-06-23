package org.slavik.OCS.model;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

public class OCSProduct {
    private String itemID;
    private String productKey;
    private String partNumber;
    private String producer;
    private String category;
    private String itemName;
    private String itemNameRus;
    private String productName;
    private String productDescription;
    private String lineCode;
    private String hsCode;
    private boolean traceable;
    private String condition;
    private String warranty;
    private long vatPercent;
    private boolean serialNumberAvailability;
    private String originalCountryISOCode;
    private List<CatalogPath> catalogPath;
    private String eaN128;

    @JsonProperty("itemId")
    public String getItemID() { return itemID; }
    @JsonProperty("itemId")
    public void setItemID(String value) { this.itemID = value; }

    @JsonProperty("productKey")
    public String getProductKey() { return productKey; }
    @JsonProperty("productKey")
    public void setProductKey(String value) { this.productKey = value; }

    @JsonProperty("partNumber")
    public String getPartNumber() { return partNumber; }
    @JsonProperty("partNumber")
    public void setPartNumber(String value) { this.partNumber = value; }

    @JsonProperty("producer")
    public String getProducer() { return producer; }
    @JsonProperty("producer")
    public void setProducer(String value) { this.producer = value; }

    @JsonProperty("category")
    public String getCategory() { return category; }
    @JsonProperty("category")
    public void setCategory(String value) { this.category = value; }

    @JsonProperty("itemName")
    public String getItemName() { return itemName; }
    @JsonProperty("itemName")
    public void setItemName(String value) { this.itemName = value; }

    @JsonProperty("itemNameRus")
    public String getItemNameRus() { return itemNameRus; }
    @JsonProperty("itemNameRus")
    public void setItemNameRus(String value) { this.itemNameRus = value; }

    @JsonProperty("productName")
    public String getProductName() { return productName; }
    @JsonProperty("productName")
    public void setProductName(String value) { this.productName = value; }

    @JsonProperty("productDescription")
    public String getProductDescription() { return productDescription; }
    @JsonProperty("productDescription")
    public void setProductDescription(String value) { this.productDescription = value; }

    @JsonProperty("lineCode")
    public String getLineCode() { return lineCode; }
    @JsonProperty("lineCode")
    public void setLineCode(String value) { this.lineCode = value; }

    @JsonProperty("hsCode")
    public String getHsCode() { return hsCode; }
    @JsonProperty("hsCode")
    public void setHsCode(String value) { this.hsCode = value; }

    @JsonProperty("traceable")
    public boolean getTraceable() { return traceable; }
    @JsonProperty("traceable")
    public void setTraceable(boolean value) { this.traceable = value; }

    @JsonProperty("condition")
    public String getCondition() { return condition; }
    @JsonProperty("condition")
    public void setCondition(String value) { this.condition = value; }

    @JsonProperty("warranty")
    public String getWarranty() { return warranty; }
    @JsonProperty("warranty")
    public void setWarranty(String value) { this.warranty = value; }

    @JsonProperty("vatPercent")
    public long getVatPercent() { return vatPercent; }
    @JsonProperty("vatPercent")
    public void setVatPercent(long value) { this.vatPercent = value; }

    @JsonProperty("serialNumberAvailability")
    public boolean getSerialNumberAvailability() { return serialNumberAvailability; }
    @JsonProperty("serialNumberAvailability")
    public void setSerialNumberAvailability(boolean value) { this.serialNumberAvailability = value; }

    @JsonProperty("originalCountryISOCode")
    public String getOriginalCountryISOCode() { return originalCountryISOCode; }
    @JsonProperty("originalCountryISOCode")
    public void setOriginalCountryISOCode(String value) { this.originalCountryISOCode = value; }

    @JsonProperty("catalogPath")
    public List<CatalogPath> getCatalogPath() { return catalogPath; }
    @JsonProperty("catalogPath")
    public void setCatalogPath(List<CatalogPath> value) { this.catalogPath = value; }

    @JsonProperty("eaN128")
    public String getEaN128() { return eaN128; }
    @JsonProperty("eaN128")
    public void setEaN128(String value) { this.eaN128 = value; }
}
