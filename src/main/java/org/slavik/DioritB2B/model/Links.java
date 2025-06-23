package org.slavik.DioritB2B.model;

import com.fasterxml.jackson.annotation.*;

public class Links {
    private String first;
    private String last;
    private Object prev;
    private String next;

    @JsonProperty("first")
    public String getFirst() { return first; }
    @JsonProperty("first")
    public void setFirst(String value) { this.first = value; }

    @JsonProperty("last")
    public String getLast() { return last; }
    @JsonProperty("last")
    public void setLast(String value) { this.last = value; }

    @JsonProperty("prev")
    public Object getPrev() { return prev; }
    @JsonProperty("prev")
    public void setPrev(Object value) { this.prev = value; }

    @JsonProperty("next")
    public String getNext() { return next; }
    @JsonProperty("next")
    public void setNext(String value) { this.next = value; }
}

