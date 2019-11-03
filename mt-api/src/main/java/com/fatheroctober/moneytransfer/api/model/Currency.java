package com.fatheroctober.moneytransfer.api.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "currency")
@XmlEnum
public enum Currency {
    @XmlEnumValue(value = "RUR")
    RUR,
    @XmlEnumValue(value = "EUR")
    EUR
}
