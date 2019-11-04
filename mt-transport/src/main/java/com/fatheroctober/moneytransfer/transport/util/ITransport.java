package com.fatheroctober.moneytransfer.transport.util;

import javax.jms.Connection;

public interface ITransport {
    Connection connection() throws Exception;
}
