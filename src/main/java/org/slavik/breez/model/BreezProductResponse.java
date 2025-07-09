package org.slavik.breez.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class BreezProductResponse {
    private String nc;
    private String ncVnutr;
    private String ncNaruj;
    private String ncAccessory;
    private Price price;
    private String articul;
    private String categoryID;
    private String series;
    private String title;
    private String brand;
    private String utp;
    private String description;
    private String booklet;
    private String manual;
    private String bimModel;
    private String videoYoutube;
    private List<String> images;
    private Map<String, Tech> techs;

    @JsonProperty("nc")
    public String getNc() { return nc; }
    @JsonProperty("nc")
    public void setNc(String value) { this.nc = value; }

    @JsonProperty("nc_vnutr")
    public String getNcVnutr() { return ncVnutr; }
    @JsonProperty("nc_vnutr")
    public void setNcVnutr(String value) { this.ncVnutr = value; }

    @JsonProperty("nc_naruj")
    public String getNcNaruj() { return ncNaruj; }
    @JsonProperty("nc_naruj")
    public void setNcNaruj(String value) { this.ncNaruj = value; }

    @JsonProperty("nc_accessory")
    public String getNcAccessory() { return ncAccessory; }
    @JsonProperty("nc_accessory")
    public void setNcAccessory(String value) { this.ncAccessory = value; }

    @JsonProperty("price")
    public Price getPrice() { return price; }
    @JsonProperty("price")
    public void setPrice(Price value) { this.price = value; }

    @JsonProperty("articul")
    public String getArticul() { return articul; }
    @JsonProperty("articul")
    public void setArticul(String value) { this.articul = value; }

    @JsonProperty("category_id")
    public String getCategoryID() { return categoryID; }
    @JsonProperty("category_id")
    public void setCategoryID(String value) { this.categoryID = value; }

    @JsonProperty("series")
    public String getSeries() { return series; }
    @JsonProperty("series")
    public void setSeries(String value) { this.series = value; }

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("brand")
    public String getBrand() { return brand; }
    @JsonProperty("brand")
    public void setBrand(String value) { this.brand = value; }

    @JsonProperty("utp")
    public String getUTP() { return utp; }
    @JsonProperty("utp")
    public void setUTP(String value) { this.utp = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("booklet")
    public String getBooklet() { return booklet; }
    @JsonProperty("booklet")
    public void setBooklet(String value) { this.booklet = value; }

    @JsonProperty("manual")
    public String getManual() { return manual; }
    @JsonProperty("manual")
    public void setManual(String value) { this.manual = value; }

    @JsonProperty("bim_model")
    public String getBimModel() { return bimModel; }
    @JsonProperty("bim_model")
    public void setBimModel(String value) { this.bimModel = value; }

    @JsonProperty("video_youtube")
    public String getVideoYoutube() { return videoYoutube; }
    @JsonProperty("video_youtube")
    public void setVideoYoutube(String value) { this.videoYoutube = value; }

    @JsonProperty("images")
    public List<String> getImages() { return images; }
    @JsonProperty("images")
    public void setImages(List<String> value) { this.images = value; }

    @JsonProperty("techs")
    public Map<String, Tech> getTechs() { return techs; }
    @JsonProperty("techs")
    public void setTechs(Map<String, Tech> value) { this.techs = value; }
}