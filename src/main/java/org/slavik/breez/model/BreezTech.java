package org.slavik.breez.model;

import com.fasterxml.jackson.annotation.*;
import java.util.Map;

public class BreezTech {
    private String nc;
    private String ncVnutr;
    private String ncNaruj;
    private String ncAccessory;
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

    @JsonProperty("techs")
    public Map<String, Tech> getTechs() { return techs; }
    @JsonProperty("techs")
    public void setTechs(Map<String, Tech> value) { this.techs = value; }
}
