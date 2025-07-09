package org.slavik.breez.model;

import com.fasterxml.jackson.annotation.*;

public class Price {
    private int ric;
    private String ricCurrency;

    @JsonProperty("ric")
    public int getRic() { return ric; }
    @JsonProperty("ric")
    public void setRic(int value) { this.ric = value; }

    @JsonProperty("ric_currency")
    public String getRicCurrency() { return ricCurrency; }
    @JsonProperty("ric_currency")
    public void setRicCurrency(String value) { this.ricCurrency = value; }
}
