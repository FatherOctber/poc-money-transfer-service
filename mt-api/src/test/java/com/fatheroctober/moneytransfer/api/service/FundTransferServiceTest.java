package com.fatheroctober.moneytransfer.api.service;

import com.fatheroctober.moneytransfer.api.model.Account;
import com.fatheroctober.moneytransfer.api.model.Amount;
import com.fatheroctober.moneytransfer.api.model.Currency;
import com.fatheroctober.moneytransfer.api.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class FundTransferServiceTest extends BaseTest {

    private WebTarget transferClient;

    @Before
    public void setUp() {
        transferClient = ClientBuilder.newClient().target("http://localhost:8080/transfer");
    }


    @Test
    public void testCreateAccount() {
        Transaction transaction = new Transaction("12345", "54321", new Amount(BigDecimal.TEN, Currency.RUR));
        Response response = transferClient.request().post(Entity.entity(transaction, MediaType.APPLICATION_JSON));

        Assert.assertEquals(200, response.getStatus());
    }

}
