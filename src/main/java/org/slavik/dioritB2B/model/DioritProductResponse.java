package org.slavik.dioritB2B.model;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

public class DioritProductResponse {
    private List<ShortProduct> data;
    private Links links;
    private Meta meta;

    @JsonProperty("data")
    public List<ShortProduct> getData() { return data; }
    @JsonProperty("data")
    public void setData(List<ShortProduct> value) { this.data = value; }

    @JsonProperty("links")
    public Links getLinks() { return links; }
    @JsonProperty("links")
    public void setLinks(Links value) { this.links = value; }

    @JsonProperty("meta")
    public Meta getMeta() { return meta; }
    @JsonProperty("meta")
    public void setMeta(Meta value) { this.meta = value; }
}
