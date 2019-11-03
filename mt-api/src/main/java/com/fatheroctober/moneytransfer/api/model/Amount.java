package com.fatheroctober.moneytransfer.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@XmlRootElement
public class Amount implements Serializable {
    @JsonProperty(required = true)
    private BigDecimal amount;

    @JsonProperty(required = true)
    private Currency currency;

    public Amount() {
    }

    public Amount(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
