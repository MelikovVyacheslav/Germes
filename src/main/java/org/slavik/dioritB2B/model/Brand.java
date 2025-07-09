package org.slavik.dioritB2B.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Brand {
    private UUID id;
    private String name;

    @JsonProperty("id")
    public UUID getID() { return id; }
    @JsonProperty("id")
    public void setID(UUID value) { this.id = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }
}
