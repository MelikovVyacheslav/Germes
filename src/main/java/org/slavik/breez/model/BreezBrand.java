package org.slavik.breez.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BreezBrand {
    private String title;
    private String chpu;
    private String image;
    private String url;
    private String order;

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("chpu")
    public String getChpu() { return chpu; }
    @JsonProperty("chpu")
    public void setChpu(String value) { this.chpu = value; }

    @JsonProperty("image")
    public String getImage() { return image; }
    @JsonProperty("image")
    public void setImage(String value) { this.image = value; }

    @JsonProperty("url")
    public String getURL() { return url; }
    @JsonProperty("url")
    public void setURL(String value) { this.url = value; }

    @JsonProperty("order")
    public String getOrder() { return order; }
    @JsonProperty("order")
    public void setOrder(String value) { this.order = value; }
}
