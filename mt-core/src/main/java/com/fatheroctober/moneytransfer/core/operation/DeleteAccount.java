package com.fatheroctober.moneytransfer.core.operation;

import com.fatheroctober.moneytransfer.message.TransportMessage;
import com.fatheroctober.moneytransfer.transport.util.ProcessStrategy;

public class DeleteAccount implements ProcessStrategy {
    @Override
    public TransportMessage process(TransportMessage incomingRq) {
        return null;
    }
}
