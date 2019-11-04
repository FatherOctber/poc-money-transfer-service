package com.fatheroctober.moneytransfer.transport.util;

import com.fatheroctober.moneytransfer.message.TransportMessage;

public interface ProcessStrategy {
    TransportMessage process(TransportMessage incomingRq);
}
