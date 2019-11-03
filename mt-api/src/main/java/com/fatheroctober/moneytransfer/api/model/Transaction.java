package com.fatheroctober.moneytransfer.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@XmlRootElement
public class Transaction implements Serializable {
    @JsonProperty(required = true)
    private String sourceAccount;

    @JsonProperty(required = true)
    private String targetAccount;

    @JsonProperty(required = true)
    private Amount amount;

    public Transaction() {

    }

    public Transaction(String sourceAccount, String targetAccount, Amount amount) {
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
    }

}
