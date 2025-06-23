package org.slavik.OCS.model;

import com.fasterxml.jackson.annotation.*;
import org.slavik.entity.product.Product;

import java.util.List;

public class Result {
    private OCSProduct product;
    private boolean isAvailableForOrder;
    private PackageInformation packageInformation;
    private Price price;
    private List<Location> locations;

    @JsonProperty("product")
    public OCSProduct getProduct() { return product; }
    @JsonProperty("product")
    public void setProduct(OCSProduct value) { this.product = value; }

    @JsonProperty("isAvailableForOrder")
    public boolean getIsAvailableForOrder() { return isAvailableForOrder; }
    @JsonProperty("isAvailableForOrder")
    public void setIsAvailableForOrder(boolean value) { this.isAvailableForOrder = value; }

    @JsonProperty("packageInformation")
    public PackageInformation getPackageInformation() { return packageInformation; }
    @JsonProperty("packageInformation")
    public void setPackageInformation(PackageInformation value) { this.packageInformation = value; }

    @JsonProperty("price")
    public Price getPrice() { return price; }
    @JsonProperty("price")
    public void setPrice(Price value) { this.price = value; }

    @JsonProperty("locations")
    public List<Location> getLocations() { return locations; }
    @JsonProperty("locations")
    public void setLocations(List<Location> value) { this.locations = value; }
}