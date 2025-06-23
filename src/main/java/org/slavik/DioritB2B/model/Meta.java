package org.slavik.DioritB2B.model;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

public class Meta {
    private long currentPage;
    private long from;
    private long lastPage;
    private List<Link> links;
    private String path;
    private long perPage;
    private long to;
    private long total;

    @JsonProperty("current_page")
    public long getCurrentPage() { return currentPage; }
    @JsonProperty("current_page")
    public void setCurrentPage(long value) { this.currentPage = value; }

    @JsonProperty("from")
    public long getFrom() { return from; }
    @JsonProperty("from")
    public void setFrom(long value) { this.from = value; }

    @JsonProperty("last_page")
    public long getLastPage() { return lastPage; }
    @JsonProperty("last_page")
    public void setLastPage(long value) { this.lastPage = value; }

    @JsonProperty("links")
    public List<Link> getLinks() { return links; }
    @JsonProperty("links")
    public void setLinks(List<Link> value) { this.links = value; }

    @JsonProperty("path")
    public String getPath() { return path; }
    @JsonProperty("path")
    public void setPath(String value) { this.path = value; }

    @JsonProperty("per_page")
    public long getPerPage() { return perPage; }
    @JsonProperty("per_page")
    public void setPerPage(long value) { this.perPage = value; }

    @JsonProperty("to")
    public long getTo() { return to; }
    @JsonProperty("to")
    public void setTo(long value) { this.to = value; }

    @JsonProperty("total")
    public long getTotal() { return total; }
    @JsonProperty("total")
    public void setTotal(long value) { this.total = value; }
}
