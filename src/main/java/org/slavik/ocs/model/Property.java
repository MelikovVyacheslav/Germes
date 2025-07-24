package org.slavik.ocs.model;

import com.fasterxml.jackson.annotation.*;
import kotlin.Unit;

import java.lang.reflect.Type;
import java.util.UUID;

public class Property {
    private UUID id;
    private String name;
    private Unit unit;
    private Type type;
    private Value value;
    private String group;
    private String description;

    @JsonProperty("id")
    public UUID getID() {
        return id;
    }

    @JsonProperty("id")
    public void setID(UUID value) {
        this.id = value;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("name")
    public void setName(String value) {
        this.name = value;
    }

    @JsonProperty("unit")
    public Unit getUnit() {
        return unit;
    }

    @JsonProperty("unit")
    public void setUnit(Unit value) {
        this.unit = value;
    }

    @JsonProperty("type")
    public Type getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(Type value) {
        this.type = value;
    }
}
