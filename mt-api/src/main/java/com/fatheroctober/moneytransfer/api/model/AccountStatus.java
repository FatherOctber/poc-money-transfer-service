package com.fatheroctober.moneytransfer.api.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "accountStatus")
@XmlEnum
public enum AccountStatus {
    @XmlEnumValue(value = "active")
    ACTIVE,
    @XmlEnumValue(value = "closed")
    CLOSED
}
