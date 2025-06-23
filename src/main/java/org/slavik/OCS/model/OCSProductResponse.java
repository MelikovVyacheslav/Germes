package org.slavik.OCS.model;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

public class OCSProductResponse {
    private List<Result> result;
    private List<Object> errors;

    @JsonProperty("result")
    public List<Result> getResult() { return result; }
    @JsonProperty("result")
    public void setResult(List<Result> value) { this.result = value; }

    @JsonProperty("errors")
    public List<Object> getErrors() { return errors; }
    @JsonProperty("errors")
    public void setErrors(List<Object> value) { this.errors = value; }
}