package org.slavik.OCS.model;

import com.fasterxml.jackson.annotation.*;

public class PackageInformation {
    private double weight;
    private double width;
    private double height;
    private double depth;
    private double volume;
    private long minOrderQuantity;
    private long multiplicity;
    private String units;

    @JsonProperty("weight")
    public double getWeight() { return weight; }
    @JsonProperty("weight")
    public void setWeight(double value) { this.weight = value; }

    @JsonProperty("width")
    public double getWidth() { return width; }
    @JsonProperty("width")
    public void setWidth(double value) { this.width = value; }

    @JsonProperty("height")
    public double getHeight() { return height; }
    @JsonProperty("height")
    public void setHeight(double value) { this.height = value; }

    @JsonProperty("depth")
    public double getDepth() { return depth; }
    @JsonProperty("depth")
    public void setDepth(double value) { this.depth = value; }

    @JsonProperty("volume")
    public double getVolume() { return volume; }
    @JsonProperty("volume")
    public void setVolume(double value) { this.volume = value; }

    @JsonProperty("minOrderQuantity")
    public long getMinOrderQuantity() { return minOrderQuantity; }
    @JsonProperty("minOrderQuantity")
    public void setMinOrderQuantity(long value) { this.minOrderQuantity = value; }

    @JsonProperty("multiplicity")
    public long getMultiplicity() { return multiplicity; }
    @JsonProperty("multiplicity")
    public void setMultiplicity(long value) { this.multiplicity = value; }

    @JsonProperty("units")
    public String getUnits() { return units; }
    @JsonProperty("units")
    public void setUnits(String value) { this.units = value; }
}


