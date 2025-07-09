package org.slavik.breez.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.FilterType;

import java.lang.reflect.Type;

public class Tech {
    private long idChar;
    private String title;
    private String type;
    private String unit;
    private String show;
    private String required;
    private String filter;
    private String filterType;
    private String first;
    private String supcat;
    private String analog;
    private String order;
    private String group;
    private String value;

    @JsonProperty("id_char")
    public long getIDChar() { return idChar; }
    @JsonProperty("id_char")
    public void setIDChar(long value) { this.idChar = value; }

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("type")
    public String getType() { return type; }
    @JsonProperty("type")
    public void setType(String value) { this.type = value; }

    @JsonProperty("unit")
    public String getUnit() { return unit; }
    @JsonProperty("unit")
    public void setUnit(String value) { this.unit = value; }

    @JsonProperty("show")
    public String getShow() { return show; }
    @JsonProperty("show")
    public void setShow(String value) { this.show = value; }

    @JsonProperty("required")
    public String getRequired() { return required; }
    @JsonProperty("required")
    public void setRequired(String value) { this.required = value; }

    @JsonProperty("filter")
    public String getFilter() { return filter; }
    @JsonProperty("filter")
    public void setFilter(String value) { this.filter = value; }

    @JsonProperty("filter_type")
    public String getFilterType() { return filterType; }
    @JsonProperty("filter_type")
    public void setFilterType(String value) { this.filterType = value; }

    @JsonProperty("first")
    public String getFirst() { return first; }
    @JsonProperty("first")
    public void setFirst(String value) { this.first = value; }

    @JsonProperty("supcat")
    public String getSupcat() { return supcat; }
    @JsonProperty("supcat")
    public void setSupcat(String value) { this.supcat = value; }

    @JsonProperty("analog")
    public String getAnalog() { return analog; }
    @JsonProperty("analog")
    public void setAnalog(String value) { this.analog = value; }

    @JsonProperty("order")
    public String getOrder() { return order; }
    @JsonProperty("order")
    public void setOrder(String value) { this.order = value; }

    @JsonProperty("group")
    public String getGroup() { return group; }
    @JsonProperty("group")
    public void setGroup(String value) { this.group = value; }

    @JsonProperty("value")
    public String getValue() { return value; }
    @JsonProperty("value")
    public void setValue(String value) { this.value = value; }
}
