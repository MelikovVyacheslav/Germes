package org.slavik.breez.model;

import com.fasterxml.jackson.annotation.*;

public class BreezCategory {
    private String title;
    private String chpu;
    private String level;
    private String order;

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("chpu")
    public String getChpu() { return chpu; }
    @JsonProperty("chpu")
    public void setChpu(String value) { this.chpu = value; }

    @JsonProperty("level")
    public String getLevel() { return level; }
    @JsonProperty("level")
    public void setLevel(String value) { this.level = value; }

    @JsonProperty("order")
    public String getOrder() { return order; }
    @JsonProperty("order")
    public void setOrder(String value) { this.order = value; }
}
