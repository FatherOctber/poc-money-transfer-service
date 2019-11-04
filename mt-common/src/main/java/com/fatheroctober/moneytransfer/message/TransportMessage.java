package com.fatheroctober.moneytransfer.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransportMessage<T extends Serializable> implements Serializable {
    private String msgId;
    private Boolean error;
    private T body;

    public TransportMessage() {
    }

    public TransportMessage(String msgId, T body) {
        this.msgId = msgId;
        this.body = body;
    }

    public static TransportMessage<String> error(String msgId, String errorMsg) {
        TransportMessage<String> err = new TransportMessage<>(msgId, errorMsg);
        err.setError(true);
        return err;
    }
}
