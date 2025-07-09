package org.slavik.ocs.model;

import com.fasterxml.jackson.annotation.*;

public class CatalogPath {
    private String category;
    private String name;

    @JsonProperty("category")
    public String getCategory() { return category; }
    @JsonProperty("category")
    public void setCategory(String value) { this.category = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }
}