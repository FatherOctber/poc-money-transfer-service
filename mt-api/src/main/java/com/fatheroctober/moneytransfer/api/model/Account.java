package com.fatheroctober.moneytransfer.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@XmlRootElement
public class Account implements Serializable {
    @JsonProperty(required = true)
    private String number;

    @JsonProperty
    private String clientUID;

    @JsonProperty
    private Amount balance;

    @JsonProperty
    private AccountStatus status;

    public Account() {
    }

    public Account(String number, Amount balance) {
        this.number = number;
        this.balance = balance;
    }
}
