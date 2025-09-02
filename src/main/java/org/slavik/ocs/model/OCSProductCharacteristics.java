package org.slavik.ocs.model;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;

public class OCSProductCharacteristics {
    private List<ResultCharacteristics> result = new ArrayList<>();
    private List<Object> errors;

    @JsonProperty("result")
    public List<ResultCharacteristics> getResult() { return result; }
    @JsonProperty("result")
    public void setResult(List<ResultCharacteristics> value) { this.result = value; }

    @JsonProperty("errors")
    public List<Object> getErrors() { return errors; }
    @JsonProperty("errors")
    public void setErrors(List<Object> value) { this.errors = value; }
}

