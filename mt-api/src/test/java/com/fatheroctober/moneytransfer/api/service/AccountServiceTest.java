package com.fatheroctober.moneytransfer.api.service;

import com.fatheroctober.moneytransfer.api.model.Account;
import com.fatheroctober.moneytransfer.api.model.Amount;
import com.fatheroctober.moneytransfer.api.model.Currency;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class AccountServiceTest extends BaseTest {

    private WebTarget accountClient;

    @Before
    public void setUp() {
        accountClient = ClientBuilder.newClient().target("http://localhost:8080/account");
    }

    @Test
    public void testGetAccount() {
        Response response = accountClient.path("123456").request().get();

        Assert.assertEquals(200, response.getStatus());
        Account account = response.readEntity(Account.class);
        Assert.assertEquals("123456", account.getNumber());
    }

    @Test
    public void testCreateAccount() {
        Account testAccount = testAccount();
        Response response = accountClient.path("create").request().post(Entity.entity(testAccount, MediaType.APPLICATION_JSON));

        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testDeleteAccount() {
        Response response = accountClient.path("12345").request().get();
    }

    private static Account testAccount() {
        return new Account("123456", new Amount(BigDecimal.TEN, Currency.RUR));
    }
}
