package org.slavik.OCS;

import com.fasterxml.jackson.annotation.*;
import org.slavik.entity.product.Product;

import java.util.List;

public class OCSCategory {
    private List<Product> products;
    private String category;
    private String name;
    private List<OCSCategory> children;

    @JsonProperty("category")
    public String getCategory() { return category; }
    @JsonProperty("category")
    public void setCategory(String value) { this.category = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("children")
    public List<OCSCategory> getChildren() { return children; }
    @JsonProperty("children")
    public void setChildren(List<OCSCategory> value) { this.children = value; }

    public void setProducts(List<Product> products) {
    }

    public List<Product> getProducts() {
        return products;
    }
}